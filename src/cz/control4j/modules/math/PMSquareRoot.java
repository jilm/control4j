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

import cz.control4j.Input;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;

/**
 *  Calculates square root of its input.
 *
 *  <h3>IO</h3>
 *  <table><caption>IO</caption>
 *      <tr>
 *          <td>Input</td><td>0</td>
 *          <td>It expects a scalar real value</td>
 *      </tr>
 *      <tr>
 *          <td>Output</td><td>0</td>
 *          <td>Square root of the input value</td>
 *      </tr>
 *  </table>
 */
@Input(alias="in", index=0)
@Output(alias="out", index=1)
public class PMSquareRoot extends ProcessModule {

  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    double a = SignalUtils.getValue(input, inputLength, 0);
    output[0] = Signal.getSignal(
        Math.sqrt(a), input[0].getTimestamp());
  }

}
