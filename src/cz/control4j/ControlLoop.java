/*
 *  Copyright 2013, 2014, 2015, 2016 Jiri Lidinsky
 *
 *  This file is part of control4j.
 *
 *  control4j is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3.
 *
 *  control4j is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with control4j.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.control4j;

import cz.control4j.tools.Tools;
import static cz.control4j.tools.Tools.catched;
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNegative;
import static cz.lidinsky.tools.Validate.notNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 *  This is the runtime engine of the control4j application.  After it is run,
 *  it enters into an infinite loop in which all of the control application
 *  modules are repeatedly executed.
 *
 *  <p>Duration of one scan may be fixed and it may be specified by
 *  configuration item cycle-period. If the processing of the scan is longer
 *  than specified the warnig is logged.
 *
 */
class ControlLoop {

  /**
   *  Duration of one scan. In ms.
   */
  private long cyclePeriod = 1000;


  /** The buffer for signals which serves as interchange point between
      outputs and inputs of the modules. */
  private DataBuffer dataBuffer;

  /**
   *  Start time of the actual scan.
   */
  private long scanStartTime;

  /**
   *  A flag that indicates that the request was received to terminate
   *  the program. Program will be terminated at the end of the actual scan.
   */
  private boolean exit = false;

  /**
   *  Duration of the last scan in ms.
   */
  private long lastScanDuration = 0l;

  /** List of all modules. */
  private List<ModuleCrate> modules;

  private final Set<ICycleEventListener> cycleListeners = new HashSet<>();

  /** True, if the dump file should be created, false otherwise. */
  protected boolean dump = true;

  /**
   *  It does nothing.
   */
  ControlLoop() {
    modules = new ArrayList<>();
  }

  /**
   *  Sets the required cycle period in ms.
   *
   *  @param period
   *             required period in ms. Must be a positive number. If zero, the
   *             period will be variable.
   */
  void setCyclePeriod(long period) {
    cyclePeriod = notNegative(period,
        "Cycle period property must be a positive number!");
  }

  /**
   *  Returns required cycle period in ms. This value is taken from global
   *  configuration, item "cycle-period".
   *
   *  @return required cycle period in ms
   */
  int getCyclePeriod() {
    return (int)cyclePeriod;
  }

  /** Delay between start of the scan and first module execution. In ms. */
  private long scanStartDelay = 400;

  /**
   *  Sets the delay between scan start and the instant where the first module
   *  is executed. May not be greater than cycle period. If so, one fifth of
   *  cycle period is used instead.
   *
   *  @param delay
   *             required start scan delay in ms. Must be a positive number. If
   *             it is zero, no delay will be used.
   *
   *  @throws CommonException
   *             if parameter is negative
   */
  void setStartScanDelay(long delay) {
    scanStartDelay = notNegative(delay,
        "Scan start delay property must be a positive number!");
  }

  /**
   *  Returns start scan delay in ms. If this delay should be greater than
   *  cycle period, ont fifth of the cycle period is returned instead.
   *
   *  @return start scan delay in ms
   */
  long getStartScanDelay() {
    return scanStartDelay < cyclePeriod
      ? scanStartDelay : (long)(cyclePeriod / 5);
  }

  /**
   * It is to set main properties of the runtime engine. This method should not
   * be called after the engine has start.
   *
   * <p>The properties are:
   * <ul>
   *   <li>cycle-period
   *   <li>scan-start-delay
   * </ul>
   */
  ControlLoop set(String key, String value) {
    try {
      if ("cycle-period".equals(key)) {
        setCyclePeriod(Long.parseLong(value));
      } else if ("scan-start-delay".equals(key)) {
        setCyclePeriod(Long.parseLong(value));
      } else {
        throw new CommonException()
            .set("message", "Unknown global property identifier!")
            .set("key", key)
            .set("value", value);
      }
    } catch (NumberFormatException e) {
      throw new CommonException()
          .setCode(ExceptionCode.PARSE)
          .set("message", "Number expected")
          .set("key", key)
          .set("value", value);
    }
    return this;
  }

  /**
   *  Terminates the control loop. The control loop is not terminated
   *  immediately. It is terminated after the current scan is finished. This
   *  method is not thread safe.
   */
  void exit() {
    exit = true;
  }

