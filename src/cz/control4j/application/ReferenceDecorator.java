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

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import static cz.lidinsky.tools.Validate.notBlank;
import static cz.lidinsky.tools.Validate.notNull;

/**
 *  Object which represents reference to some other object. A property may contain
 *  reference to the define object, or a use object is a reference to some block
 *  object for instance.
 *  This object is immutable.
 *
 *  <p>The referenced object is identified thanks to the href and scope.
 *
 *  <p>Moreover, this object contains a object which contain the reference
 *  The key parameter serves as an identifier inside the
 *  parent object.
 *
 *  @param <T>
 *             it is a datatype of the object which will contain resolved
 *             object
 */
public class ReferenceDecorator<T> extends Configurable {

    private final String href;
    private final Scope scope;
    private final T decorated;
    private final String key;

    /**
     *  Initialization.
     *
     *  @param href
     *             identifier of the referenced object
     *
     *  @param scope
     *             scope of the object which contains reference
     *
     *  @param decorated
     *             decorated object
     *
     *  @param key
     *             identifier of the referenced object inside the parent object
     *
     *  @throws CommonException
     *             if eiter of the arguments is null or blank
     */
    public ReferenceDecorator(
        String href, Scope scope, T decorated, String key) {

        try {
            this.href = notBlank(href);
            this.scope = notNull(scope);
            this.decorated = notNull(decorated);
            this.key = key;
        } catch (Exception e) {
            throw new CommonException()
                .setCause(e)
                .setCode(ExceptionCode.SYNTAX_ERROR)
                .set("message", "Bad argument for reference decorator!")
                .set("href", href)
                .set("scope", scope)
                .set("decorated", decorated);
        }
    }

    /**
     * Returns decorated object.
     *
     * @return decorated object
     */
    public T getDecorated() {
        return decorated;
    }

    /**
     * Returns identifier of the referenced object.
     *
     * @return identifier of the referenced object
     */
    public String getHref() {
        return href;
    }

    /**
     * Scope of the object with reference.
     *
     * @return scope of the object with reference
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns key, which is identifier of the referenced object inside the
     * parent (decorated) object.
     *
     * @return key
     */
    public String getKey() {
      return key;
    }

}
