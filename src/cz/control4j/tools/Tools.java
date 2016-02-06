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

package cz.control4j.tools;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Tools
{

  public static void close(java.io.Closeable object)
  {
    if (object != null)
      try
      {
	object.close();
      }
      catch (java.io.IOException e)
      {
      }
  }

  public static void close(
      java.io.Closeable object, String sourceClass, String methodName)
  {
    if (object != null)
      try
      {
	object.close();
      }
      catch (java.io.IOException e)
      {
        catched(sourceClass, methodName, e);
      }
  }

  public static void sleep(long millis)
  {
    try
    {
      Thread.sleep(millis);
    }
    catch (InterruptedException e)
    {
    }
  }

  public static void catched(String logger, String method, Throwable exception) {
    Logger.getLogger(logger).log(Level.WARNING, method, exception.toString());
  }

}
