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
import cz.lidinsky.tools.reflect.Setter;
import java.lang.reflect.Method;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jilm
 */
public class ModuleUtilsTest {

  public ModuleUtilsTest() {
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
   * Test of getInputIndex method, of class ModuleUtils.
   */
  @Test
  @Ignore
  public void testGetInputIndex() {
    System.out.println("getInputIndex");
    Class<? extends Module> moduleClass = null;
    String key = "";
    int expResult = 0;
    int result = ModuleUtils.getInputIndex(moduleClass, key);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  class TestModule2 extends Module {
    String strProperty;
    @Setter("strPropertyKey") void setStrProperty(String value) {
      this.strProperty = value;
    }
    int intProperty;
    @Setter("intPropertyKey") void setIntProperty(int value) {
      this.intProperty = value;
    }
  }

  /**
   * Test of setProperty method, of class ModuleUtils.
   */
  @Test
  public void testSetProperty() {
    System.out.println("setProperty");
    TestModule2 module = new TestModule2();
    String key = "strPropertyKey";
    String value = "strValue";
    ModuleUtils.setProperty(module, key, value);
    assertEquals(module.strProperty, value);
  }

  /**
   * Test of setProperty method, of class ModuleUtils.
   */
  @Test
  public void testSetProperty2() {
    System.out.println("setProperty");
    TestModule2 module = new TestModule2();
    String key = "intPropertyKey";
    String value = "951";
    ModuleUtils.setProperty(module, key, value);
    assertEquals(951, module.intProperty);
  }

  class Module1 extends Module {
    @Setter("key1") public void setProperty1(String value) {}
    @Setter("key2") public void setProperty2(String value) {}
    @Setter("key2") public void setProperty3(String value) {}
  }

  /**
   * Test of getSetterMethod method, of class ModuleUtils.
   */
  @Test
  public void testGetSetterMethod() throws Exception {
    System.out.println("getSetterMethod");
    Class _class = Module1.class;
    String key = "key1";
    Method expResult = Module1.class.getMethod("setProperty1", String.class);
    Method result = ModuleUtils.getSetterMethod(_class, key);
    assertEquals(expResult, result);
  }

  @Test(expected=CommonException.class)
  public void testGetSetterMethod1() throws Exception {
    try {
      System.out.println("getSetterMethod, Duplicate setter method");
      Class _class = Module1.class;
      String key = "key2";
      ModuleUtils.getSetterMethod(_class, key);
    } catch (Exception e) {
      System.out.println(e.toString());
      throw e;
    }
  }

  @Test(expected=CommonException.class)
  public void testGetSetterMethod2() throws Exception {
    try {
      System.out.println("getSetterMethod, not implemented key");
      Class _class = Module1.class;
      String key = "key3";
      ModuleUtils.getSetterMethod(_class, key);
    } catch (Exception e) {
      System.out.println(e.toString());
      throw e;
    }
  }

  public static class TestModuleInstance extends Module { }

  /**
   * Test of createModuleInstance method, of class ModuleUtils.
   */
  @Test
  public void testCreateModuleInstance() {
    System.out.println("createModuleInstance");
    System.out.println(TestModuleInstance.class.getName());
    String className = "cz.control4j.ModuleUtilsTest$TestModuleInstance";
    Module result = ModuleUtils.createModuleInstance(className);
    assertTrue(result instanceof TestModuleInstance);
  }

  @Test(expected=CommonException.class)
  public void testCreateModuleInstance1() {
    System.out.println("createModuleInstance");
    System.out.println(TestModuleInstance.class.getName());
    String className = "java.lang.Object";
    ModuleUtils.createModuleInstance(className);
  }

  @Test(expected=CommonException.class)
  public void testCreateModuleInstance2() {
    System.out.println("createModuleInstance");
    System.out.println(TestModuleInstance.class.getName());
    String className = null;
    ModuleUtils.createModuleInstance(className);
  }
}
