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

package cz.control4j.modules.math;

import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import java.util.Arrays;

  /**
   *  Returns a sum of the input signal values.
   *
   *  <p>Provides only one output which is the sum of the input signal
   *  values. Number of input signals must be at least one.
   *
   *  <p>If there is one or more invalid input signals, then the
   *  output signal is invalid. Output signal is valid only if all
   *  of the input signals are valid.
   *
   *  <p>Timestamp of the output signal is set to the current system
   *  time.
   *
   */
@Output(alias="out", index=0)
public class PMSum extends ProcessModule {

  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    if (SignalUtils.allValid(input, inputLength)) {
      double sum = Arrays.stream(input, 0, inputLength)
        .mapToDouble(signal -> signal.getValue())
        .sum();
      output[0] = SignalUtils.getSignal(sum);
    }
  }

  private int inputCounter;

  @Override
  public int getInputIndex(String key) {
    return inputCounter++;
  }

}
