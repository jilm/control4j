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

package cz.control4j.modules;

import cz.control4j.Input;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;
import cz.lidinsky.tools.reflect.Setter;

/**
 *  TODO:
 *
 *  <p>
 *  </p>
 *
 *  <h3>Resources</h3>
 *  <table>
 *      <tr>
 *          <td>table-name</td>
 *          <td></td>
 *          <td>A name of the table to insert to. The string value is
 *          expected.</td>
 *      </tr>
 *  </table>
 *
 *  <h3>Property</h3>
 *  <table>
 *      <tr>
 *          <td>message</td>
 *          <td>The message which will be written into the log.</td>
 *      </tr>
 *  </table>
 *
 *  <h3>IO</h3>
 *  <table>
 *      <tr>
 *          <td>Input</td>
 *          <td>0</td>
 *          <td>The control input; it expects scalar boolean signal. The
 *          application exits after the value on this input becomes valid
 *          true.</td>
 *      </tr>
 *  </table>
 */
@Input(alias="in", index=0)
@Output(alias="out", index=0)
public class PMLinearTransform extends ProcessModule {

  private double mul = 1.0d;

  @Setter("mul")
  public void setMul(double mul) {
    this.mul = mul;
  }

  private double add = 0.0;

  @Setter("add")
  public void setAdd(double add) {
    this.add = add;
  }

  /**
   *  Calculates linear transformation of its input.
   */
  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    if (inputLength > 0 && outputLength > 0) {
      if (input[0].isValid()) {
        double value = input[0].getValue() * mul + add;
        output[0] = Signal.getSignal(value, input[0].getTimestamp());
      } else {
        output[0] = Signal.getSignal(input[0].getTimestamp());
      }
    }
  }

}
