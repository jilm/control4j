/*
 *  Copyright 2016 Jiri Lidinsky
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
package cz.control4j.modules.papouch;

import cz.control4j.Output;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import cz.lidinsky.papouch.AD4;
import cz.lidinsky.spinel.SpinelException;
import cz.lidinsky.spinel.SpinelMessage;

/**
 * Provides AD4 measurement results.
 */
@Output(alias = "out1", index = 0)
@Output(alias = "out2", index = 1)
@Output(alias = "out3", index = 2)
@Output(alias = "out4", index = 3)
public class OMAD4 extends OMPapouch {

  private int status;

  @Override
  protected SpinelMessage getRequest() {
    return new SpinelMessage(
            address, cz.lidinsky.papouch.AD4.MEASUREMENT, new int[]{0});
  }

  @Override
  protected void get(Signal[] output, int outputLength) {
    SpinelMessage responseMessage = getResponse();
    if (responseMessage != null) {
      try {
        int[] values = AD4.getOneTimeMeasurement(responseMessage);
        int[] status = AD4.getStatus(responseMessage);
        for (int i = 0; i < Math.min(4, outputLength); i++) {
          double value = (double) values[i] / 10000.0d;
          output[i] = ((status[i] & 0x80) == 0)
              ? SignalUtils.getSignal()
              : SignalUtils.getSignal(value);
        }
      } catch (SpinelException spinelException) {
        status = 1;
      }
    }
  }

}
