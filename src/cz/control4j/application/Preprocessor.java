/*
 *  Copyright 2016 Jiri Lidinsky
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Prepare application for execution.
 */
public class Preprocessor {

  public Preprocessor() {
    connections = new ScopeMap<>();
    modules = new ArrayList<>();
    inputs = new ArrayDeque<>();
  }

  public void process() {

  }

  //------------------------------------------------------------------ Modules.

  private final List<Module> modules;

  /**
   * Adds a module for further processing.
   *
   * @param module
   *            a module to add
   *
   * @throws CommonException
   *             if the parameter is null
   */
  public void add(Module module) {
    modules.add(notNull(module));
  }

  //--------------------------------------------------------------- Module IOs.

  private final ScopeMap<Connection> connections;

  /**
   * Returns collection of all of the inputs that belongs to the given module.
   */
  Collection<IO> getModuleInputs(Module module) {
    return connections.values().stream()
      .flatMap(connection -> connection.getConsumers().stream())
      .filter(input -> input.getModule() == module)
      .collect(Collectors.toSet());
  }

  /**
   * TODO:
   *
   * @param inputs
   * @return
   */
  int[] getInputMap(Collection<IO> inputs) {
    int size = inputs.size();
    int[] indices = new int[size];
    int[] pointers = new int[size];
    int i = 0;
    int maxIndex = 0;
    for (IO input : inputs) {
      String key = input.getKey();
      //indices[i] = input.getModule().getInputIndex(key);
      maxIndex = Math.max(maxIndex, indices[i]);
      pointers[i] = input.getPointer();
      i++;
    }
    int[] map = new int[maxIndex + 1];
    Arrays.fill(map, -1);
    for (i = 0; i < size; i++) {
      map[indices[i]] = pointers[i];
    }
    return map;
  }

  /**
   * Adds module output for further processing.
   *
   * @param name
   *            name of the signal
   *
   * @param scope
   *            scope from where the signal should be accessed
   *
   * @param connection
   *            an output to add
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
  public void putModuleOutput(String name, Scope scope, Connection connection) {
    connections.put(name, scope, connection);
  }

  private final Deque<ReferenceDecorator<IO>> inputs;

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
  public void putModuleIntput(String name, Scope scope, IO input) {
    inputs.add(new ReferenceDecorator<>(name, scope, input));
  }

  /**
   * @throws CommonException
   *            if there is not appropriate connection (output) for some input
   */
  protected void processModuleInputs() {
    while (!inputs.isEmpty()) {
      ReferenceDecorator<IO> reference = inputs.pop();
      // find appropriate connection
      Connection connection = connections.get(
          reference.getHref(), reference.getScope());
      // insert input into it
      connection.addConsumer(reference.getDecorated());
    }
  }


}
