/*
 *  Copyright 2015 Jiri Lidinsky
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

import cz.lidinsky.tools.ToStringBuilder;

/**
 *
 *  Resource definition.
 *
 */
public class ResourceDef extends DescriptionBase implements IDefinition {

  public ResourceDef() {}

  /**
   *  Initialize fields of this object.
   */
  public ResourceDef(String className, String name, int scope) {
    setClassName(className);
    setName(name);
    setScope(scope);
  }

  /** Name of the java class that implements functionality of
      the resource */
  private String className;

  /**
   *  Returns the name of the java class that implements functionality
   *  of the resource.
   */
  public String getClassName() {
    //check();
    return className;
  }

  ResourceDef setClassName(String className) {
    this.className = className;
    return this;
  }

  /** Identification of this resource definition to be referenced. */
  private String name;
  private int scope;

  public String getName() {
    //check();
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScope() {
    //check();
    return scope;
  }

  public void setScope(int scope) {
    this.scope = scope;
  }

  @Override
  public void toString(ToStringBuilder builder) {
    super.toString(builder);
    builder.append("className", className)
        .append("name", name)
        .append("scope", scope);
  }

}
