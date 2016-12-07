/*
 * Copyright (C) 2016 jilm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.control4j;

import cz.control4j.application.Connection;
import cz.control4j.application.Preprocessor;
import cz.control4j.application.ScopeHandler;
import cz.control4j.resources.Console;
import cz.lidinsky.logview.LogFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

/**
 * An entry point of the control4j application. This object is a singleton.
 */
public class Control {

  private static Control instance;

  private ControlLoop engine;

  private Console console;

  private Control() { }

  static {
    // application logger
    applicationLogger = Logger.getLogger("cz.control4j");
  }

  /** Application logger. */
  public static final Logger applicationLogger;

  /**
   * Returns application logger.
   *
   * @return application logger.
   */
  public static Logger getLogger() {
    return applicationLogger;
  }

  /**
   * @param args the command line arguments
   *
   * @throws java.lang.Exception
   *            Just to report all of the exceptions
   */
  public static void main(String[] args) throws Exception {
    // create an instance
    instance = new Control();
    // load application
    String filename = args[0];
    instance.loadApplication(filename);
  }

  private void loadApplication(String filename) throws Exception {
    // load application
    SocketHandler sh = new SocketHandler("localhost", 12347);
    sh.setFormatter(new LogFormatter());
    applicationLogger.addHandler(sh);
    applicationLogger.info(
        String.format("Going to load an application from the file: %s", filename));
    cz.control4j.application.c4j.C4j2Control translator
        = new cz.control4j.application.c4j.C4j2Control();
    cz.control4j.application.c4j.XMLHandler c4jHandler
        = new cz.control4j.application.c4j.XMLHandler(new ScopeHandler());
    c4jHandler.setDestination(translator);
    cz.lidinsky.tools.xml.XMLReader reader
        = new cz.lidinsky.tools.xml.XMLReader();
    reader.addHandler(c4jHandler);
    reader.load(new java.io.File(filename));
    // translate application
    applicationLogger.info("Going to translate the application...");
    Preprocessor preprocessor = new Preprocessor();
    translator.process(preprocessor);
    // preprocess application
    applicationLogger.info("Going to preprocess the application...");
    preprocessor.process();
    // sort application
    applicationLogger.info("Going to sort the application...");
    Sorter sorter = new Sorter();
    for (Connection connection : preprocessor.getConnections()) {
      Module source = connection.getProducer().getModule();
      connection.getConsumers().stream()
          .map(in -> in.getModule())
          .forEach(destination -> sorter.add(source, destination));
    }
    List<Module> sortedModules = new ArrayList<>();
    for (Module module : sorter) {
      sortedModules.add(module);
    }
    // build application
    applicationLogger.info("Going to build the application...");
    Builder builder = new Builder(sortedModules, preprocessor.getConnections());
    // run the application
    applicationLogger.info("Going to run the application...");
    int dataBufferSize = preprocessor.getConnections().size();
    engine = new ControlLoop();
    console = new Console();
    engine.addCycleEventListener(console);
    engine.run(builder.get(), dataBufferSize);
  }

  public static long getScanNumber() {
    return instance.engine.getScanNumber();
  }

  public static void print(String label, Signal signal) {
    instance.console.print(label, signal);
  }

}
