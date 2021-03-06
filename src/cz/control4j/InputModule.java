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

import cz.control4j.application.IO;

/**
 *  Represents an input module, it is the module which takes input but
 *  doesn't provide any output for further processing. Typicaly it may
 *  be a module which writes values into the output hardware. Abstract
 *  class which must be extended by each input module.
 *
 *  <p>The {@link #put} method is dedicated for module functionality.
 *  Override this method and put the algorithm of the module there.
 */
public abstract class InputModule extends Module
{

  /**
   *  Method for the module functionality implementation. This method must be
   *  overwritten. It is regularily called by the {@link ControlLoop} during
   *  the processing.
   *
   *  @param input
   *             input signal values. Elements that were not assigned contains
   *             null value. This array may be lager than the inputLength.
   *             It is due to performance improvemnt.
   *
   *  @param inputLength
   *             how many elements in the input array contains an input for
   *             this function. Elements behind should stay untouched.
   *
   *  @throws cz.control4j.RuntimeException
   *             should be thrown only rarely in the extreme situations. If
   *             thrown, the current scan will be broken down.
   */
  protected abstract void put(Signal[] input, int inputLength)
    throws RuntimeException;

  /**
   * Returns index for the input with given key. Default implementation uses
   * input and output annotations to find the index out. This method may be
   * overriden to achieve more complex behaviour.
   *
   * <p>Returned index must be greater or equal to zero.
   *
   * TODO: when it is called?
   *
   * @param key
   *            requested key
   *
   * @return an index of the input with given index
   *
   * @see ModuleUtils#getInputIndex(java.lang.Class, java.lang.String)
   */
  public int getInputIndex(String key) {
    return ModuleUtils.getInputIndex(this.getClass(), key);
  }

  /**
   * It must return the index for each given IO that is supported by the
   * module. The default implementation calls the
   * {@link #getInputIndex(java.lang.String)} method. Override this method
   * if another behaviour is needed or if you need to collect information
   * about input signals. This method is called dureing the application
   * building.
   *
   * @param input
   *            information about signal which will be connected into this
   *            module
   *
   * @return index, a number greater or equal to zero. It is the position
   *            in the input array where the signal value will be placed
   *            during the control run.
   *
   * @see #getInputIndex(java.lang.String)
   */
  public int getInputIndex(IO input) {
    return getInputIndex(input.getKey());
  }

  /**
   * It must return how many input signals is supported by this module. The
   * default implemntation relay on the annotations to find this information
   * out. If another behaviour is needed, override this method.
   *
   * @return total number of input signal that is supported by the module
   *
   * @see ModuleUtils#getInputSize(java.lang.Class)
   */
  public int getInputSize() {
    return ModuleUtils.getInputSize(this.getClass());
  }

}
