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

import cz.control4j.Module;

/**
 *
 * @author jilm
 */
public interface ApplicationHandler {

  /**
   * Adds a module for further processing.
   *
   * @param module
   *            a module to add
   *
   * @throws CommonException
   *             if the parameter is null
   */
  void add(Module module);

  void addModuleInput(ReferenceDecorator<Module> reference);

  void addPropertyReference(ReferenceDecorator<Configurable> reference);

  /**
   * Ends current local scope and returns to its parent scope.
   */
  void endScope();
  //--------------------------------------------------------------- Properties.

  void putDefinition(String name, Scope scope, String value);

  /**
   * Puts a module intput into the internal buffer for further processing.
   *
   * @param name
   *            signal name
   *
   * @param scope
   *            scope where the signal should be looked for
   *
   * @param input
   *            an input to add
   */
  void putModuleInput(String name, Scope scope, IO input);

  /**
   * Adds module output for further processing.
   *
   * @param name
   *            name of the signal
   *
   * @param scope
   *            scope from where the signal should be accessed
   * @param output
   *
   * @throws CommonException
   *            if either of the arguments is null
   *
   * @throws CommonException
   *            if there already is connection under the same name and scope
   *
   * @throws CommonException
   *            if there already is the same connection under the different
   *            name and or scope
   */
  void putModuleOutput(String name, Scope scope, IO output);

  /**
   *  Puts given signal into the internal data structure.  A unique order
   *  number is assigned to the signal (index).
   *
   *  @param name
   *             an identifier of the signal which serves for referencing it
   *
   *  @param scope
   *             a scope under which the signal was defined
   *
   *  @param signal
   *             a signal object to store
   *
   *  @throws IllegalArgumentException
   *             if the name is empty string or a blank string
   *
   *  @throws NullPointerException
   *             if either of the parameters is <code>null</code> value
   */
  void putSignal(String name, Scope scope, Signal signal);

  /**
   * Starts new local scope, actual scope becomes parent of the new scope.
   */
  void startScope();

}
