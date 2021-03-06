/*
 *  Copyright 2013, 2016 Jiri Lidinsky
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
 *  It is thrown by a module which encounter such a problem, during the runtime
 *  phase, that prevent not only the module processing finishing but the whole
 *  scan should be discarded.
 */
public class RuntimeException extends Exception {

  public RuntimeException(String message) {
    super(message);
  }

  public RuntimeException() {
    super();
  }

}
