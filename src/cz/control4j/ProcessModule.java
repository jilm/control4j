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
 *  Represents processing module, it is a module that gets input, performs
 *  some calculation on it and provides output.
 */
public abstract class ProcessModule extends Module
{

  /**
   *  This method should implement the module functionality.
   *
   *  @param input
   *             an array that contains the whole input for the processing. If
   *             some input is not connected, the corresponding element
   *             contains null value. For performance reasons, the array may
   *             have more elements than neccessary, do not use elements with
   *             indices greather nhan inputLength.
   *
   *  @param inputLength
   *             number of input signals in the input array
   *
   *  @param output
   *             an array to fill-in the output of the module. For performance
   *             reasons, the array may have more elements than neccessary. Do
   *             not use elements on indices greather than outputLength. Null
   *             elemnets will be replaced by invalid signals after the method
   *             ends.
   *
   *  @param outputLength
   *             number of elments in output array which should be filled
   *
   *  @throws RuntimeException
   *             should be thrown only in extreme situations in which the
   *             actual scan should not be finished. In most cases, the invalid
   *             output is sufficient solution to some processing problemns
   */
  public abstract void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength)
    throws RuntimeException;

  public int getInputIndex(String key) {
    return ModuleUtils.getInputIndex(this.getClass(), key);
  }

  public int getOutputIndex(String key) {
    return ModuleUtils.getOutputIndex(this.getClass(), key);
  }

  public int getInputSize() {
    return ModuleUtils.getInputSize(this.getClass());
  }

  public int getOutputSize() {
    return ModuleUtils.getOutputSize(this.getClass());
  }

}
