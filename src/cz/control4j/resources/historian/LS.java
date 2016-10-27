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

import cz.lidinsky.tools.text.ArticleBuilder;
import cz.lidinsky.tools.text.TableBuilder;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jilm
 */
public class LS {

  public static void main(String[] args) {

    System.out.println(FileWriter.STORE_PATH);
    File dir = new File(FileWriter.STORE_PATH);

    if (args.length == 0) {
      ArticleBuilder builder = new ArticleBuilder("List of historian files");
      TableBuilder table = builder.appendTable();
      File[] list = dir.listFiles();
      for (File file : list) {
        try {
          FileReader reader = new FileReader();
          reader.read(file);
          System.out.println(file.getName());
          table.newRow();
          table.appendValue("filename", file.getName());
          table.appendValue("begin", Instant.ofEpochMilli(reader.getTimestamp()).toString());
          table.appendValue("length", Long.toString(reader.getLength()));
          table.appendValue("sample per.", Long.toString(reader.getSamplePeriod() / 1000));
        } catch (IOException ex) {
        }
      }
      (new cz.lidinsky.tools.text.Formatter()).format(builder.serialize());

    } else {
      File file = new File(dir, args[0]);
      FileReader reader = new FileReader();
      try {
        reader.read(file);
        for (String label : reader.getLabels()) {
          System.out.println(label);
        }
      } catch (IOException ex) {
        Logger.getLogger(LS.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

}
