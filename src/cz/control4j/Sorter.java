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
  public Sorter addVertex(Module module) {
    if (graph.addVertex(notNull(module))) {
      // mark the graph as dirty
      dirty = true;
    }
    return this;
  }

  /**
   *  Adds all of the given modules.
   * @param modules
   * @return
   */
  public Sorter addAll(Iterable<Module> modules) {
    for (Module module : modules) {
      addVertex(module);
    }
    return this;
  }

  public Sorter addEdge(Module source, Module destination) {
    graph.addEdge(source, destination);
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
    // create iterator
    return new TopologicalOrderIterator<>(graph);
  }

  //--------------------------------------------------------------- Processing.

  /**
   *  Signalize that sice last processing, the change in the graph occured
   *  and that the new processing is necessary to get uptodate results.
   */
  private boolean dirty = false;

  private final DefaultDirectedGraph<Module, DefaultEdge> graph;

}
