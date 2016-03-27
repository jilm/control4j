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
 *  Represents a module which provides only output for further processing.
 */
public abstract class OutputModule extends Module
{

  /**
   *  Method that provides output of the module for further prcessing.  This is
   *  the only method that must be overwritten by the module developer. This
   *  method will be called repeatedly by the execute, method during the
   *  processing phase.
   *
   *  @param output
   *             an array which serves for output transfer.
   *
   *  @param outputLength
   *             how may elements may be used for this method
   *
   *  @throws RuntimeException
   *             if something went wrong and the whole control loop should not
   *             be finished. But sometimes it is sufficient just to return
   *             invalid signals.
   *
   */
  protected abstract void get(Signal[] output, int outputLength)
      throws RuntimeException;

  public int getOutputIndex(String key) {
    return ModuleUtils.getOutputIndex(this.getClass(), key);
  }

  public int getOutputSize() {
    return ModuleUtils.getOutputSize(this.getClass());
  }

}
