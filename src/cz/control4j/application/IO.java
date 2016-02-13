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

import cz.control4j.Module;
import static cz.lidinsky.tools.Validate.notNull;
import static cz.lidinsky.tools.text.StrUtils.isBlank;

/**
 *  This is a crate object for the module IO definition. It is immutable
 *  object. IO may represent an input or an output.
 */
public class IO {

  /**
   * The module, this IO belongs to. May not be null.
   */
  private final Module module;

  /**
   * An identifier of the IO. May be a blank value, because, some module do not
   * need to distinguish between IO. Inputs for SUM module is an example.
   */
  private final String key;

  /**
   * @param module
   *            the module this IO belongs to
   *
   * @param key
   *            in identifier of the IO, may have null or blank value
   *
   * @throws CommonException
   *            if the module is null
   */
  public IO(Module module, String key) {
    this.module = notNull(module);
    this.key = isBlank(key) ? null : key;
    this.pointer = -1;
  }

  /**
   * Returns the module, this IO belongs to.
   *
   * @return the module that is assiciated with this IO
   */
  public Module getModule() {
    return this.module;
  }

  /**
   * Get the identifier of this IO.
   *
   * @return the identifier of this IO
   */
  public String getKey() {
    return this.key;
  }

  /**
   *
   */
  protected int pointer;

  /**
   *
   * @param pointer
   */
  protected void setPointer(int pointer) {
    this.pointer = pointer;
  }

  /**
   *
   * @return
   */
  public int getPointer() {
    return pointer;
  }

}
