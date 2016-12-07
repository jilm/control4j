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

import cz.control4j.application.Scope;

/**
 *
 *  Represents a property of some higher level object. Each property is
 *  uniquely identified by the key. This object has two variants.
 *  <ol>
 *    <li>Property which directly contains a value.
 *    <li>Property which refers to some define object.
 *  </ol>
 *
 */
public class Property extends DeclarationBase implements IReference {

    /** An empty constructor. */
    public Property() { }

    /** Unique identifier of the property. */
    private String key;

    /**
     * Returns a unique identifier of this property.
     *
     * @return identifier of the property
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the identifier of this property.
     *
     * @param key
     *            property identifier
     *
     * @return this property
     */
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
     * Sets the reference to some definition object.
     *
     * @param href
     *            reference to some definition object
     */
    @Override
    public void setHref(String href) {
        this.href = href;
        if (href != null) {
            this.isReference = true;
        }
    }

    /** Scope under which this property is defined. */
    private Scope scope;

    /**
     * Returns scope under which this object is defined.
     *
     * @return scope of this property
     */
    @Override
    public Scope getScope() {
        return scope;
    }

    /**
     * Sets the scope of this property object.
     *
     * @param scope
     *            scope of this property object
     */
    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    private boolean isReference;

    /**
     * Returns false if this object contains the value directly, true if
     * this object contains reference to some definition object instead.
     *
     * @return false if the value was given directly, true if the reference
     *            was given istead
     */
    public boolean isReference() {
        return isReference;
    }

}
