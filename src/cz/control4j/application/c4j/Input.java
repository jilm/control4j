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
 *  Represents an input element
 *
 */
public class Input extends Configurable implements IReference {

  public Input() {}

  private String index;

  public String getIndex() {
    return index;
  }

  Input setIndex(String index) {
    this.index = index;
    return this;
  }

  private String href;

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public void setHref(String href) {
    this.href = href;
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

}
