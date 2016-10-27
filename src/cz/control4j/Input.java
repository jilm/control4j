/*
 *  Copyright 2016 Jiri Lidinsky
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

package cz.control4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark one input of the module class. It may be used to denote just single
 * input, just like that:<br>
 *
 * {@literal @Input(index=0 alias="en" description="Enable input")}<br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ModuleInputs.class)
public @interface Input
{
  /**
   * It is the index into the input array of the processing method.
   *
   * @return input index
   */
  int index() default -1;

  /**
   * Identifier of the input.
   *
   * @return input identifier
   */
  String alias() default "";

  /**
   * Short description if the input.
   *
   * @return input description
   */
  String description() default "";

}
