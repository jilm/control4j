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

import cz.lidinsky.tools.IToStringBuildable;
import cz.lidinsky.tools.ToStringBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 *  Provides common interface for objects which contain
 *  configuration.
 *
 */
abstract class Configurable
extends DeclarationBase implements IToStringBuildable {

  private List<Property> properties = new ArrayList<>();

  public void addProperty(Property property) {
    if (property != null) {
      properties.add(property);
    }
  }

  public Collection<Property> getConfiguration() {
    return properties;
  }

  public void put(Property property) {
    addProperty(property);
  }

  @Override
    public void toString(ToStringBuilder builder) {
      builder.append("properties", properties);
    }

  public void putConfiguration(Configurable source) {
    if (source.properties != null && source.properties.size() > 0) {
      if (properties == null) {
        properties = new ArrayList<>();
      }
      properties.addAll(source.properties);
    }
  }

}
