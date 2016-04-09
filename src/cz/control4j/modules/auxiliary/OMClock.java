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

package cz.control4j.modules.auxiliary;


import cz.control4j.Output;
import cz.control4j.OutputModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import java.util.Calendar;

/**
 *
 *  Returns actual system time.
 *
 */
@Output(alias="sec", index=0)
@Output(alias="min", index=1)
@Output(alias="hour", index=2)
@Output(alias="day", index=3)
@Output(alias="month", index=4)
@Output(alias="year", index=5)
public class OMClock extends OutputModule
{

  /**
   *  Returns actual local system time. It returns an array of size
   *  six with following meaning:
   *  <ol>
   *    <li value="0"> second
   *    <li> minute
   *    <li> hour
   *    <li> day
   *    <li> month
   *    <li> year
   *  </ol>
   *
   */
  @Override
  public void get(Signal[] output, int outputLength) {

    Calendar calendar = Calendar.getInstance();
    out(0, SignalUtils.getSignal((double)calendar.get(Calendar.SECOND)),
	output, outputLength);
    out(1, SignalUtils.getSignal((double)calendar.get(Calendar.MINUTE)),
	output, outputLength);
    out(2, SignalUtils.getSignal((double)calendar.get(Calendar.HOUR_OF_DAY)),
	output, outputLength);
    out(3, SignalUtils.getSignal((double)calendar.get(Calendar.DATE)),
	output, outputLength);
    out(4, SignalUtils.getSignal((double)calendar.get(Calendar.MONTH) + 1),
	output, outputLength);
    out(5, SignalUtils.getSignal((double)calendar.get(Calendar.YEAR)),
	output, outputLength);
  }

  private void out(int index, Signal signal, Signal[] output, int outputLength)
  {
    if (index < outputLength)
      output[index] = signal;
  }

}
