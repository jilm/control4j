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

package cz.control4j.modules.comparison;

import cz.control4j.Input;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;

/**
 *  Compares three signals and returns true only if the third signal
 *  is greater than the first one and smaller than the second one.
 */
@Input(alias="min", index=0)
@Input(alias="max", index=1)
@Input(alias="in", index=2)
@Output(alias="out", index=0)
public class PMBetween extends ProcessModule {

  /**
   *  Compares three signals and returns true only if the third signal
   *  is greater than the first one and smaller than the second one.
   *
   *  <p>It expects the input to be an array of size at least three
   *  elements. The third and each subsequent element is compared
   *  with the first two elements. If it is greater or equal to the
   *  first one and simoultaneously if it is smaller or equal to the
   *  second one, the appropriate output signal is true. Otherwise the
   *  output signal is false. The size of the returned array is
   *  equal to the size of the input array minus two. The first output
   *  element is the result of comparison of the third input element,
   *  the second output element is the result of comparison of the
   *  fourth input element, etc.
   *
   *  <p>If the first or the second input element is invalid, all
   *  of the output elements are invalid. If the third or any subsequent
   *  element is invalid, only the corresponding output element signal
   *  is invalid.
   *
   *  <p>Timestamp of the output signal is set to the timestamp
   *  of corresponding input signal.
   *
   *  @param input
   *             must be an array of size at least three elements.
   *
   *  @return an array of size that is equat to the size of input
   *             array minus two
   */
  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    int validity = SignalUtils.getValidityStatus(input, inputLength);
    double min = SignalUtils.getValue(input, inputLength, 0);
    double max = SignalUtils.getValue(input, inputLength, 1);
    double inp = SignalUtils.getValue(input, inputLength, 2);
    switch (validity) {
      case 0b101:
        output[0] = inp < min
            ? Signal.getSignal(false, input[2].getTimestamp())
            : Signal.getSignal(input[2].getTimestamp());
        break;
      case 0b110:
        output[0] = inp > max
            ? Signal.getSignal(false, input[2].getTimestamp())
            : Signal.getSignal(input[2].getTimestamp());
        break;
      case 0b111:
        output[0] = Signal.getSignal(inp >= min && inp <= max, input[2].getTimestamp());
        break;
      default:
        output[0] = SignalUtils.getSignal();
        break;
    }
  }

}
