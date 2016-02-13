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

package cz.control4j.application;

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.text.StrUtils.isBlank;

/**
 *
 *  A key object which consists of a pair: a name and a scope.
 *
 */
class NameScopeKey {

  private String name;
  private Scope scope;

  NameScopeKey(String name, Scope scope) {
    // param check
    if (isBlank(name) || scope == null) {
      throw new CommonException()
          .setCode(ExceptionCode.ILLEGAL_ARGUMENT)
          .set("message", "Given name is blank or given scope is null!")
          .set("name", name)
          .set("scope", scope);
    }
    // store param
    this.name = name;
    this.scope = scope;
  } // param check
  // store param

  /**
   *  Returns true if and only if the parameter is an instance
   *  of the Key class and the name equals to the objects name
   *  and scope equals to the keys scope.
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object instanceof NameScopeKey) {
      NameScopeKey key = (NameScopeKey) object;
      return getName().equals(key.getName()) && getScope() == key.getScope();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return getName().hashCode() ^ getScope().hashCode();
  }

  /**
   *  Returns name and scope fields in the human readable form.
   */
  @Override
  public String toString() {
    String pattern = "name: {0}, scope: {1}";
    return java.text.MessageFormat.format(pattern, getName(), getScope().toString());
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the scope
   */
  public Scope getScope() {
    return scope;
  }

}
