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

import static cz.lidinsky.tools.text.StrUtils.isBlank;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.List;

/**
 *
 *  Stands for a block element. The block is a definition object. It contains
 *  modules, signal definitions, references to other blocks, input and output
 *  objects. Each block is identified by a name. Each block could be referenced
 *  by a use object.
 *
 */
public class Block extends DescriptionBase implements IDefinition {

  /** An empty constructor. */
  public Block() {
    this.input = new ArrayList<>();
    this.output = new ArrayList<>();
    this.modules = new ArrayList<>();
    this.signals = new ArrayList<>();
    this.uses = new ArrayList<>();
  }

  /** Identification of the block. */
  private String name;

  /** Returns a name, which is identification of the block. */
  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  private int scope;

  @Override
  public int getScope() {
    return scope;
  }

  @Override
  public void setScope(final int scope) {
    this.scope = scope;
  }

  private final List<String> input;

  public void addInput(String name) {
      if (!isBlank(name)) {
          input.add(name);
      }
  }

  public Collection<String> getInput() {
    return unmodifiableCollection(input);
  }

  private final List<String> output;

  public void addOutput(String name) {
      if (!isBlank(name)) {
          output.add(name);
      }
  }

  public Collection<String> getOutput() {
    return unmodifiableCollection(output);
  }

  private final List<Module> modules;

  public void add(Module module) {
      if (module != null) {
          modules.add(module);
      }
  }

  public Collection<Module> getModules() {
    return unmodifiableCollection(modules);
  }

  private final List<Signal> signals;

  public void add(Signal signal) {
      if (signal != null) {
          signals.add(signal);
      }
  }

  public Collection<Signal> getSignals() {
    return unmodifiableCollection(signals);
  }

  private final List<Use> uses;

  public void add(Use use) {
      if (use != null) {
          uses.add(use);
      }
  }

  public Collection<Use> getUses() {
    return unmodifiableCollection(uses);
  }

//
//  /**
//   *  A class that contain code to resolve the scope and expand inner
//   *  elements of the block into the handler.
//   */
//  private class ExpandableBlock extends cz.control4j.application.Block {
//
//    @Override
//    public void expand(
//        control4j.application.Use use,
//        Preprocessor handler) {
//
//      C4jToControlAdapter translator = new C4jToControlAdapter(handler);
//
//      // Expand all of the signal definitions
//      for (Signal signal : getSignals()) {
//        translator.put(signal);
//      }
//
//      // Expand all of the modules
//      for (Module module : getModules()) {
//        translator.put(module, use, Block.this);
//      }
//
//      // Expand all of the nested use objects
//      for (Use nestedUse : getUses()) {
//        translator.put(nestedUse);
//      }
//
//    }
//
//  }
//
//  /**
//   *
//   */
//  public control4j.application.Block getExpandable() {
//    return new ExpandableBlock();
//  }

}
