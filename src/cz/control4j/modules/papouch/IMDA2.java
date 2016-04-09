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

import cz.control4j.Input;
import cz.control4j.Signal;
import cz.lidinsky.spinel.SpinelMessage;

/**
 */
@Input(alias = "in1", index = 0)
@Input(alias = "in2", index = 1)
public class IMDA2 extends IMPapouch {

  @Override
  protected void put(Signal[] input, int inputLength) {
    if (inputLength > 0 && input[0] != null && input[0].isValid()) {
      request = createMessage(address, 0, input[0].getValue());
    } else {
      request = null;
    }
  }

  protected static SpinelMessage createMessage(int address, int channel, double value) {
    int raw = (int) (value * 4095);
    int msb = (raw & 0xf00) / 0x100;
    int lsb = raw & 0xff;
    int[] data = new int[]{channel + 1, msb, lsb};
    SpinelMessage result = new SpinelMessage(address, 0x40, data);
    return result;
  }

}
