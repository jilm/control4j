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

import cz.control4j.ModuleUtils;
import cz.control4j.application.ModuleConfigurableAdapter;
import cz.control4j.application.ReferenceDecorator;
import cz.control4j.application.Scope;
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;
import static cz.lidinsky.tools.text.StrUtils.isBlank;

/**
 *  An adapter which can translate objects that belongs to this package into
 *  the objects that can be processed by the Preprocessor. Preprocessor is
 *  denoted as a handler.
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

  /**
   * Translates given module definition into the real module that could be
   * executed by the runtime machine. The result objects are sent into the
   * preprocessor object.
   *
   * @param moduleDef
   *            module definition object to be translated
   */
  protected void translateModule(Module moduleDef) {

    try {
      // The scope of the module
      Scope localScope = handler.getScopePointer();
      // create instance of the module
      cz.control4j.Module module
        = ModuleUtils.createModuleInstance(moduleDef.getClassName());
      // configure the module
      translateConfiguration(
          moduleDef, new ModuleConfigurableAdapter(module), localScope);
      // translate resource definitions
      //translateResources(module, destModule, localScope); // TODO:

      // translate input
      //translateInput(module, destModule, localScope);

      // translate output
      //translateOutput(module, destModule, localScope);

      // translate tagged input
      //for (String tag : module.getInputTags()) {
      //  handler.addInputTag(destModule, tag);
      //}

      // translate tagged output
      //for (String tag : module.getOutputTags()) {
      //  handler.addOutputTag(destModule, tag);
      //}

      // send translated module
      handler.add(module);
    } catch (Exception e) {
      throw new CommonException()
        .setCause(e)
        .set("message", "Couldn't create and configure some module!")
        .set("module definition", moduleDef.toString());
    }
  }


  /**
   *  Copy all of the property objects from the source object to the
   *  destination object.
   *
   *  @param source
   *             the source of configuration object
   *
   *  @param destination
   *             the destination object for the properties
   *
   *  @param localScope
   *             the scope level of source and destination objects
   *
   *  @throws CommonException
   *             if something is wrong
   */
  protected void translateConfiguration(
      Configurable source,
      cz.control4j.application.Configurable destination,
      Scope localScope) {

    for (Property srcProperty : source.getConfiguration()) {
      String key = srcProperty.getKey();
      String value = srcProperty.getValue();
      String href = srcProperty.getHref();
      // TODO: detect duplicate keys
      if (isReference(value, href)) {
        // If the property is the reference to some declaration
        ReferenceDecorator<cz.control4j.application.Configurable>
          reference = new ReferenceDecorator<>(
              href,
              resolveScope(srcProperty.getScope(), localScope),
              destination,
              key);
        reference.setDeclarationReference(
            srcProperty.getDeclarationReference());
        handler.addPropertyReference(reference);
      } else {
        // If the object is in the form of key and value
        cz.control4j.application.Property destProperty
          = new cz.control4j.application.Property(value);
        destProperty.setDeclarationReference(
            srcProperty.getDeclarationReference());
        destination.putProperty(key, destProperty);
      }
    }
  }

  /**
   * Desides wheather the pair href and value is reference or if it contains
   * the value directly. If the href is not blank than returns true, otherwise
   * returns false.
   *
   * @param href
   *            a reference to some other object. It may be null or blank which
   *            means, the this paire of values is not a reference
   *
   * @param value
   *            a value. It may be null or blank. If it is null, it means that
   *            given paire of values doesn't denote value, but blank content
   *            may be valid value, in such a case, content of the href
   *            parameter is significant.
   *
   * @return true if the href is not blank and the value is, returns false if
   *            the href is blank
   *
   * @throws CommonException
   *            if both the value and href are not blank
   */
  protected boolean isReference(String value, String href) {
    boolean notAReference = isBlank(href);
    boolean notAValue = value == null;
    if (notAReference && !notAValue) {
      return false;
    } else if (!notAReference && isBlank(value)) {
      return true;
    } else if (notAReference && notAValue) {
      // Neither value, nor href was specified for the property!
      throw new CommonException()
        .setCode(ExceptionCode.ILLEGAL_STATE)
        .set("message", "Neither value nor href attribute was "
            + "specified for some property!")
        .set("value", value)
        .set("href", href);
    } else {
      // Both, value and href were specified for the property!
      throw new CommonException()
        .setCode(ExceptionCode.ILLEGAL_STATE)
        .set("message", "Both value and href attributes were "
            + "specified for some property!")
        .set("value", value)
        .set("href", href);
    }
  }

  /**
   *
   * @param scopeCode
   * @param localScope
   * @return
   */
  protected Scope resolveScope(int scopeCode, Scope localScope) {
    switch (scopeCode) {
      case 0:
        return Scope.getGlobal();
      case 1:
        return localScope;
      case 2:
        return localScope.getParent();
      default:
        throw new IllegalArgumentException();
    }
  }

}
