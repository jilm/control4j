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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author jilm
 */
public class RExporter {

  public static void main(String[] args) {

    File dir = new File(FileWriter.STORE_PATH);
    File file = new File(dir, args[0]);
    FileReader reader = new FileReader();

    try {
      reader.read(file);
      long timestamp = reader.getTimestamp();
      DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
      int signals = reader.getLabels().length;
      double buffer[] = new double[signals];
      System.out.printf("timestamp ");
      System.out.println(
          String.join(" ", reader.getLabels()));
      while (true) {
        reader.read(buffer, buffer.length);
        System.out.print(timestamp);
        System.out.print(" ");
//        System.out.print(DateTimeFrmatter.ISO_TIME.format(LocalDateTime.ofEpochSecond(timestamp/1000, 0, ZoneOffset.UTC)));
//        System.out.print(" ");
        System.out.println(
            Arrays.stream(buffer)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(" ")));
        timestamp += reader.getSamplePeriod();
      }
    } catch (EOFException ex) {
      // just end of file
    } catch (IOException ex) {
      Logger.getLogger(CUT.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
