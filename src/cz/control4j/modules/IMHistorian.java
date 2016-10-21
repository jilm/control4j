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

import cz.control4j.ICycleEventListener;
import cz.control4j.InputModule;
import cz.control4j.RuntimeException;
import cz.control4j.Signal;
import cz.control4j.application.IO;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 */
public class IMHistorian extends InputModule implements ICycleEventListener {

  private final List<Meta> metas = new ArrayList<>();

  private float[] buffer;
  private int bufferIndex = 0;

  @Override
  public void prepare() {
    buffer = new float[1024 * metas.size()];

  }

  @Override
  protected void put(Signal[] input, int inputLength) throws RuntimeException {
    for (int i = 0; i < inputLength; i++) {
      buffer[bufferIndex + i] = getValue(input[i]);
    }
    bufferIndex += inputLength;
  }

  private float getValue(Signal signal) {
    if (signal == null || !signal.isValid()) {
      return Float.NaN;
    } else {
      return (float)signal.getValue();
    }
  }

  /**
   *
   * @param input
   * @return
   */
  @Override
  public int getInputIndex(IO input) {
    metas.add(new Meta());
    return metas.size() - 1;
  }

  @Override
  public void scanStart() {
  }

  @Override
  public void processingStart() {
  }

  @Override
  public void scanEnd() {
    if (buffer.length - bufferIndex <= metas.size()) {
      try (
          OutputStream os = new FileOutputStream(getFilename());
          DataOutputStream dos = new DataOutputStream(os);
          ) {
      dos.writeInt(metas.size());
      for (int i = 0; i < bufferIndex; i++) {
        dos.writeFloat(buffer[i]);
      }
      bufferIndex = 0;
    } catch (IOException ex) {
        Logger.getLogger(IMHistorian.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  private class Meta {
  }

  private String getFilename() {
    return Long.toHexString(System.currentTimeMillis()) + ".rec";
  }

}
