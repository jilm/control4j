/*
 *  Copyright 2013, 2015, 2016 Jiri Lidinsky
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

package cz.control4j.modules;

import cz.control4j.Input;
import cz.control4j.ModuleUtils;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;

/**
 *  Selects one of several input signals and forwards the selected input
 *  to the output.
 */
@Input(alias="select", index=0)
@Output(alias="out", index=0)
public class PMMultiplexer extends ProcessModule {

  /**
   *  Selects one of several input signals and forwards the selected input
   *  to the output.
   *
   *  <p>It accepts two or more inputs. The first input is select input,
   *  it is interpreted as a scalar integer value. The input with index
   *  i is forwarded (copied) to the output if and only if the select input
   *  is valid and its rounded value is i-1.
   *
   *  <p>The output is invalid if eather the selected input is invalid,
   *  or the select input is invalid, or value of select input is less
   *  than zero, or value of select input is greater than the number
   *  of inputs minus two.
   *
   *  <p>The output signal is eather the clone of the selected input
   *  or it is invalid signal with timestamp set to the current system
   *  time.
   *
   *  @param input
   *             an array of size at least two. The first element is
   *             interpreted as scalar integer.
   */
  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    //Signal[] result = new Signal[1];
    int size = inputLength - 1;
    if (input[0].isValid()) {
      int select = (int)Math.round(input[0].getValue());
      if (select < 0 || select > size - 1)
        output[0] = SignalUtils.getSignal();
      else
        output[0] = (Signal)input[select+1].clone();
    } else {
      output[0] = SignalUtils.getSignal();
    }
  }

  @Override
  public int getInputIndex(String key) {
    int index;
    try {
      index = Integer.parseInt(key);
    } catch (NumberFormatException e) {
      index = ModuleUtils.getInputIndex(this.getClass(), key);
    }
    if (index < 0) {
      throw ModuleUtils.createBadIOKeyException(this.getClass(), key);
    } else {
      return index;
    }
  }

}
