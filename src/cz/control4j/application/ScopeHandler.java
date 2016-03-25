/*
 *  Copyright 2016 Jiri Lidinsky
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

package cz.control4j.application;

import cz.control4j.Module;
import static cz.lidinsky.tools.Validate.notNull;
import cz.lidinsky.tools.text.StrBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 */
public class ScopeHandler {

  /** Holds actual scope during the process of translation. */
  private Scope scopePointer = Scope.getGlobal();

  /**
   * Returns actual local scope.
   *
   * @return actual local scope
   */
  public Scope getScopePointer() {
    return scopePointer;
  }

  /**
   * Starts new local scope, actual scope becomes parent of the new scope.
   */
  public void startScope() {
    scopePointer = new Scope(scopePointer);
  }

  /**
   * Ends current local scope and returns to its parent scope.
   */
  public void endScope() {
    scopePointer = scopePointer.getParent();
  }

}
