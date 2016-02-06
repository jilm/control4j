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

package cz.control4j;

/**
 *  Module is one of the main building blocks of the application. Modules reads
 *  data from technology, provides them for the further processing, performs
 *  processing, and writes results back to the technology.
 *
 *  <p>Modules are connected one to another in order to exchange data. There
 *  are input and output terminals for making the connection. A module may be
 *  just output module, input module or process module. This depends on
 *  wheather the module provides output, accepts input, or supports boths.
 *
 *  <p>To implement some functionality, a module have to override one of the
 *  {@link InputModule}, {@link ProcessModule} or {@link OutputModule} classes.
 *
 *  <p>Number of input signals (terminals) and input terminal meaning depends
 *  entirely on the module implementation. Each module gets all of its input
 *  in one array, during the runtime, so each input terminal has a zero-based
 *  index to distinguish it. Because the number of input terminals doesn't
 *  have to be known during the module creation, and moreover there may be
 *  modules for which the order of input signals is meaningless, the input
 *  terminals must be organized inside the input array as follows:
 *  <ol>
 *
 *    <li>Indices from zero to some <em>n</em> are dedicated to terminals
 *    which are mandatory. Elements inside the input array may not contain
 *    <code>null</code> value.  In other words, these input terminals must be
 *    connected.  Concrete module implementation must eiter override a method
 *    <code>getMandatoryInputSize</code> to return the proper number of
 *    mandatory input terminals or the <code>AMinInput</code> annotation my
 *    be used to achive the correct functionality.
 *
 *    <li>Indexes from <em>n</em> to some <em>m</em> are used by terminals
 *    which are optional. Moreover, the <em>m</em> may not be known by the
 *    time of module creation, it means, that in such a case, it depends
 *    entirely on the application definition, how many input signals will be
 *    connected. Nevertheless the terminals which span into this category may
 *    left disconnected. In other words, the corresponding element in the
 *    input array may contain <code>null</code> value. Module implementation
 *    must either override the method <code>getIndexedInputSize</code> to
 *    return the proper number of totally supported indexed input terminals,
 *    or it may use <code>AMaxInput</code> for the same purpose.
 *
 *    <li>The third category of input is index-less input. If this kind of
 *    input is supported by the module, than the method
 *    <code>isVariableInputSupported</code> must return <code>true</code>.
 *    Same functionality may be achieved thanks to the
 *    <code>AVariableInput</code> annotation.  The only condition that must
 *    be satisfied is that number of input terminals that belongs to the
 *    previous two categoris is bounded. In such a case, index-less signals
 *    will ocupy input array indexes starting from <em>m</em>.
 *  </ol>
 *
 */
abstract class Module {

  //------------------------------------------------ Build-time initialization.

  /**
   * Called to initialize module immediately after the instance has been
   * created. If exception is thrown, the application stop excecution and
   * control loop will not start.
   */
  void initialize() {
  }

  //----------------------------------------------------------------- Run-time.

  /**
   * Method that is called once immediately before the control loop runs, it is
   * used for module internal status variable initialization.  It is called
   * before the control loop starts or if it is neccessary to restart the
   * control loop after the crash. The default implementation does nothing.
   * TODO: what if the exception is thrown? Should it stop the
   * execution?
   */
  public void prepare() { }

}
