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

/**
 *
 *  Represents a property of some higher level object.
 *  This object has two variants.
 *  <ol>
 *    <li>Property which directly contains a value.
 *    <li>Property which refers to some define object.
 *  </ol>
 *
 */
public class Property extends DeclarationBase implements IReference {

    /** An empty constructor. */
    public Property() { }

    private String key;

    public String getKey() {
        return key;
    }

    Property setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     *  Value of the property.
     */
    private String value;

    /**
     *  Returns the value of the property.
     *
     *  @return value which was set by the setValue method
     */
    public String getValue() {
        return value;
    }

    /**
     *  Set the value of the property.
     *
     *  @param value
     *             the value of the property
     *
     *  @return this object reference
     */
    Property setValue(String value) {
        this.value = value;
        if (value != null) {
            this.isReference = false;
        }
        return this;
    }

    private String href;

    /**
     *  Returns reference
     *
     *  @return reference to some definition object
     */
    @Override
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     */
    @Override
    public void setHref(String href) {
        this.href = href;
        if (href != null) {
            this.isReference = true;
        }
    }

    private int scope;

    /**
     *
     * @return
     */
    @Override
    public int getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     */
    @Override
    public void setScope(int scope) {
        this.scope = scope;
    }

    private boolean isReference;

    public boolean isReference() {
        return isReference;
    }

}
