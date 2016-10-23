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
import cz.lidinsky.spinel.SpinelException;
import cz.lidinsky.spinel.SpinelMessage;
import java.util.concurrent.TimeoutException;

/**
 *
 *
 */
@Output(alias = "out", index = 0)
public class OMQuidoTemperature extends Papouch {

  private int status;

  /**
   * Creates a request for new measurement.
   */
  @Override
  public void prepare() {
    super.prepare();
    request
        = new SpinelMessage(
            address, cz.lidinsky.papouch.Quido.TEMPERATURE_MEASUREMENT, new int[]{1});
  }

  @Override
  protected void get(Signal[] output, int outputLength) {
    if (transaction != null && transaction.hasResponse()) {
      try {
        responseMessage = transaction.get(100);
        double temperature = cz.lidinsky.papouch.Quido.getOneTimeTemperatureMeasurement(responseMessage);
        output[0] = SignalUtils.getSignal(temperature);
      } catch (SpinelException spinelException) {
        status = 1;
      } catch (TimeoutException ex) {
        SignalUtils.fillInvalid(output, outputLength);
      }
    } else {
      SignalUtils.fillInvalid(output, outputLength);
    }
  }

}
