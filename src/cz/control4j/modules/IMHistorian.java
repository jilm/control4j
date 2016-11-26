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
package cz.control4j.modules;

import cz.control4j.InputModule;
import cz.control4j.RuntimeException;
import cz.control4j.Signal;
import cz.control4j.application.IO;
import cz.control4j.resources.historian.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Save input signals into the file. The structure of the file is as follows:
 * <ol>
 *   <li>Header
 *     <ol>
 *       <li>integer: number of signals</li>
 *       <li>long: timestamp of the first set of signals</li>
 *       <li>long: sample period [ms]</li>
 *       <li>int: 0 - integer datatype, 1 - real datatype
 *       <li>int: number of bytes of the datatype for each signal</li>
 *       <li>String: signal label for eacch of the signals</li>
 *     </ol>
 *   </li>
 *   <li>float: deta for each of the signal and for each sample
 *   </li>
 * </ol>
 *
 * <p>It uses {@link cz.control4j.resources.historian.FileWriter} class
 * to store data into the file.
 *
 */
public class IMHistorian extends InputModule {

  /** Back-end storage object. */
  private FileWriter writer;

  /** Identifiers inside the history file. */
  private List<String> labels;

  @Override
  public void initialize() {
    labels = new ArrayList<>();
  }

  @Override
  public void prepare() {
    String[] labelArray = new String[labels.size()];
    labelArray = (String[]) labels.toArray(labelArray);
    writer = new FileWriter(labelArray, 1000);
    writer.start(System.currentTimeMillis());
    //Runtime.getRuntime().addShutdownHook(new Thread(writer::close));
  }

  @Override
  public int getInputIndex(IO input) {
    labels.add(input.getKey());
    return labels.size() - 1;
  }

  @Override
  protected void put(Signal[] input, int inputLength) throws RuntimeException {
    float[] values = new float[inputLength];
    for (int i=0; i<inputLength; i++) {
      values[i] = (input[i] == null || !input[i].isValid())
          ? Float.NaN
          : (float)input[i].getValue();
    }
    writer.write(values);
  }

}
