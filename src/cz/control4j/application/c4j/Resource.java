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
 *  Represents a resource element inside the module.  This object has two
 *  variants.
 *  <ol>
 *    <li>Resource fully described inside the module.
 *    <li>Resource which refers to some resource definition.
 *  </ol>
 *  For the first variant the class name must be specified, for the second one
 *  the href and scope are mandatory fields.
 *
 */
public class Resource extends Configurable implements IReference {

  public Resource() {}

  private String key;

  /**
   *
   * @return
   */
  public String getKey() {
    return key;
  }

  Resource setKey(String key) {
    this.key = key;
    return this;
  }

  private String className;

  public String getClassName() {
    //check();
    return className;
  }

  Resource setClassName(String className) {
    this.className = className;
    return this;
  }

  private String href;

  /**
   *
   * @return
   */
  @Override
    public String getHref() {
      //check();
      return href;
    }

  /**
   *
   * @param href
   */
  @Override
    public void setHref(String href) {
      this.href = href;
    }

  private Scope scope;

  /**
   *
   * @return
   */
  @Override
    public Scope getScope() {
      //check();
      return scope;
    }

  /**
   *
   * @param scope
   */
  @Override
    public void setScope(Scope scope) {
      this.scope = scope;
    }

  private boolean isReference;

  public boolean isReference() {
    //check();
    return isReference;
  }

}
