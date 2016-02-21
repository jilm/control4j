/*
 *  Copyright 2015 Jiri Lidinsky
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

public class PrintAdapter extends AbstractAdapter
{

  public void startLevel()
  {
    System.out.println("Start level");
  }

  public void endLevel()
  {
    System.out.println("End level");
  }

  public void put(Module module)
  {
    System.out.println(module.toString());
  }

  public void put(Block block)
  {
    System.out.println(block.toString());
  }

  public void put(Signal signal)
  {
    System.out.println(signal.toString());
  }

  public void put(ResourceDef resource)
  {
    System.out.println(resource.toString());
  }

  public void put(Define define)
  {
    System.out.println(define.toString());
  }

  public void put(Property property)
  {
    System.out.println(property.toString());
  }

  public void put(Use use)
  {
    System.out.println(use.toString());
  }

  public static void main(String[] args) throws Exception {
    try {
    //String filename = args[0];
    String filename = "C:\\Users\\jilm\\Documents\\projects\\new_heating\\heating.xml";
    cz.lidinsky.tools.xml.XMLReader reader
        = new cz.lidinsky.tools.xml.XMLReader();
    XMLHandler handler = new XMLHandler();
    handler.setDestination(new PrintAdapter());
    reader.addHandler(handler);
    reader.load(new java.io.File(filename));
    } catch (Exception e) {
      System.out.println("*****");
      System.out.println(e.toString());
      throw e;
    }
  }

}
