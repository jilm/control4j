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
package cz.control4j.application.c4j;

import cz.control4j.application.Preprocessor;
import cz.control4j.application.Scope;
import cz.lidinsky.tools.CommonException;
import org.junit.After;
import org.junit.AfterClass;
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
public class C4j2ControlTest {

  public C4j2ControlTest() {
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
   * Test of translateModule method, of class C4j2Control.
   */
  @Test
  public void testTranslateModule() {
    System.out.println("translateModule");
    Module moduleDef = null;
    C4j2Control instance = null;
    instance.translateModule(moduleDef);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  class ConfigurableSource extends Configurable {}
  class ConfigurableDestination extends cz.control4j.application.Configurable {};

  /**
   * Test of translateConfiguration method, of class C4j2Control.
   */
  @Test
  public void testTranslateConfiguration() {
    System.out.println("translateConfiguration");
    Configurable source = new ConfigurableSource();
    Property prop1 = new Property().setKey("key1").setValue("val1");
    prop1.setDeclarationReference("ref1");
    source.put(prop1);
    cz.control4j.application.Configurable destination
        = new ConfigurableDestination();
    Scope localScope = Scope.getGlobal();
    C4j2Control instance = new C4j2Control(new Preprocessor());
    instance.translateConfiguration(source, destination, localScope);
    assertEquals("val1", destination.getValue("key1"));
    assertEquals("ref1", destination.getDeclarationReferenceText());
  }

  /**
   * Test of isReference method, of class C4j2Control.
   */
  @Test
  public void testIsReference() {
    System.out.println("isReference");
    String value = "";
    String href = "ref1";
    C4j2Control instance = new C4j2Control(new Preprocessor());
    boolean expResult = true;
    boolean result = instance.isReference(value, href);
    assertEquals(expResult, result);
  }

  /**
   * Test of isReference method, of class C4j2Control.
   */
  @Test
  public void testIsReference2() {
    System.out.println("isReference");
    String value = "value";
    String href = "";
    C4j2Control instance = new C4j2Control(new Preprocessor());
    boolean expResult = false;
    boolean result = instance.isReference(value, href);
    assertEquals(expResult, result);
  }

  /**
   * Test of isReference method, of class C4j2Control.
   */
  @Test(expected=CommonException.class)
  public void testIsReference3() {
    System.out.println("isReference");
    String value = "value";
    String href = "ref";
    C4j2Control instance = new C4j2Control(new Preprocessor());
    boolean expResult = false;
    boolean result = instance.isReference(value, href);
    assertEquals(expResult, result);
  }

  /**
   * Test of isReference method, of class C4j2Control.
   */
  @Test(expected=CommonException.class)
  public void testIsReference4() {
    System.out.println("isReference");
    String value = null;
    String href = null;
    C4j2Control instance = new C4j2Control(new Preprocessor());
    boolean expResult = false;
    boolean result = instance.isReference(value, href);
    assertEquals(expResult, result);
  }

  /**
   * Test of resolveScope method, of class C4j2Control.
   */
  @Test
  @Ignore
  public void testResolveScope() {
    System.out.println("resolveScope");
    int scopeCode = 0;
    Scope localScope = null;
    C4j2Control instance = null;
    Scope expResult = null;
    Scope result = instance.resolveScope(scopeCode, localScope);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
