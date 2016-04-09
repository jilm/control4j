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

package cz.control4j.modules;


//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;

import cz.control4j.Output;
import cz.control4j.OutputModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import cz.lidinsky.tools.reflect.Setter;

/**
 *  Output module, which returns just valid constant signal, which doesn't
 *  change in time.
 *
 *  Property: value, real number is expected.
 *
 *  Output: 0, Scalar constant value which is specified by the value property.
 *             It is always valid and with the actual timestamp.
 */
@Output(index=0, alias="out", description="Output of constant signal")
public class OMConst extends OutputModule {

  private double value;

  @Setter("value")
  public void setValue(double value) {
    this.value = value;
  }

  @Override
  protected void get(Signal[] output, int outputLength) {
    output[0] = SignalUtils.getSignal(value);
  }

}
