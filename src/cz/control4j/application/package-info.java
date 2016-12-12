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
 * <p>There are objects for loading and preprocessing the application logic
 * before the runtime engine is started. There is a complete object model 
 * needed to store the application under this package.
 * 
 * <p>Preprocessor {@link cz.control4j.application.Preprocessor} is the
 * first class which starts to prepare the application for execution.
 * It simply resolves references. So, all of the properties which were
 * specified indirectly through the define objects, got the direct value.
 * 
 * <p>The second step is to put modules in order. A module may be executed
 * after all of its input is known. So, all of the modules which provide
 * that input must be executed first. Such ordering is performed by the
 * {@link cz.control4j.Sorter} class.
 * 
 * <p>Finally the {@link cz.control4j.Builder} is used to encapsulate module
 * instances into objects which arrange information transfere between the
 * runtime engine and the module.
 *
 */
package cz.control4j.application;
