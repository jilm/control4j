/*
 *  Copyright 2013, 2015 Jiri Lidinsky
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
 *  Implements functionality of the comparator.  It compares values on input
 *  and returns a boolean value that indicates relationship between them. More
 *  precisely, it expects two input signals. First of all the difference
 *  between these two is computed: diff = input[1] - input[0]. The output
 *  signal will be false if diff &le; -hysteresis, the output will be true if
 *  diff &ge; hysteresis, and the output will stay unchanged otherwise.
 *
 *  Property: hysteresis, default 0, the positive real number is expected.
 *
 *  Input: 0, scalar real number, the reference input
 *
 *  Input: 1, scalar real number, the input to compare
 *
 *  Output: 0, scalar boolean value, if at least one of input signals is
 *  invalid, the output is invalid, otherwise, the output is valid. The
 *  timestamp of the signal will be equal to the timestamp of input one.
 */
@Input(alias="in", index=0)
@Input(alias="ref", index=1)
@Output(alias="out", index=0)
public class PMComparator extends ProcessModule
{

  public double hysteresis = 0;

  @Setter("hysteresis")
  public void setHysteresis(double hysteresis) {
    this.hysteresis = hysteresis;
  }

  private boolean oldValue = false;

  /**
   *  Returns result of input signals comparison; hysteresis is taken into
   *  consideration. Two input signals are expected. First of all, the
   *  differece between input signals is calculated: diff = input[+] -
   *  input[-]. True signal is returned for {@ diff >= hysteresis}, false
   *  signal is returned for diff <= -hysteresis and the output signal stays
   *  unchanged otherwise.
   *
   *  @param input
   *             must contain two Signal objects
   */
  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength)
  {
    if (input[0].isValid() && input[1].isValid())
    {
      double diff = input[1].getValue() - input[0].getValue();
      boolean value;
      if (diff >= hysteresis)
        value = true;
      else if (diff <= -hysteresis)
        value = false;
      else
        value = oldValue;
      oldValue = value;
      output[0] = SignalUtils.getSignal(value);
    }
    else
    {
      output[0] = SignalUtils.getSignal();
    }
  }
}
