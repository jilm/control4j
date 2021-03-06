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
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;
import cz.lidinsky.tools.text.StrBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves all of the references. It means:
 * - properties that are given in the form (key, href) pair are replaced by
 *   (key, value) form.
 * - Resolves connections between modules which are by nature references to
 *   the signal objects.
 */
public class Preprocessor implements ApplicationHandler {

  /**
   * Initialize internal structures.
   */
  public Preprocessor() {
    this.outputs = new ArrayDeque<>();
    this.modules = new ArrayList<>();
    this.inputs = new ArrayDeque<>();
    this.signals = new ScopeMap<>();
    this.definitions = new ScopeMap<>();
    this.references = new ArrayDeque<>();
  }

  /**
   * Process given data. This method should be called after all of the data
   * necessary to process.
   */
  public void process() {

    // resolve all of the property references
    while (!references.isEmpty()) {
      // take a reference
      ReferenceDecorator<Configurable> reference = references.pop();
      // find referenced definition
      ValueObject definition = definitions.get(
          reference.getHref(), reference.getScope());
      // TODO: what if there is no appropriate definition
      // create new property
      Property property = new Property(definition.getValue());
      // configure the object
      reference.getDecorated().putProperty(reference.getKey(), property);
    }

    // process module IOs
    processModuleIOs();
  }

  //------------------------------------------------------------ Scope pointer.

  /** Holds actual scope during the process of translation. */
  private Scope scopePointer = Scope.getGlobal();

  /**
   * Returns actual local scope.
   *
   * @return actual local scope
   */
  public Scope getScopePointer() {
    return scopePointer;
  }

  /**
   * Starts new local scope, actual scope becomes parent of the new scope.
   */
  @Override
  public void startScope() {
    scopePointer = new Scope(scopePointer);
  }

  /**
   * Ends current local scope and returns to its parent scope.
   */
  @Override
  public void endScope() {
    scopePointer = scopePointer.getParent();
  }

  //--------------------------------------------------------------- Properties.



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
  @Override
  public void add(Module module) {
    modules.add(notNull(module));
  }

  /**
   * Returns a list of processed modules
   *
   * @return unmodifiable list of all of the modules
   */
  public List<Module> getModules() {
    return Collections.unmodifiableList(modules);
  }

  //--------------------------------------------------------------- Module IOs.

  private final Deque<ReferenceDecorator<IO>> outputs;
  private final Deque<ReferenceDecorator<IO>> inputs;

  /**
   * TODO:
   *
   * @param inputs
   * @return
   */
//  int[] getInputMap(Collection<IO> inputs) {
//    int size = inputs.size();
//    int[] indices = new int[size];
//    int[] pointers = new int[size];
//    int i = 0;
//    int maxIndex = 0;
//    for (IO input : inputs) {
//      String key = input.getKey();
//      //indices[i] = input.getModule().getInputIndex(key);
//      maxIndex = Math.max(maxIndex, indices[i]);
//      pointers[i] = input.getPointer();
//      i++;
//    }
//    int[] map = new int[maxIndex + 1];
//    Arrays.fill(map, -1);
//    for (i = 0; i < size; i++) {
//      map[indices[i]] = pointers[i];
//    }
//    return map;
//  }

  /** A data structure for name and scope signal look up. */
  private final ScopeMap<Signal> signals;

