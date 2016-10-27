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
package cz.control4j;

import cz.control4j.application.Connection;
import cz.control4j.application.IO;
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds the application for execution. It takes a list of all of the modules
 * and connections and creates a list of module crates together with the io
 * maps.
 *
 * <p>Pass input data through the constructor and than call method get to
 * provides processing and return results.
 *
 * <p>Modules order is not changed.
 */
public class Builder {

  /**
   * The list of all of the modules.
   */
  private final Collection<Module> modules;

  /**
   * The list of all of the connections.
   */
  private final Collection<Connection> connections;

  private List<IOElement> inputIndex;
  private List<IOElement> outputIndex;

  /** Data structure to store results. */
  private List<ModuleCrate> crates;


  /**
   * Initialization.
   *
   * @param modules
   *            application modules
   *
   * @param connections
   *            connections between modules
   */
  public Builder(
      Collection<Module> modules, Collection<Connection> connections)
  {
    this.modules = modules;
    this.connections = connections;
  }

  /**
   * Provides processing and returns result.
   *
   * @return list of modules wrapped into the module crates
   */
  public List<ModuleCrate> get() {
    if (crates == null) {
      process();
    }
    return Collections.unmodifiableList(crates);
  }

  /**
   * Wrap modules into the module crates.
   */
  private void process() {

    crates = new ArrayList<>();
    createIOIndices();

    // group according to modules
    Map<Module, List<IOElement>> inputMaps
        = inputIndex.stream()
          .collect(Collectors.groupingBy(element -> element.module));

    Map<Module, List<IOElement>> outputMaps
        = outputIndex.stream()
          .collect(Collectors.groupingBy(element -> element.module));

    // for each module, create a module crate together with the io maps
    for (Module module : modules) {
      try {
        int[] inputMap
            = module instanceof InputModule || module instanceof ProcessModule
                ? createMap(inputMaps.get(module))
                : null;
        int[] outputMap
            = module instanceof OutputModule || module instanceof ProcessModule
                ? createMap(outputMaps.get(module))
                : null;
        ModuleCrate crate = ModuleCrate.create(module, inputMap, outputMap);
        crates.add(crate);
      } catch (Exception e) {
        throw new CommonException()
            .setCause(e)
            .set("message", "An exception while creating module IO maps!")
            .set("module class", module.getClass().getName());
      }
    }
  }

  /**
   * Creates io map based on the IOElement list for just one module.
   *
   * @param index
   *            TODO:
   *
   * @return an io map
   */
  private int[] createMap(List<IOElement> index) {

    if (index == null) {
      return new int[0];
    } else {
      int size = index.stream()
          .mapToInt(e -> e.moduleIndex)
          .max()
          .orElse(-1) + 1;
      int[] map = new int[size];
      Arrays.fill(map, -1);
      index.stream()
          .forEach(e -> map[e.moduleIndex] = e.signalPointer);
      return map;
    }
  }

  private void createIOIndices() {
    outputIndex = new ArrayList<>();
    inputIndex = new ArrayList<>();
    int pointer = 0;
    for (Connection connection : connections) {
      outputIndex.add(createOutputRecord(pointer, connection.getProducer()));
      for (IO input : connection.getConsumers()) {
        inputIndex.add(createInputRecord(pointer, input));
      }
      pointer++;
    }
  }

  private IOElement createOutputRecord(int pointer, IO output) {
    IOElement record = new IOElement();
    record.module = output.getModule();
    record.signalPointer = pointer;
    if (record.module instanceof OutputModule) {
      record.moduleIndex
          = ((OutputModule) record.module).getOutputIndex(output.getKey());
    } else if (record.module instanceof ProcessModule) {
      record.moduleIndex
          = ((ProcessModule) record.module).getOutputIndex(output.getKey());
    } else {
      throw new CommonException()
          .setCode(ExceptionCode.SYNTAX_ERROR)
          .set("message", "Output specified for an input module!")
          .set("module class", record.module.getClass().getName());
      // TODO: identification of the module and output
    }
    return record;
  }

  private IOElement createInputRecord(int pointer, IO input) {
    IOElement record = new IOElement();
    record.module = input.getModule();
    record.signalPointer = pointer;
    if (record.module instanceof InputModule) {
      record.moduleIndex
          = ((InputModule) record.module).getInputIndex(input);
    } else if (record.module instanceof ProcessModule) {
      record.moduleIndex
          = ((ProcessModule) record.module).getInputIndex(input.getKey());
    } else {
      throw new CommonException()
          .setCode(ExceptionCode.SYNTAX_ERROR)
          .set("message", "Output specified for an input module!")
          .set("module class", record.module.getClass().getName());
      // TODO: identification of the module and output
    }
    return record;
  }

  private static class IOElement {

    /**
     * The module that this io belongs to
     */
    Module module;

    int moduleIndex;

    int signalPointer;

  }

}
