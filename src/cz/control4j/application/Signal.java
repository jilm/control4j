/*
 *  Copyright 2013, 2014, 2015, 2016 Jiri Lidinsky
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

package cz.control4j.application;

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.Validate;
import static cz.lidinsky.tools.text.StrUtils.isBlank;

/**
 *  Keeps signal definition.
 */
public class Signal extends Configurable {

  private final String name;

  /**
   * Crate a new signal definition object.
   *
   * @param name
   *            an identifier of the signal
   *
   * @throws CommonException
   *            if the given parameter is blank
   */
  public Signal(String name) {
    try {
      this.name = Validate.notBlank(name);
    } catch (Exception e) {
      throw new CommonException()
        .set("message", "The name of the signal may not be blank!")
        .set("name", name);
    }
  }

  //-------------------------------------------------------------------- Label.

  private String label;

  /**
   * Returns the label, which is short textual identification of the signal
   * which is dedicated mainly for use by the MMI clients and programs. If the
   * label has not been set yet, or if it has been set to blank value, the
   * name is returned instead.
   *
   * @return the label of the signal
   */
  public String getLabel() {
    return isBlank(label) ? name : label;
  }

  /**
   * Sets the label property of the signal.
   *
   * @param label
   *            the label of the signal
   */
  public void setLabel(String label) {
    this.label = label;
  }

  //--------------------------------------------------------------------- Unit.

  private String unit;

  /**
   * Returns signal unit.
   *
   * @return unit of the signal
   */
  public String getUnit() {
    return unit == null ? "" : unit;
  }

  /**
   * Sets the unit of the signal.
   *
   * @param unit
   *            the unit of the signal
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  //-------------------------------------------------------------------- Other.

}
