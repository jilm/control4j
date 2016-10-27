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
import java.util.Collection;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

/**
 *
 * A key, value data storage, where the key is composed of the
 * pair: a name and a scope.
 *
 * @param <E>
 *            value type
 */
public class ScopeMap<E extends Object> {

  /**
   *  Creates an empty storage.
   */
  public ScopeMap() { }

  //------------------------------------------- Back-end Storage Implemantation

  private HashMap<NameScopeKey, E> buffer;

  //------------------------------------------------------------ Access Methods

  /**
   *  Associates the given value with the specified key and scope.
   *
   * @param name
   *            identifier of the given value. The identifier must be unique
   *            for given scope
   *
   * @param scope
   *            an object which thogether with the name represents unique
   *            identifier for the value
   *
   * @param value
   *            the value which will be associater with given name and scope
   */
  public void put(String name, Scope scope, E value) {
    // param check
    if (value == null) {
      throw new CommonException()
        .setCode(ExceptionCode.ILLEGAL_ARGUMENT)
        .set("message", "The value may not be null!")
        .set("value", value)
        .set("name", name)
        .set("scope", scope);
    }
    // combined key
    NameScopeKey key = new NameScopeKey(name, scope);
    // lazy buffer
    if (buffer == null) {
      buffer = new HashMap<>();
    }
    // put the value
    E result = buffer.put(key, value);
    if (result != null) {
      throw new CommonException()
        .setCode(ExceptionCode.DUPLICATE_ELEMENT)
        .set("message", "Duplicate value")
        .set("name", name)
        .set("scope", scope)
        .set("value", value);
    }
  }

  public Collection<E> getValues() {
    return buffer.values();
  }

  /**
   * Return a key set.
   *
   * @return a key set
   */
  public Set<NameScopeKey> keySet() {
    return buffer.keySet();
  }

  /**
   *  Returns the value that is associated with given name and scope. If no
   *  value associated with given scope is found, parent of the given scope
   *  is searched.
   *
   *  @param name
   *             the identifier (key)
   *
   *  @param scope
   *             the scope object
   *
   *  @return value that is associated with given name and scope
   *
   *  @throws IllegalArgumentException
   *             if eather name or scope contain null value
   *
   *  @throws NoSuchElementException
   *             if there is no such value in the internal buffer
   */
  public E get(String name, Scope scope) {
    if (buffer == null) {
      throw new CommonException()
        .setCode(ExceptionCode.NO_SUCH_ELEMENT)
        .set("message", "The buffer is empty")
        .set("name", name)
        .set("scope", scope);
    }
    while (scope != null) {
      NameScopeKey tempKey = new NameScopeKey(name, scope);
      E result = buffer.get(tempKey);
      if (result != null) return result;
      scope = scope.getParent();
    }
    throw new CommonException()
      .setCode(ExceptionCode.NO_SUCH_ELEMENT)
      .set("message", "Missing element!")
      .set("name", name)
      .set("scope", scope);
  }

  public E getOrCreate(String name, Scope scope, Supplier<E> factory) {
    try {
      return get(name, scope);
    } catch (NoSuchElementException | CommonException e) {
      E newElement = factory.get();
      put(name, scope, newElement);
      return newElement;
    }
  }

  /**
   * Returns true if this object is empty, eg. it doesn't contain any value.
   *
   * @return true if this object is empty
   */
  public boolean isEmpty() {
    return buffer.isEmpty();
  }


  /**
   * Returns all of the value objects stored in this object.
   *
   * @return a collection of all of the values
   */
  public Collection<E> values() {
    if (buffer == null) {
      return emptyList();
    } else {
      return unmodifiableCollection(buffer.values());
    }
  }

}
