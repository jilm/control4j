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

import cz.lidinsky.tools.ToStringBuilder;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.List;

/**
 *
 *  Represents a use element.
 *
 */
public class Use extends Configurable implements IReference {

  public Use() {
    this.input = new ArrayList<>();
    this.output = new ArrayList<>();
  }

  private String href;

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public void setHref(String href) {
      this.href = href;
  }

  private int scope;

  @Override
  public int getScope() {
    return scope;
  }

  @Override
  public void setScope(int scope) {
    this.scope = scope;
  }

  private final List<Input> input;

  void add(Input input) {
    if (input != null) {
      this.input.add(input);
    }
  }

  public Collection<Input> getInput() {
    return unmodifiableCollection(input);
  }

  private final List<Output> output;

  void add(Output output) {
    if (output != null) {
      this.output.add(output);
    }
  }

  public Collection<Output> getOutput() {
    return unmodifiableCollection(output);
  }

  @Override
  public void toString(ToStringBuilder builder) {
    super.toString(builder);
    builder.append("href", href)
        .append("scope", scope)
        .append("input", input)
        .append("output", output);
  }

}
