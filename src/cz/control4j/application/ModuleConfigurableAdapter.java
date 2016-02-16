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

package cz.control4j.application;

import cz.control4j.Module;
import static cz.lidinsky.tools.Validate.notNull;

/**
 * Provides module instance with configurable interface.
 */
public class ModuleConfigurableAdapter extends Configurable {

  private final Module module;

  public ModuleConfigurableAdapter(Module module) {
    this.module = notNull(module);
  }

  @Override
  public boolean containsKey(String key) {
    return false;
  }

  @Override
  public Property putProperty(String key, Property property) {
    module.set(key, property.getValue());
    return property;
  }

  @Override
  public String getValue(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Property getProperty(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putConfiguration(Configurable source) {
    throw new UnsupportedOperationException();
  }

}
