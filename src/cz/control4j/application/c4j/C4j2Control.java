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
import cz.control4j.application.IO;
import cz.control4j.application.ModuleConfigurableAdapter;
import cz.control4j.application.ReferenceDecorator;
import cz.control4j.application.Scope;
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notNull;
import static cz.lidinsky.tools.text.StrUtils.isBlank;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * An adapter which can translate objects that belongs to this package into
 * the objects that can be processed by the Preprocessor. Preprocessor is
 * denoted as handler.
 *
 * <p>At first it is necessary to use put methods to insert all of the objects.
 * Then the process method must be called in order to translate the objects.
 */
public class C4j2Control extends AbstractAdapter {

  /**
   * A destination for translated objects.
   */
  protected cz.control4j.application.ApplicationHandler handler;

  /**
   * Internal data structures initialization.
   */
  public C4j2Control() {
    this.signals = new ArrayDeque<>();
    this.modules = new ArrayDeque<>();
    this.definitions = new ArrayDeque<>();
  }

  /**
   * Transform previously given classes into form which is acceptable for
   * the given handler. After this method is called, the object is empty.
   *
   * @param handler
   *            an object to send results
   */
  public void process(cz.control4j.application.ApplicationHandler handler) {

    this.handler = notNull(handler);

    // translate definitions
    while (!definitions.isEmpty()) {
      Define define = definitions.pop();
      handler.putDefinition(
        define.getName(),
        define.getScope(),
        define.getValue());
    }

    // translate all of the signals
    while (!signals.isEmpty()) {
      translateSignal(signals.pop());
    }

    // translate all of the modules
    while (!modules.isEmpty()) {
      translateModule(modules.pop());
    }
  }

  //---------------------------------- Methods inherited from abstract adapter.

  @Override
  public void startLevel() {
    handler.startScope();
  }

  @Override
  public void endLevel() {
    handler.endScope();
  }

  /** Modules. */
  private final Deque<Module> modules;

  @Override
  public void put(Module module) {
    modules.push(module);
  }

  @Override
  public void put(Block block) {
    // TODO:
    throw new UnsupportedOperationException();
  }

  /** Signals. */
  private final Deque<Signal> signals;

  @Override
  public void put(Signal signal) {
    signals.push(signal);
  }

  @Override
  public void put(ResourceDef resource) {
    // TODO:
    throw new UnsupportedOperationException();
  }

  /** Definitions. */
  private final Deque<Define> definitions;

  @Override
  public void put(Define define) {
    definitions.add(define);
  }

  @Override
  public void put(Property property) {
    // TODO:
    throw new UnsupportedOperationException();
  }

  @Override
  public void put(Use use) {
    // TODO:
    throw new UnsupportedOperationException();
  }

  /**
   * Translates given module definition into the real module that could be
   * executed by the runtime machine. The result objects are sent into the
   * handler object.
   *
   * @param moduleDef
   *            module definition object to be translated
   */
  protected void translateModule(Module moduleDef) {

    try {
      // The scope of the module
      Scope localScope = moduleDef.getScope();
      // create instance of the module
      cz.control4j.Module module
          = ModuleUtils.createModuleInstance(moduleDef.getClassName());
      // call initialize method
      module.initialize();
      // configure the module
      translateConfiguration(
          moduleDef, new ModuleConfigurableAdapter(module), localScope);
      // translate resource definitions
      //translateResources(module, destModule, localScope); // TODO:

      // initialization method before IO configuration
      module.beforeIOInitialization(
          moduleDef.getInput().size(), moduleDef.getOutput().size());
      // translate input
      moduleDef.getInput().stream()
          .forEach(in -> translateInput(in, module, localScope));

      // translate output
      moduleDef.getOutput().stream()
          .forEach(out -> translateOutput(out, module, localScope));

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
          .set("module class", moduleDef.getClassName());
    }
  }

