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

package cz.control4j.application;

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  A common base for all of the objects that are configurable.
 *  Each configuration item consists of a key and value. The
 *  key must be unique acros the configurable object.
 *
 *  <p>This object contains two kinds of configuration items.
 *  Configuration that was given in the form of key and value,
 *  and the one that was given in the form of reference to
 *  some define object.
 *
 */
public abstract class Configurable extends DeclarationBase {

  /**
   *  Configuration.
   */
  public Configurable() {
    this.configuration = new HashMap<>();
  }

  /** Internal storage for the configuration. */
  private final Map<String, Property> configuration;

  /**
   *  Returns true if and only if the key is already defined.
   *
   *  @param key
   *             requested key
   *
   *  @return true if the internal storage contains configuration under the
   *             the given key
   */
  public boolean containsKey(String key) {
    if (key == null) {
      throw new NullPointerException();
    } else {
      return configuration.containsKey(key);
    }
  }

  /**
   *  Puts given property into the internal buffer. If there already is the
   *  value, it is replaced by the new value.
   *
   *  @param key
   *             property identification
   *
   *  @param value
   *             property value
   *
   *  @return property object which was stored in the internal buffer
   */
  public Property putProperty(String key, String value) {
    return putProperty(key, new Property(value));
  }

  public Property putProperty(String key, Property property) {
    return configuration.put(key, notNull(property));
  }

  /**
   *  Returns the property value which is asociated with given key. This is
   *  simply a shorthand for <code>getProperty(key).getValue()</code>.
   *
   *  @param key identification of the value that is required
   *
   *  @return the value which was stored under the given key
   *
   *  @throws CommonException if the parameter key is blank
   *
   *  @throws CommonException (NO_SUCH_ELEMENT)
   *             if there is not a property associated with given key
   */
  public String getValue(String key) {
    Property property = getProperty(key);
    if (property != null) {
      return property.getValue();
    } else {
      throw new CommonException()
        .setCode(ExceptionCode.NO_SUCH_ELEMENT)
        .set("message", "There is not a property under the given key.")
        .set("key", key);
    }
  }

  /**
   * Returns property that is stored under the given key or <code>null</code>
   * if there is no property with given key.
   *
   * @param key required value identification
   *
   * @return the value that is asociated with given key
   */
  public Property getProperty(String key) {
    return configuration.get(key);
  }

  /**
   *  Returns resolved configuration.
   * @return
   */
  //public IConfigBuffer getConfiguration() {
    //ConfigBuffer buffer = new ConfigBuffer();
    //configuration.entrySet().stream().forEach((entry) -> {
    //    buffer.put(entry.getKey(), entry.getValue().getValue());
    //    });
    //return buffer;
  //}

  public void putConfiguration(Configurable source) {
    if (source != null) {
      this.configuration.putAll(source.configuration);
    }
  }

  @Override
  public String toString() {
    return configuration.toString();
  }

}
