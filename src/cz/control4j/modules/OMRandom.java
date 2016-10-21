/*
 * Copyright (C) 2016 jilm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.control4j.modules;

import cz.control4j.Output;
import cz.control4j.OutputModule;
import cz.control4j.RuntimeException;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;

/**
 *
 * @author jilm
 */
@Output(alias="out", index=0)
public class OMRandom extends OutputModule {

  @Override
  protected void get(Signal[] output, int outputLength) throws RuntimeException {
    output[0] = SignalUtils.getSignal(Math.random());
  }

}
