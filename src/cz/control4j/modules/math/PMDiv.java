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
   *  Provides mathematical operation division. It expects two inputs, first
   *  one is used as a dividend and the second one as a divisor.  It provides
   *  one output signal.
   *
   *  <p>The output signal is invalid if one of the input signals is invalid,
   *  or if the divisor is zero.
   *
   *  <p>Timestamp of the output signal is set to the system time.
   *
   */
@Input(alias="divident", index=0)
@Input(alias="divisor", index=1)
@Output(alias="out", index=0)
public class PMDiv extends ProcessModule {

  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength) {

    double a = SignalUtils.getValue(input, inputLength, 0);
    double b = SignalUtils.getValue(input, inputLength, 1);
    output[0] = SignalUtils.getSignal(a / b);
  }
}
