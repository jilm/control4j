/*
 *  Copyright 2015, 2016 Jiri Lidinsky
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

package cz.control4j.application.c4j;

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Stands for a signal element.
 *
 */
public class Signal extends DescriptionBase implements IDefinition {

  public Signal() {
    tags = new ArrayList<>();
  }

  private String name;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
      this.name = name;
  }

  private int scope;

  @Override
  public int getScope() {
    return scope;
  }

  @Override
  public void setScope(int scope) {
    this.scope = scope;
  }

  /*
   *
   *    Value for time t-1 specification
   *
   */

  private boolean isValueT_1Specified = false;
  private boolean isValueT_1Valid = false;
  private String valueT_1;

  void setInvalidDefaultValue() {
    isValueT_1Specified = true;
    isValueT_1Valid = false;
  }

  void setDefaultValue(String value) {
    valueT_1 = value;
    isValueT_1Specified = true;
    isValueT_1Valid = true;
  }

  /**
   *  Returns true if and only if the default value is specified for this
   *  signal.
   */
  boolean isDefaultValueSpecified() {
    return isValueT_1Specified;
  }

  /**
   *  Returns true if the specified default value should be valid.
   */
  boolean isDefaultValueValid() {
    return isValueT_1Valid;
  }

  String getDefaultValue() {
    if (!isValueT_1Specified) {
      throw new CommonException()
        .setCode(ExceptionCode.ILLEGAL_STATE)
        .set("message", "Default value for this signal was not specified!")
        .set("name", name)
        .set("scope", scope);
    } else if (!isValueT_1Valid) {
      throw new CommonException()
        .setCode(ExceptionCode.ILLEGAL_STATE)
        .set("message", "Default value for this signal is not valid!")
        .set("name", name)
        .set("scope", scope);
    } else {
      return valueT_1;
    }
  }

  //---------------------------------------------------------------------- Tags

  private final List<Tag> tags;

  public void add(Tag tag) {
    tags.add(notNull(tag));
  }

  public List<Tag> getTags() {
    return tags;
  }

}
