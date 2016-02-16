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

import static cz.lidinsky.tools.Validate.notNull;

/**
 *  A property of some object like a module or a resource. Property is just
 *  a value. This is immutable object.
 */
public class Property extends DeclarationBase {

    private final String value;
    
    /**
     *  Initialize the value.
     * 
     *  @param value
     *             property value
     */
    public Property(String value) {
        this.value = notNull(value);
    }

    /**
     *  Returns value of the property.
     * 
     *  @return value of the property
     */
    public String getValue() {
        return value;
    }

}
