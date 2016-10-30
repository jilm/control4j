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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * List the archive informations.
 */
public class LS implements Iterable<FileReader> {

  /** Archive directory. */
  private final File archiveDir;

  /** List of all of the files inside the archive directory. */
  private final File[] fileList;

  /** Initialize internal variables. */
  public LS() {
    String STORE_PATH = System.getProperty("HISTORIAN_PATH", "C:\\Users\\jilm\\Documents\\hist");
    archiveDir = new File(STORE_PATH);
    fileList = archiveDir.listFiles();
  }

  @Override
  public Iterator<FileReader> iterator() {
    return new Iterator<FileReader>() {

      /**
       * Index into the fileList array, it points to the file that has not
       * been discovered yet.
       */
      private int index = 0;

      /**
       * An object that will be returned next time, the next method is called.
       */
      private FileReader nextReader;

      @Override
      public boolean hasNext() {
        if (nextReader != null) {
          // if there is a reader available
          return true;
        } else {
          // else try to find one
          while (index < fileList.length) {
            try {
              nextReader = new FileReader();
              nextReader.read(fileList[index++]);
              return true;
            } catch (IOException ex) {
              nextReader = null;
            }
          }
        }
        return false;
      }

      @Override
      public FileReader next() {
        if (hasNext()) {
          FileReader reader = nextReader;
          nextReader = null;
          return reader;
        } else {
          throw new NoSuchElementException();
        }
      }
    };
  }

  public static void main(String[] args) {

    LS instance = new LS();

    if (args.length == 0) {
      // list all of the files
      ArticleBuilder builder = new ArticleBuilder("List of historian files");
      TableBuilder table = builder.appendTable();
      for (FileReader reader : instance) {
        table.newRow();
        table.appendValue("filename", reader.getFile().getName());
        table.appendValue("begin", Instant.ofEpochMilli(reader.getTimestamp()).toString());
        table.appendValue("length", Long.toString(reader.getLength()));
        table.appendValue("sample per.", Long.toString(reader.getSamplePeriod() / 1000));
      }
      (new cz.lidinsky.tools.text.Formatter()).format(builder.serialize());

    } else if (args[0].equals("-s")) {

      Set<String> signals = new HashSet<>();
      for (FileReader reader : instance) {
        signals.addAll(Arrays.asList(reader.getLabels()));
      }
      ArticleBuilder builder = new ArticleBuilder("List of signals");
      TableBuilder table = builder.appendTable();
      signals.stream().forEach(sig -> {
        table.newRow();
        table.appendValue("signal", sig);
      });
      (new cz.lidinsky.tools.text.Formatter()).format(builder.serialize());

    } else {

      File file = new File(instance.archiveDir, args[0]);
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