  /**
   *  Runs the infinite control loop. Following steps are performed:
   *
   *  <ol>
   *    <li> Creates {@link control4j.DataBuffer} instance.
   *    <li> Runs a {@link control4j.resources.Resource#prepare}
   *         method for all of the resources.
   *    <li> Runs a {@link control4j.Module#prepare} method for
   *         all of the modules.
   *    <li> Enters an infinite loop.
   *    <li> Note the cycle start time.
   *    <li> Clear the data buffer.
   *    <li> Trigger cycleStartEvent event for all of the registered
   *         listeners.
   *    <li> Wait for time specified by start-cycle-delay.
   *    <li> Trigger processingStartEvent event for all of the registered
   *         listeners.
   *    <li> Execute all of the modules.
   *    <li> Trigger cycleEndEvent event for all of the registered listeners.
   *    <li> Terminate the program if requested, see {@link #exit}.
   *    <li> Wait for new cycle. The duration of the cycle
   *         must be cycle-period.
   *  </ol>
   *
   *  @see control4j.ICycleEventListener
   */
  void run(List<ModuleCrate> modules, int bufferSize) {

    // Initialize data structures
    this.modules = notNull(modules);

    // Create data buffer
    dataBuffer = new DataBuffer(bufferSize);

    // Register event listeners
    modules.stream()
      .filter(crate -> crate.getModule() instanceof ICycleEventListener)
      .map(crate -> (ICycleEventListener)crate.getModule())
      .forEach(this::addCycleEventListener);

    // Initialize modules
    modules.stream()
      .map(crate -> crate.getModule())
      .forEach(module -> module.prepare());

    // Enter the control loop
    Control.getLogger().info("Runnig control loop...");
    ModuleCrate executedModule = null; // for dump purposes

    // prepare for execution
    //ResourceManager.getInstance().prepare(); // TODO:

    // The control loop !
    while (!exit) {

      try {
        scan();
      } catch (Exception e) {
        // if an exception arise during the processing some of the module, the
        // cycle is not completed and problem is logged.
        Control.getLogger().warning(new CommonException()
            .setCause(e)
            .set("message", "The scan was not finished because of exception!")
            .set("module", executedModule)
            .toString());
        //dump(e, executedModule.getModule()); // TODO:
      }
    }
  }

  /**
   * Performs one complete scan.
   */
  protected void scan() throws RuntimeException {
    scanStartTime = System.currentTimeMillis();
    // erase data buffer
    dataBuffer.clear();
    fireCycleStartEvent();
    // start cycle delay
    Tools.sleep(getStartScanDelay());
    fireProcessingStartEvent();
    // module execution
    Control.getLogger().fine("Start of module processing");
    for (ModuleCrate crate : modules) {
      crate.execute(dataBuffer);
    }
    fireCycleEndEvent();
    // wait for next turn
    long scanDuration;
    while (
        (scanDuration = System.currentTimeMillis() - scanStartTime)
        < cyclePeriod) {

      long sleepTime = cyclePeriod - scanDuration;
      Tools.sleep(sleepTime);
    }
    // last scan was too long
    if (cyclePeriod > 0l && scanDuration - cyclePeriod > 100l) {
        Control.getLogger().log(Level.WARNING, "Last scan was too long! {0}",
            Long.toString(scanDuration));
    }
    lastScanDuration = scanDuration;
  }

  /**
   *  Returns a system time in ms when the last cycle was started.
   *
   *  @return a system time in ms when the last cycle was started
   */
  long getScanStartTime() {
    return scanStartTime;
  }

  /**
   *  Duration of the last cycle in ms. It returns zero during the first
   *  cycle.
   *
   *  @return duration of the last cycle in ms
   */
  public long getLastCycleDuration() {
    return lastScanDuration;
  }


  void addCycleEventListener(ICycleEventListener listener) {
    cycleListeners.add(notNull(listener));
  }

  private void fireCycleStartEvent() {
    for (ICycleEventListener listener : cycleListeners) {
      listener.scanStart();
    }
  }

  private void fireCycleEndEvent() {
    for (ICycleEventListener listener : cycleListeners) {
      listener.scanEnd();
    }
  }

  private void fireProcessingStartEvent() {
    for (ICycleEventListener listener : cycleListeners) {
      listener.processingStart();
    }
  }

  /**
   *  Create a file which contains all of the available information
   *  that could be useful to find a problem. This method is called
   *  during the runtime if an exception was thrown.
   *
   *  <p>To prevent a colaps of the system, potentialy caused by dump
   *  files flood, the dump file is created only ones.
   *
   *  @param cause
   *             an exception which interrupted control loop, may be
   *             null
   */
  protected void dump(Exception cause, Module module)
  {
    java.io.PrintWriter writer = null;
    if (dump)
    try
    {
      // create dump file
      String tempDir = System.getProperty("java.io.tmpdir");
      String filename = "control4j_"
          + java.util.UUID.randomUUID().toString() + ".dump";
      java.io.File dumpFile = new java.io.File(tempDir, filename);
      writer = new java.io.PrintWriter(dumpFile);
      // write a timestamp
      writer.println("This is a control4j dump file.");
      writer.println(new java.util.Date().toString());
      // write system information
      writer.println(System.getProperty("java.version"));
      writer.println(System.getProperty("java.vendor"));
      writer.println(System.getProperty("os.name"));
      writer.println(System.getProperty("os.arch"));
      writer.println(System.getProperty("os.version"));
      // write the exception
      if (cause != null)
      {
        writer.println("EXCEPTION: " + cause.getClass().getName());
        writer.println(cause.getMessage());
        cause.printStackTrace(writer);
      }
      // write data buffer
      dataBuffer.dump(writer);
      // write the resources
      //ResourceManager.getInstance().dump(writer);
      // write the modules
      writer.println("----- Modules ------");
      for (ModuleCrate crate : modules) {
        writer.println(crate.getModule().toString());
      }
      //ModuleManager.getInstance().dump(writer);
      writer.println("----- Executed module ------");
      if (module != null) {
        writer.println(module.toString());
      }
      Control.getLogger().log(Level.INFO, "The dump file was created: {0}",
          dumpFile.getAbsolutePath());
      dump = false;
    }
    catch (java.io.IOException e)
    {
      catched(getClass().getName(), "dump", e);
      Control.getLogger().warning("Cannot create dump file");
    }
    finally
    {
      Tools.close(writer);
    }
  }

}
