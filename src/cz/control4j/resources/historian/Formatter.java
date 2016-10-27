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
package cz.control4j.resources.historian;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author jilm
 */
public class Formatter {

    public static void main(String[] args) {

    File dir = new File(FileWriter.STORE_PATH);
    File file = new File(dir, args[0]);
    FileReader reader = new FileReader();
    try {
      reader.read(file);
      System.out.println(String.join(" ", reader.getLabels()));
      double buffer[] = new double[reader.getLabels().length];
      while (true) {
        reader.read(buffer, buffer.length);
        System.out.println(Arrays.stream(buffer).mapToObj(Double::toString).collect(Collectors.joining(" ")));
      }
    } catch (IOException ex) {
      Logger.getLogger(CUT.class.getName()).log(Level.SEVERE, null, ex);
    }

  }


}
