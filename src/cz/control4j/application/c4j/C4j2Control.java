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

import static cz.lidinsky.tools.Validate.notNull;

/**
 *  An adapter which can translate objects from nativelang package
 *  to the objects from the application package. Translated objects
 *  are sent into the given handler object.
 */
public class C4j2Control {

  /**
   *  A destination for translated objects.
   */
  protected cz.control4j.application.Preprocessor handler;

  /**
   *  Initialization.
   *
   *  @param handler
   *             the destination of translated objects
   */
  public C4j2Control(cz.control4j.application.Preprocessor handler) {
    this.handler = notNull(handler);
  }
}
