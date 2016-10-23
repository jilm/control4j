/*
 * Copyright (C) 2016 jilm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.control4j.resources;

import cz.lidinsky.tools.text.ArticleBuilder;
import cz.lidinsky.tools.text.TableBuilder;

/**
 *
 */
public class Console {

  public static void main(String[] args) {
    ArticleBuilder message = new ArticleBuilder("");
    TableBuilder table = message.appendTable();
    table.appendValue("label", "outdoor tamp.");
    table.appendValue("value", "12.5");
    table.appendValue("unit", "deg. C");
    String temp = message.serialize();
    System.out.println(temp);
//    new Formatter().format(temp);
  }
}
