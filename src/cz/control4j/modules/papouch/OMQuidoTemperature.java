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
import cz.lidinsky.papouch.Quido;
import cz.lidinsky.spinel.SpinelException;
import cz.lidinsky.spinel.SpinelMessage;

/**
 * Provides temperature output of the Quido module.
 */
@Output(alias = "out", index = 0)
public class OMQuidoTemperature extends OMPapouch {

  private int status;

  @Override
  protected SpinelMessage getRequest() {
    return new SpinelMessage(
            address, Quido.TEMPERATURE_MEASUREMENT, new int[]{1});
  }

  @Override
  protected void get(Signal[] output, int outputLength) {
    SpinelMessage responseMessage = getResponse();
    if (responseMessage != null) {
      try {
        double temperature = Quido.getOneTimeTemperatureMeasurement(responseMessage);
        output[0] = SignalUtils.getSignal(temperature);
      } catch (SpinelException spinelException) {
        status = 1;
      }
    } else {
      SignalUtils.fillInvalid(output, outputLength);
    }
  }

}
