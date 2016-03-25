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

import cz.control4j.application.Scope;

/**
 *
 *  Represents define element
 *
 */
public class Define extends DeclarationBase implements IDefinition {

  public Define() {}

  private String name;

  @Override
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  private Scope scope;

  @Override
  public Scope getScope() {
    return scope;
  }

  @Override
  public void setScope(final Scope scope) {
    this.scope = scope;
  }

  private String value;

  public String getValue() {
    return value;
  }

  Define setValue(final String value) {
    this.value = value;
    return this;
  }

}
