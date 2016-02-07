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
package cz.control4j;

import cz.lidinsky.tools.CommonException;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jilm
 */
public class SorterTest {

  public class TestModule extends Module { }

  public SorterTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }


  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {
  }

  /**
   * Test of addVertex method, of class Sorter.
   */
  @Test
  public void testAddVertex() {
    System.out.println("addVertex");
    Module module = new TestModule();
    Sorter instance = new Sorter();
    instance.addVertex(module);
    Module result = instance.iterator().next();
    assertEquals(result, module);
  }

  /**
   * Test of addAll method, of class Sorter.
   */
  @Test
  @Ignore
  public void testAddAll() {
    System.out.println("addAll");
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of addEdge method, of class Sorter.
   */
  @Test
  public void testAddEdge() {
    System.out.println("addEdge");
    Sorter sorter = new Sorter();
    Module moduleSrc = new TestModule();
    Module moduleDest = new TestModule();
    sorter.addVertex(moduleDest);
    sorter.addVertex(moduleSrc);
    sorter.addEdge(moduleSrc, moduleDest);
    Iterator<Module> iterator = sorter.iterator();
    Module resultModuleSrc = iterator.next();
    Module resultModuleDest = iterator.next();
    assertEquals(moduleSrc, resultModuleSrc);
    assertEquals(moduleDest, resultModuleDest);
  }

  /**
   * Test of iterator method, of class Sorter.
   */
  @Test(expected=CommonException.class)
   public void testIterator() {
    System.out.println("iterator");
    System.out.println("cycle detection");

    Sorter sorter = new Sorter();
    Module module1 = new TestModule();
    Module module2 = new TestModule();
    sorter.addVertex(module1);
    sorter.addVertex(module2);
    sorter.addEdge(module1, module2);
    sorter.addEdge(module2, module1);
    Iterator<Module> iterator = sorter.iterator();
  }

}