  /**
   * Copy all of the property objects from the source object to the destination
   * object.
   *
   * @param source
   *            the source of configuration object
   *
   * @param destination
   *            the destination object for the properties
   *
   * @param localScope
   *            the scope level of source and destination objects
   *
   * @throws CommonException
   *            if something is wrong
   */
  protected void translateConfiguration(
      Configurable source,
      cz.control4j.application.Configurable destination,
      Scope localScope) {

    for (Property srcProperty : source.getConfiguration()) {

      String key = srcProperty.getKey();
      String value = srcProperty.getValue();
      String href = srcProperty.getHref();

      // detect not null key
      if (key == null) {
        throw new CommonException()
            .setCode(ExceptionCode.SYNTAX_ERROR)
            .set("message", "The key attribute of the property is missing!")
            .set("declaration reference", srcProperty.getDeclarationReferenceText());
      }

      // TODO: detect duplicate keys
      if (isReference(value, href)) {
        // If the property is the reference to some declaration
        ReferenceDecorator<cz.control4j.application.Configurable> reference
            = new ReferenceDecorator<>(
                href, srcProperty.getScope(), destination, key);
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
   * Translates one module input. It means, that new instance of IO class is
   * created and sent to the preprocessor.
   *
   * @param inputDef
   *
   * @param module
   *            the module whose input is translated
   *
   * @param localScope
   *            the scope of the module
   */
  protected void translateInput(
      Input inputDef,
      cz.control4j.Module module,
      Scope localScope) {

    try {
      String key = inputDef.getIndex();
      String href = inputDef.getHref();
      Scope scope = inputDef.getScope();
      IO input = new IO(module, key);
      input.setDeclarationReference(inputDef.getDeclarationReference());
      translateConfiguration(inputDef, input, localScope);
      this.handler.putModuleInput(href, scope, input);
    } catch (Exception e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "An exception was catched during the module input translation!");
    }
  }

  //private final StrBuffer errorMessage = new StrBuffer();
  //private boolean error = false;

  /**
   * Desides wheather the pair href and value is reference or if it contains the
   * value directly. If the href is not blank than returns true, otherwise
   * returns false.
   *
   * @param href a reference to some other object. It may be null or blank which
   * means, the this paire of values is not a reference
   *
   * @param value a value. It may be null or blank. If it is null, it means that
   * given paire of values doesn't denote value, but blank content may be valid
   * value, in such a case, content of the href parameter is significant.
   *
   * @return true if the href is not blank and the value is, returns false if
   * the href is blank
   *
   * @throws CommonException if both the value and href are not blank
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
   * Translate the signal definition and hand it over.
   *
   * @param signal signal definition to translate
   */
  public void translateSignal(Signal signal) {

    Scope localScope = signal.getDeclaredScope();
    cz.control4j.application.Signal translated
        = new cz.control4j.application.Signal(
            Integer.toHexString(localScope.hashCode()) + "@" + signal.getName());
    translateConfiguration(signal, translated, signal.getScope());
    // translate tags
//    for (Tag tag : signal.getTags()) {
//      cz.control4j.application.Tag translatedTag = new control4j.application.Tag();
//      translateConfiguration(tag, translatedTag, localScope);
//      translated.putTag(tag.getName(), translatedTag);
//    }
//    // TODO: other signal properties!
//    if (signal.isDefaultValueSpecified()) {
//      if (signal.isDefaultValueValid()) {
//        translated.setValueT_1(signal.getDefaultValue());
//      } else {
//        translated.setValueT_1Invalid();
//      }
//    }
    // put signal further
    handler.putSignal(signal.getName(), signal.getScope(), translated);
  }

  private void translateOutput(
      Output outputDef, cz.control4j.Module module, Scope localScope) {
    try {
      String key = outputDef.getIndex();
      String href = outputDef.getHref();
      Scope scope = outputDef.getScope();
      IO output = new IO(module, key);
      output.setDeclarationReference(outputDef.getDeclarationReference());
      translateConfiguration(outputDef, output, localScope);
      this.handler.putModuleOutput(href, scope, output);
    } catch (Exception e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "An exception was catched during the module output translation!");
    }
  }

}
