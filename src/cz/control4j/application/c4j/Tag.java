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

/**
 *
 *  Represents a tag of the signal.
 *
 */
public class Tag extends Configurable {

  public Tag() {}

  private String name;

  /**
   *  Returns the value of the property.
   */
  public String getName() {
    return name;
  }

  Tag setName(String name) {
    this.name = name;
    return this;
  }

}
