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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author jilm
 */
public class FileReader {

  DataInputStream dis;

  private File file;

  public void read(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    //GZIPInputStream gzis = new GZIPInputStream(fis);
    dis = new DataInputStream(fis);
    readHead(dis);
    System.out.println(dis.available());
    this.file = file;
  }

  private int signals;
  private long timestamp;
  private long samplePeriod;
  private String[] labels;
  private long length;

  public String[] getLabels() {
    return labels;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getLength() {
    return length / Float.BYTES / signals;
  }

  public long getSamplePeriod() {
    return samplePeriod;
  }

  public File getFile() {
    return file;
  }

  public void read(float[] buffer, int length) throws IOException {
    for (int i = 0; i < length; i++) {
      buffer[i] = dis.readFloat();
    }
  }

  public void read(double[] buffer, int length) throws IOException {
    for (int i = 0; i < length; i++) {
      buffer[i] = dis.readFloat();
    }
  }

  protected void readHead(DataInputStream dis) throws IOException {
    //long: identifier HISTOR01</li>
    long identifier = dis.readLong();
    if (identifier != FileWriter.IDENTIFIER) {
      throw new IOException("Not supported file format");
    }
    // byte: number of signals, max 127 signals
    signals = dis.readByte();
    // long: timestamp of the first set of signals
    timestamp = dis.readLong();
    // long: sample period [ms]
    samplePeriod = dis.readLong();
    // double: number to add to obtain obtain original number, one value per signal
    for (int i = 0; i < signals; i++) {
      dis.readDouble();
    }
    // double: number to multiply to obtain original number, one value per signal
    for (int i = 0; i < signals; i++) {
      dis.readDouble();
    }
    // int: 0 - integer datatype, 1 - real datatype
    for (int i = 0; i < signals; i++) {
      dis.readInt();
    }
    // int: number of bytes of the datatype for each signal
    for (int i = 0; i < signals; i++) {
      dis.readInt();
    }
    // String: signal label for eacch of the signals
    labels = new String[signals];
    for (int i = 0; i < signals; i++) {
      labels[i] = dis.readUTF();
    }
    this.length = dis.available();
  }

}
