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
 * Provides binary input of the Quido module.
 */
@Output(alias = "out1", index = 0)
@Output(alias = "out2", index = 1)
@Output(alias = "out3", index = 2)
@Output(alias = "out4", index = 3)
@Output(alias = "out5", index = 4)
@Output(alias = "out6", index = 5)
@Output(alias = "out7", index = 6)
@Output(alias = "out8", index = 7)
public class OMQuidoBinary extends OMPapouch {

  private int status;

  @Override
  protected SpinelMessage getRequest() {
    return new SpinelMessage(
            address, Quido.READ_BINARY_INPUT);
  }

  @Override
  protected void get(Signal[] output, int outputLength) {
    SpinelMessage responseMessage = getResponse();
    if (responseMessage != null) {
      try {
        int bin = Quido.getBinaryInput(responseMessage);
        int mask = 1;
        for (int i = 0; i < Math.min(8, outputLength); i++) {
          output[i] = SignalUtils.getSignal((bin & mask) > 0);
          mask *= 2;
        }
      } catch (SpinelException spinelException) {
        status = 1;
      }
    }
  }

}
