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

package cz.control4j;

import cz.lidinsky.tools.CommonException;
import static cz.lidinsky.tools.Validate.notNull;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 *
 *  Performs a toplogical sort of the modules.
 *
 *  <p>At first, use method add or addAll to add all of the application
 *  modules. Than use the iterator to get the topological order.
 *
 */
public class Sorter implements Iterable<Module> {

  /**
   *  Creates an empty object.
   */
  public Sorter() {
    this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    this.unresolvedVertices = new ArrayDeque<>();
  }

  //--------------------------------------------------------- Public Interface.

  /**
   *  Adds a module into the internal graph. All of the modules must be
   *  added to get the toplogical order. If the module is already present
   *  in the internal graph, nothing happens.
   *
   *  @param module
   *             a module to add into the internal graph
   * @return
   *
   *  @throws CommonException
   *             if the parameter contains <code>null</code> value
   *
   *  @throws CommonException
   *             with code <code>DUPLICATE_ELEMENT</code> if there already
   *             is a module with output into the same signal as this one
   */
  public Sorter add(Module module) {
    unresolvedVertices.add(notNull(module));
    return this;
  }

  /**
   *
   * @param source
   * @param destination
   * @return
   */
  public Sorter add(Module source, Module destination) {
    graph.addVertex(source);
    graph.addVertex(destination);
    graph.addEdge(source, destination);
    dirty = true;
    return this;
  }

  /**
   *  Adds all of the given modules.
   * @param modules
   * @return
   */
  public Sorter addAllVertices(Collection<Module> modules) {
    unresolvedVertices.addAll(modules);
    return this;
  }

  /**
   *  Returns the topological order iterator.
   *
   *  @throws CommonException
   *             with code <code>NO_SUCH_ELEMENT</code> if there is no
   *             source module for some input of some target module
   *
   *  @throws CommonException
   *             with code <code>CYCLIC_DEFINITION</code> if there is an
   *             unbreakable feedback within the graph
   */
  @Override
  public Iterator<Module> iterator() {
    // prepare graph
    process();
    // create iterator
    return new TopologicalOrderIterator<>(graph);
  }

  //--------------------------------------------------------------- Processing.

  /**
   *  Signalize that sice last processing, the change in the graph occured
   *  and that the new processing is necessary to get uptodate results.
   */
  private boolean dirty = false;

  private final Deque<Module> unresolvedVertices;

  /**
   *  Provides preprocessing before the results may be obtained.
   *
   *  @throws CommonException
   *             with code <code>NO_SUCH_ELEMENT</code> if there is no
   *             source module for some input of some target module
   *
   *  @throws CommonException
   *             with code <code>CYCLIC_DEFINITION</code> if there is an
   *             unbreakable feedback within the graph
   */
  protected void process() {
    breakFeedback();
    dirty = false;
  }

  private final DefaultDirectedGraph<Module, DefaultEdge> graph;

  private boolean isResolved() {
    return unresolvedVertices.isEmpty();
  }

  //------------------------------------------------------- Feedback Treatment.

  /**
   *  Breaks a feedback, cycle in the directed graph, which contain a
   *  particular vertex, module.
   *
   *  @throws CommonException
   *             with code <code>CYCLIC_DEFINITION</code> if there is an
   *             unbreakable feedback within the graph
   */
  private void breakFeedback() {

//    CycleDetector<Module, DefaultEdge> cycleDetector
//        = new CycleDetector<>(graph);
//    while (cycleDetector.detectCycles()) {
//      // find cycles
//      DirectedSimpleCycles<Module, DefaultEdge> cycleFinder
//        = new TarjanSimpleCycles<>(graph);
//      List<List<Module>> cycles = cycleFinder.findSimpleCycles();
//      // go through all of the cycles and break them
//      for (List<Module> cycle : cycles) {
//        for (Module srcModule : cycle) {
//          Collection<Module> destModules
//            = Graphs.successorListOf(graph, srcModule);
//          destModules = CollectionUtils.intersection(cycle, destModules);
//          for (Module destModule : destModules) {
//            if (hasEdgeDefaultValue(srcModule, destModule)) {
//              breakEdge(srcModule, destModule);
//              return;
//            }
//          }
//        }
//        // The cycle is not breakable
//        throw new CommonException()
//          .setCode(ExceptionCode.CYCLIC_DEFINITION)
//          .set("message", "The graph contains unbreakable cycle!");
//      }
//    }

  }

  /**
   *  Returns true if and only if all of the signals that are connected
   *  from source module to the target module has default values defined.
   *  If there is no such connection between the given modules, it returns
   *  false.
   */
//  private boolean hasEdgeDefaultValue(Module source, Module target) {
//    boolean connected = false; // if there is connection between modules
//    for (IO output : source.getOutput()) {
//      for (IO input : target.getInput()) {
//        if (output.isConnected() && input.isConnected()
//            && output.getPointer() == input.getPointer()) {
//          connected = true;
//          if (!input.getSignal().isValueT_1Specified()) {
//            return false;
//          }
//        }
//      }
//    }
//    return connected;
//  }

//  /**
//   *  Breaks the direct connection between two modules.
//   */
//  private void breakEdge(Module source, Module target) {
//    // remove the broken edge from the graph
//    graph.removeEdge(source, target);
//    for (IO output : source.getOutput()) {
//      for (IO input : target.getInput()) {
//        if (output.isConnected() && input.isConnected()
//            && output.getPointer() == input.getPointer()) {
//          // if the signal connects the given modules, break it
//          // Create a shared handover place.
//          MutableObject<control4j.Signal> sharedSignal
//            = new MutableObject<>();
//          sharedSignal.setValue(
//              input.getSignal().isValueT_1Valid()
//              ? control4j.Signal.getSignal(input.getSignal().getValueT_1())
//              : control4j.Signal.getSignal());
//          // Create new signal place for the source module
//          int brokenPointer = getSignalCount();
//          signalIndex[output.getPointer()] = null;
//          output.setPointer(brokenPointer);
//          setSourceModule(brokenPointer, source);
//          // Create new output module.
//          FeedbackModule outModule = new FeedbackModule(
//              FeedbackModule.OUTPUT_CLASSNAME, sharedSignal);
//          IO brokenOutput = new IO();
//          brokenOutput.setPointer(input.getPointer());
//          outModule.putOutput(0, brokenOutput);
//          add(outModule);
//          // Create new input module
//          FeedbackModule inModule = new FeedbackModule(
//              FeedbackModule.INPUT_CLASSNAME, sharedSignal);
//          IO brokenInput = new IO();
//          brokenInput.setPointer(brokenPointer);
//          brokenInput.setSignal(input.getSignal());
//          inModule.putInput(0, brokenInput);
//          add(inModule);
//          unresolvedVertices.add(target);
//        }
//      }
//    }
//  }

}
