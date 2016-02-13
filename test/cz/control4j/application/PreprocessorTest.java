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
package cz.control4j.application;

import cz.control4j.Module;
import cz.lidinsky.tools.CommonException;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jilm
 */
public class PreprocessorTest {

  public PreprocessorTest() {
  }

  public class TestModule extends Module { }

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
   * Test of process method, of class Preprocessor.
   */
  @Test
  @Ignore
  public void testProcess() {
    System.out.println("process");
    Preprocessor instance = new Preprocessor();
    instance.process();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of add method, of class Preprocessor.
   */
  @Test
  @Ignore
  public void testAdd() {
    System.out.println("add");
    Module module = null;
    Preprocessor instance = new Preprocessor();
    instance.add(module);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getModuleInputs method, of class Preprocessor.
   */
  @Test
  @Ignore
  public void testGetModuleInputs() {
    System.out.println("getModuleInputs");
    Module module = null;
    Preprocessor instance = new Preprocessor();
    Collection<IO> expResult = null;
    Collection<IO> result = instance.getModuleInputs(module);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getInputMap method, of class Preprocessor.
   */
  @Test
  @Ignore
  public void testGetInputMap() {
    System.out.println("getInputMap");
    Collection<IO> inputs = null;
    Preprocessor instance = new Preprocessor();
    int[] expResult = null;
    int[] result = instance.getInputMap(inputs);
    assertArrayEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of putModuleOutput method, of class Preprocessor.
   */
  @Test
  @Ignore
  public void testPutModuleOutput() {
    System.out.println("putModuleOutput");
    String name = "";
    Scope scope = null;
    Connection connection = null;
    Preprocessor instance = new Preprocessor();
    instance.putModuleOutput(name, scope, connection);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of putModuleIntput method, of class Preprocessor.
   */
  @Test
  public void testPutModuleIntput() {
    System.out.println("putModuleIntput");
    String name = "input1";
    Scope scope = Scope.getGlobal();
    IO input = new IO(new TestModule(), "input1Key");
    Preprocessor instance = new Preprocessor();
    instance.putModuleIntput(name, scope, input);
  }

  /**
   * Test of processModuleInputs method, of class Preprocessor.
   */
  @Test
  public void testProcessModuleInputs() {
    System.out.println("processModuleInputs");
    Preprocessor instance = new Preprocessor();
    instance.putModuleOutput("sig1", Scope.getGlobal(), new Connection(new TestModule(), "key1"));
    instance.putModuleIntput("sig1", Scope.getGlobal(), new IO(new TestModule(), "key2"));
    instance.processModuleInputs();
  }

  /**
   * Test of processModuleInputs method, of class Preprocessor.
   */
  @Test(expected = CommonException.class)
  public void testProcessModuleInputs1() {
    System.out.println("processModuleInputs, not appropriate connection for the input");
    Preprocessor instance = new Preprocessor();
    instance.putModuleIntput("signal1", Scope.getGlobal(), new IO(new TestModule(), "key"));
    instance.processModuleInputs();
  }

}
