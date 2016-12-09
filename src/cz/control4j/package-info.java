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

/**
 * Runtime engine classes, this is the package with core classes of the
 * application.
 *
 * <p>The aim of this program is to provide a tool for technology or process
 * control by a computer. However there could be different use cases. The
 * program consists of a runtime environment (engine) that interprets the 
 * application. The application determine the logic (behaviour) and is 
 * written as a simple text, or a xml.
 *
 * <p>The building block of the application is a module. Module is a
 * mean to provide a data processing. Typicaly the module get input data,
 * provides some processing and put data for furhter processing. The
 * {@link cz.control4j.Signal} class seves as a datatype for module input 
 * and output. Besides processing modules there may be purely input or
 * output modules. Input modules only takes input and do not provide any
 * output back into the runtime environment. These modules mostly encapsulate
 * output into the technology, or output for the HMI interface. Output 
 * modules in turn provide only output. They mainly get input from technology
 * into the runtime environment for later processing. Process modules
 * have both input and output. These are modules that process data according
 * to some algorithm and provide processed data back to the runtime environment.
 * For more about the modules see {@link cz.control4j.Module}
 *
 * <p>The runtime environment is highly inspired by
 * <a href="https://en.wikipedia.org/wiki/Programmable_logic_controller">
 * Programmable Logic Controllers</a> (PLCs). Equally as PLCs it is working in
 * scans of constant length. The output modules are evaluated at the start of
 * the scan to gather the outcomes of the technology. Then the process modules
 * are evaluated. The process module is executed when all of its input data are
 * available. This condition excludes the presence of cycles between modules.
 * Finally, the input modules are executed to provide output back into the
 * technology. If evaluation of the entire application is finished before the
 * entered period of the cycle, the runtime environment is suspended for the rest
 * of the scan.
 *
 * <p>The entry point of the program is the class {@link cz.control4j.Control}.
 * It expects a name of the file with the application description as the commad
 * line argument. So the program may be executed as follows:
 *
 * <p><code>java cz.control4j.Control app.conf</code>
 *
 * <h2>After the control4j is run</h2>
 *
 * <p>The process after the {@link cz.control4j.Control} has been run, may be devided
 * into two phases.
 * <ol>
 *   <li>Application (project) loading: the application logic is read from the
 *       given files. Instances of all of the modules and objects are created
 *       and configured. Modules are ordered and the whole project is checked.
 *       For more about the application loading and building see
 *       {@link cz.control4j.application}
 *   <li>The control loop is run: the runtime engine which regularily evaluate
 *       the application is started. For more about the runtime engine, see
 *       {@link cz.control4j.ControlLoop}
 * </ol>
 *
 * <h3>Project loading</h3>
 *
 * TODO:
 *
 * <p>Even the project loging phase consists of several steps.
 * <ol>
 *  <li>First of all, the instance of {@link cz.control4j.ControlLoop} class
 *     is created, but not run yet. It is becouse during the project loading
 *     it is neccessary to configure the control loop object.
 * <li>The project file is loaded. For more information.
 * <li>All of the files which are referenced from the project file are loaded.
 *     Loaders can be found under the  package.
 * <li>Than project is built. It means that instances of modules and resources
 *     are created and everything is interconnected and sorted. For details
 *     .
 * <li>And finally, the consistency of the whole project is checked.
 *     .
 * </ol>
 *
 * <h3>Control loop</h3>
 *
 * <h2>Units</h2>
 *
 * <p>If the application works with data and signals that are measured somewhere
 * in the technology and the data represents temperature, preasure, flow, etc.,
 * it is typically necessary to handle units of these signals. Therefore the
 * basic datatype the {@link cz.control4j.Signal} class has a unit property attached.
 * The unit property may be set in two ways. If the module, which is the source
 * of some signal, knows the unit, then it is responsible for setting the unit
 * property of the output signal. If some module reads data from AD converter,
 * for example, which measure electric current in mA, then the module should set
 * the unit of output signal to mA. Sometimes the module is not able to determine
 * the unit of output signal. If you multiply the input signal by a constant, for
 * instance, the unit may change, but the only developer of the application is
 * able to derive how. That is why, there is a second option for unit settings.
 * The unit may be stated in the application description in the signal
 * declaration. In cases when the unit is set even by the module and
 * simoultaneously it is mentioned in the declaration of the signal, the unit
 * set by the module is prefered.
 *
 */
package cz.control4j;
