/*
 *  Copyright 2015, 2016 Jiri Lidinsky
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

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;

/**
 * Encapsulates a module together with the IO map. For application execution
 * the list of module crates that are in the correcr order is sufficient and
 * simultaneously necessary. Such a list is created by the
 * {@link Builder} class.
 */
public abstract class ModuleCrate {

  /**
   * Create and return appropriate crate object for the given module.
   *
   * @param module
   *            the module which will be wrapped up
   *
   * @param inputMap
   *            a map which describe connection between the signal pointer
   *            number and the position inside the given module input array.
   *            If the given module is just output module, this may be null
   *
   * @param outputMap
   *            a map which describe connection between the signal pointer
   *            number and the position inside the given module output array
   *            If given module is just input moudle, this may be null
   *
   * @return module crate object
   */
  static ModuleCrate create(
      Module module, int[] inputMap, int[] outputMap) {

    if (module instanceof InputModule) {
      return new InputModuleCrate((InputModule)module, inputMap);
    } else if (module instanceof OutputModule) {
      return new OutputModuleCrate((OutputModule)module, outputMap);
    } else if (module instanceof ProcessModule) {
      return new ProcessModuleCrate((ProcessModule)module, inputMap, outputMap);
    } else {
      throw new CommonException()
        .setCode(ExceptionCode.CLASS_CAST)
        .set("message", "Module implementation")
        .set("module class", module.getClass().getName());
    }
  }

  /**
   * Executes given module. If it is input or process module, the input is
   * get from given buffer, module is executed and the output is written back
   * to the buffer. Do not call this method directly. It is called regularly
   * by the Control loop.
   *
   * @param buffer
   *            the global buffer of signal values
   *
   * @throws RuntimeException
   *            the exception thrown by the module execution method
   */
  abstract void execute(DataBuffer buffer) throws RuntimeException;

  /**
   * Returns the wrapped module.
   *
   * @return the wrapped module
   */
  abstract Module getModule();

  private static Signal[] inputArray;

  protected static Signal[] getInputArray(int size) {
    if (inputArray == null)
      inputArray = new Signal[size];
    else if (inputArray.length < size)
      inputArray = new Signal[size];
    return inputArray;
  }

  private static Signal[] outputArray;

  protected static Signal[] getOutputArray(int size) {
    if (outputArray == null)
      outputArray = new Signal[size];
    else if (outputArray.length < size)
      outputArray = new Signal[size];
    return outputArray;
  }

  /**
   * Crate for input modules.
   */
  private static class InputModuleCrate extends ModuleCrate {

    /** The module, not null. */
    private final InputModule module;

    /** Input map, not null. */
    private final int[] inputMap;

    InputModuleCrate(InputModule module, int[] inputMap) {
      this.module = notNull(module);
      this.inputMap = notNull(inputMap);
    }

    @Override
      Module getModule() {
        return module;
      }

    @Override
    void execute(DataBuffer buffer) throws RuntimeException {
      Signal[] input = buffer.get(inputMap);
      module.put(input, inputMap.length);
    }

  }

  /**
   * A crate for output modules.
   */
  private static class OutputModuleCrate extends ModuleCrate {

    private final OutputModule module;
    private final int[] outputMap;

    OutputModuleCrate(OutputModule module, int[] outputMap) {
      this.module = notNull(module);
      this.outputMap = notNull(outputMap);
    }

    @Override
      Module getModule() {
        return module;
      }

    @Override
    void execute(DataBuffer buffer) throws RuntimeException {
      Signal[] output = getOutputArray(outputMap.length);
      module.get(output, outputMap.length);
      SignalUtils.fillInvalid(output, outputMap.length);
      buffer.put(output, outputMap);
    }

  }

  /**
   * A crate for process modules.
   */
  private static class ProcessModuleCrate extends ModuleCrate {

    private final ProcessModule module;
    private final int[] inputMap;
    private final int[] outputMap;

    ProcessModuleCrate(ProcessModule module, int[] inputMap, int[] outputMap) {
      this.module = notNull(module);
      this.inputMap = notNull(inputMap);
      this.outputMap = notNull(outputMap);
    }

    @Override
      Module getModule() {
        return module;
      }

    @Override
    void execute(DataBuffer buffer) throws RuntimeException {
      Signal[] input = buffer.get(inputMap);
      Signal[] output = getOutputArray(outputMap.length);
      module.process(input, inputMap.length, output, outputMap.length);
      SignalUtils.fillInvalid(output, outputMap.length);
      buffer.put(output, outputMap);
    }

  }

}
