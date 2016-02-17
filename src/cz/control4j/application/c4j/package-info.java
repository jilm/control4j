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
 * <p>There are objects for loading an application which is written in the
 * c4j language in this package. There is a complete datastructure needed
 * to store the application. The objects provides only small integrity checks.
 * It is up to the higher level of the processing to check the data consistency.
 *
 * <p>All of the objects are prepared to hold the so called declaration
 * reference. This kind of information should be in the human readable
 * form and should be sufficient to identify such objected whenever
 * necessary.
 *
 * <p>The object XMLHandler is dedicated to load the data structure from the
 * XML document.
 */
package cz.control4j.application.c4j;
