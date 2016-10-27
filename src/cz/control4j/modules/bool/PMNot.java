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

package cz.control4j.modules.bool;

import cz.control4j.Input;
import cz.control4j.Output;
import cz.control4j.ProcessModule;
import cz.control4j.Signal;

/**
 *  Perform logical NOT operation on input.
 */
@Input(alias="in", index=0)
@Output(alias="out", index=0)
public class PMNot extends ProcessModule
{

  /**
   *  Perform logical NOT operation on input. It expects exactly one
   *  input and one output signal. The otput
   *  signal is invalid if and only if corresponging input signal is
   *  invalid. Timestamp of output signal is copy of timestamp of
   *  correspoing input signal.
   *
   *  @param input an array of size one. It shall not contain
   *         null value. Signals are treated to be boolean values.
   */
  @Override
  public void process(
      Signal[] input, int inputLength, Signal[] output, int outputLength)
  {
    if (input[0].isValid())
      output[0] = Signal.getSignal(
	  !input[0].getBoolean(), input[0].getTimestamp());
    else
      output[0] = Signal.getSignal(input[0].getTimestamp());
  }
}
