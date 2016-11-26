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
import cz.lidinsky.papouch.Quido;
import cz.lidinsky.spinel.SpinelMessage;

/**
 */
@Input(alias = "in1", index = 0)
@Input(alias = "in2", index = 1)
@Input(alias = "in3", index = 2)
@Input(alias = "in4", index = 3)
@Input(alias = "in5", index = 4)
@Input(alias = "in6", index = 5)
@Input(alias = "in7", index = 6)
@Input(alias = "in8", index = 7)
public class IMQuidoBinary extends IMPapouch {

  private int[] data = new int[8];

  @Override
  protected void put(Signal[] input, int inputLength) {
    int counter = 0;
    for (int i = 0; i < inputLength; i++) {
      if (input[i] != null && input[i].isValid()) {
        data[counter++] = input[i].getBoolean() ? 0x80 | (i+1) : (i+1);
      }
    }
    request = new SpinelMessage(address, Quido.SET_OUTPUT, data, 0, counter);
    System.out.println(request.toString());
  }

}
