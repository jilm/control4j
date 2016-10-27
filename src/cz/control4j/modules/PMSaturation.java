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

package cz.control4j.modules;

import cz.control4j.Input;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import cz.lidinsky.tools.reflect.Setter;

/**
 *  Doesn't allow the input signal to exceed given limits. It takes one
 *  input and provides tree outputs. The first output is always between
 *  given limits. If the input is lower then min-limit, than the first
 *  output is min-limit and second output is true. If the input is
 *  greater than max-limit than the first output is max-limit and the
 *  threed output is true. If the input is between limits than the
 *  output is equal to the input and the second and threed outputs are
 *  false. If the input is invalid than all of the outputs are invalid.
 *
 *  <h3>Property</h3>
 *  <table><caption>Properties</caption>
 *      <tr>
 *          <td>minLimit</td>
 *          <td>The minimum value for the output signal.</td>
 *      </tr>
 *      <tr>
 *          <td>maxLimit</td>
 *          <td>The maximum value for the output signal.</td>
 *      </tr>
 *  </table>
 *
 *  <h3>IO</h3>
 *  <table><caption>IO</caption>
 *      <tr>
 *          <td>Input</td><td>0</td>
 *          <td>Input signal</td>
 *      </tr>
 *      <tr>
 *          <td>Output</td><td>0</td>
 *          <td>As long as the input signal is between given limits, this is
 *          the copy of input signal. If the input is out of given limit, it
 *          contains the limit value.</td>
 *      </tr>
 *  </table>
 */
@Input(alias="in", index=0)
@Output(alias="out", index=0)
@Output(alias="min", index=1)
@Output(alias="max", index=2)
public class PMSaturation extends ProcessModule {

  private double minLimit = Double.NEGATIVE_INFINITY;

  @Setter("min-limit")
  public void setMinLimit(double minLimit) {
    this.minLimit = minLimit;
  }

  private double maxLimit = Double.POSITIVE_INFINITY;

  @Setter("max-limit")
  public void setMaxLimit(double maxLimit) {
    this.maxLimit = maxLimit;
  }

  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    if (input[0].isValid()) {
      double value = input[0].getValue();
      if (value > maxLimit) {
        output[0] = Signal.getSignal(maxLimit, input[0].getTimestamp());
      } else if (value < minLimit) {
        output[0] = Signal.getSignal(minLimit, input[0].getTimestamp());
      } else {
        output[0] = Signal.getSignal(value, input[0].getTimestamp());
      }
      output[1] = Signal.getSignal(value < minLimit, input[0].getTimestamp());
      output[2] = Signal.getSignal(value > maxLimit, input[0].getTimestamp());
    } else {
      for (int i=0; i<3; i++)
        output[i] = SignalUtils.getSignal();
    }
  }

}