  /**
   *
   * @return application signals
   */
  public ScopeMap<Signal> getSignals() {
    return signals;
  }

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
  @Override
  public void putSignal(String name, Scope scope, Signal signal) {
    try {
      signals.put(name, scope, signal);
    } catch (Exception e) {
      throw new CommonException()
        .setCause(e)
        .setCode(ExceptionCode.SYNTAX_ERROR)
        .set("message", "Signal definition is not correct!")
        .set("name", name)
        .set("scope", scope.toString())
        .set("signal", signal.toString());
    }
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
   * @param output
   *            an output for processing
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
  @Override
  public void putModuleOutput(String name, Scope scope, IO output) {
    ReferenceDecorator<IO> decorator
        = new ReferenceDecorator<>(name, scope, output, output.getKey());
    decorator.setDeclarationReference(output.getDeclarationReference());
    outputs.push(new ReferenceDecorator<>(name, scope, output, output.getKey()));
  }


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
  @Override
  public void putModuleInput(String name, Scope scope, IO input) {
    inputs.push(new ReferenceDecorator<>(name, scope, input, input.getKey()));
  }

  private final Deque<ReferenceDecorator<Configurable>> references;

  @Override
  public void addPropertyReference(ReferenceDecorator<Configurable> reference) {
    references.add(reference);
  }

  private final ScopeMap<ValueObject> definitions;

  @Override
  public void putDefinition(String name, Scope scope, String value) {
    definitions.put(name, scope, new ValueObject(value));
  }

  @Override
  public void addModuleInput(ReferenceDecorator<Module> reference) {
    throw new UnsupportedOperationException();
  }

  private List<Connection> connections;

  /**
   * Returns processed connections between modules.
   *
   * @return connections between modules
   */
  public List<Connection> getConnections() {
    return Collections.unmodifiableList(connections);
  }

  /**
   * It takes all of the given IO, group it according to the signal and creates
   * a connection objects for each group of IO pointing to the same signal.
   * The result is stored into the connections map.
   */
  private void processModuleIOs() {

    Map<Signal, ConnectionCrate> connectionCrates = new HashMap<>();

    // process all of the outputs
    while (!outputs.isEmpty()) {
      // get an output to process
      ReferenceDecorator<IO> output = outputs.pop();
      // find appropriate signal object
      try {
        Signal signal = signals.get(output.getHref(), output.getScope());
        // store the output into the temporary data structure
        ConnectionCrate crate = getConnectionCrate(connectionCrates, signal);
        crate.outputs.add(output.getDecorated());
      } catch (CommonException e) {
        // there is no signal for some output
        throw new CommonException()
            .setCause(e)
            .setCode(ExceptionCode.SYNTAX_ERROR)
            .set("message", "Signal definition for an output is missing!")
            .set("name", output.getHref())
            .set("declaration reference", output.getDeclarationReferenceText());
      }
    }

    // process all of the inputs
    while (!inputs.isEmpty()) {
      // get an input
      ReferenceDecorator<IO> input = inputs.pop();
      // find appropriate signal object
      Signal signal = signals.get(input.getHref(), input.getScope());
      // store it into the temprary data structure
      ConnectionCrate crate = getConnectionCrate(connectionCrates, signal);
      crate.inputs.add(input.getDecorated());
    }

    // check that for all connection objects, there is at least one output per connection
    Set<Signal> outputless = connectionCrates.values().stream()
      .filter(c -> (c.outputs.isEmpty() && !c.inputs.isEmpty()))
      .map(c -> c.signal)
      .collect(Collectors.toSet());
    if (!outputless.isEmpty()) {
      throw new CommonException()
          .setCode(ExceptionCode.SYNTAX_ERROR)
          .set("message", "There is a signal, not connected to some output!")
          .set("signal", outputless.toString());
    }

    // Check that there is no connection with two or more outputs
    Set<Signal> manyoutputs = connectionCrates.values().stream()
      .filter(c -> (c.outputs.size() > 1))
      .map(c -> c.signal)
      .collect(Collectors.toSet());
    // TODO:

    // create final connection objects
    connections = connectionCrates.values().stream()
      .filter(c -> c.outputs.size() == 1)
      .filter(c -> !c.inputs.isEmpty())
      .map(
          c -> new Connection(
            c.outputs.iterator().next(),
            c.inputs))
      .collect(Collectors.toList());
  }

  private class ConnectionCrate {
    List<IO> inputs;
    List<IO> outputs;
    Signal signal;

    ConnectionCrate() {
      inputs = new ArrayList<>();
      outputs = new ArrayList<>();
    }
  }

  private ConnectionCrate getConnectionCrate(
      Map<Signal, ConnectionCrate> crates, Signal signal) {
    ConnectionCrate crate = crates.get(signal);
    if (crate == null) {
      crate = new ConnectionCrate();
      crate.signal = signal;
      crates.put(signal, crate);
    }
    return crate;
  }

  private final StrBuffer errors = new StrBuffer();


}
