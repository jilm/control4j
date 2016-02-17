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

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ToStringBuilder;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.List;

/**
 *
 * Stands for a module element.
 *
 */
public class Module extends DescriptionBase {

  private String className;
  private List<Resource> resources;
  private List<Input> input;
  private List<Output> output;
  private List<String> inputTags;
  private List<String> outputTags;

  /**
   *  An empty constructor.
   */
  public Module() {
    resources = new ArrayList<>();
    input = new ArrayList<>();
    output = new ArrayList<>();
    inputTags = new ArrayList<>();
    outputTags = new ArrayList<>();
  }

  /**
   *  Creates an empty module object.
   *
   *  @param className a name of the class that implements functionality of a
   *             module
   */
  public Module(String className) {
    this();
    this.className = className;
  }

  /**
   *  Returns name of the class that implements the functionality of the
   *  module.
   *
   *  @return name of the class that implements the functionality of the
   *             module.
   *
   *  @throws CommonException
   *             if the class name is eather null or empty
   */
  public String getClassName() {
    return className;
  }

  Module setClassName(String className) {
    this.className = className;
    return this;
  }


  void add(Resource resource) {
    if (resource != null) {
      resources.add(resource);
    }
  }

  public Collection<Resource> getResources() {
    return unmodifiableCollection(resources);
  }


  void add(Input input) {
    if (input != null) {
      this.input.add(input);
    }
  }

  public Collection<Input> getInput() {
    return unmodifiableCollection(input);
  }


  void add(Output output) {
    if (output != null) {
      this.output.add(output);
    }
  }

  public Collection<Output> getOutput() {
    return unmodifiableCollection(output);
  }

  void addInputTag(String tag) {
    if (tag != null) {
      inputTags.add(tag);
    }
  }

  public Collection<String> getInputTags() {
    return unmodifiableCollection(inputTags);
  }


  void addOutputTag(String tag) {
    if (tag != null) {
      outputTags.add(tag);
    }
  }

  public Collection<String> getOutputTags() {
    return unmodifiableCollection(outputTags);
  }

  @Override
    public void toString(ToStringBuilder builder) {
      super.toString(builder);
      builder.append("className", className)
        //.append("resources", resources)
        .append("input", input)
        .append("output", output)
        .append("inputTags", inputTags)
        .append("outputTags", outputTags);
    }

}
