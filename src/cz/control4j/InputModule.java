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
   *             input signal values. Inputs that were not assigned contains
   *             null value. This array may be lager than the inputLength.
   *
   *  @param inputLength
   *             how many elements in the input array contains an input for
   *             this function. Don't use elements behind.
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
   * @param key
   *            requested key
   *
   * @return an index of the input with given index
   *
   * @throws CommonException
   *            if the given key is not supported by the module
   *
   * @throws CommonException
   *            if the module doesn't support inputs at all
   *
   * @see ModuleUtils#getInputIndex(java.lang.Class, java.lang.String)
   */
  public int getInputIndex(String key) {
    return ModuleUtils.getInputIndex(this.getClass(), key);
  }

  public int getInputSize() {
    return ModuleUtils.getInputSize(this.getClass());
  }

}
