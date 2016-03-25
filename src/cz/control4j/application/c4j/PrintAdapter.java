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

import cz.control4j.application.ScopeHandler;
import java.util.ArrayList;
import java.util.List;

public class PrintAdapter extends AbstractAdapter
{

  public PrintAdapter() {
    objects = new ArrayList<>();
  }

  private final List objects;

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
    objects.add(module);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(module))));
  }

  public void put(Block block)
  {
    objects.add(block);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(block))));
  }

  public void put(Signal signal)
  {
    objects.add(signal);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(signal))));
  }

  public void put(ResourceDef resource)
  {
    objects.add(resource);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(resource))));
  }

  public void put(Define define)
  {
    objects.add(define);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(define))));
  }

  public void put(Property property)
  {
    objects.add(property);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(property))));
  }

  public void put(Use use)
  {
    objects.add(use);
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(use))));
  }

  public static void main(String[] args) throws Exception {
    try {
    //String filename = args[0];
    String filename = "C:\\Users\\jilm\\Documents\\projects\\new_heating\\heating.xml";
    cz.lidinsky.tools.xml.XMLReader reader
        = new cz.lidinsky.tools.xml.XMLReader();
    XMLHandler handler = new XMLHandler(new ScopeHandler());
    PrintAdapter instance = new PrintAdapter();
    handler.setDestination(instance);
    reader.addHandler(handler);
    reader.load(new java.io.File(filename));
    System.out.println(
        new cz.lidinsky.tools.text.Formatter().format(
            new cz.lidinsky.tools.text.StrIterator(
                new cz.lidinsky.tools.text.Object2String().toStrBuffer(instance.objects))));
    } catch (Exception e) {
      System.out.println("*****");
      System.out.println(e.toString());
      throw e;
    }
  }

}
