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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Save input signals into the file. The structure of the file is as follows:
 * <ol>
 *   <li>Header
 *     <ol>
 *       <li>long: identifier HISTOR01</li>
 *       <li>byte: number of signals, max 127 signals</li>
 *       <li>long: timestamp of the first set of signals</li>
 *       <li>long: sample period [ms]</li>
 *       <li>double: number to add to obtain obtain original number, one
 *                   value per signal</li>
 *       <li>double: number to multiply to obtain original number, one value
 *                   per signal</li>
 *       <li>int: 0 - integer datatype, 1 - real datatype
 *       <li>int: number of bytes of the datatype for each signal</li>
 *       <li>String: signal label for eacch of the signals</li>
 *     </ol>
 *   </li>
 *   <li>float: deta for each of the signal and for each sample
 *   </li>
 * </ol>
 */
public class FileWriter {

  /** HISTOR01 */
  public static final long IDENTIFIER = 0x484953544F523031L;

  public final static String STORE_PATH;

  private final static int BUFFER_SIZE = 1024;

  /** Circle buffer */
  private final float[] buffer;

  /** Number of signals. */
  private final int signals;

  /** Index of the first valid element in the buffer. */
  private int offset;

  /** How many elements in the buffer contain valid data. */
  private int length;

  private final long samplePeriod;

  /** Timestamp of the latest sample. */
  private long timestamp;

  private final String[] labels;

  private boolean stop;

  private boolean running;

  private Thread writerThread;

  static {
    STORE_PATH = System.getProperty("HISTORIAN_PATH", "C:\\Users\\jilm\\Documents\\hist");
  }

  private String PATH;

  public FileWriter(String[] labels, long samplePeriod) {
    this.labels = labels;
    this.signals = labels.length;
    this.buffer = new float[BUFFER_SIZE];
    //this.timestamp = timestamp;
    this.stop = false;
    this.running = false;
    this.samplePeriod = samplePeriod;
    System.out.println("Historian directory: " + STORE_PATH);
  }

  public synchronized void start(long timestamp) {
    if (!running && !stop) {
      this.running = true;
      this.timestamp = timestamp;
      writerThread = new Thread(this::fetch);
      writerThread.start();
      Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }
  }

  /**
   * Store next data sample.
   *
   * @param data
   */
  public synchronized void write(float[] data) {

    // check the status of this object
    if (stop || !running) {
      throw new IllegalStateException();
    }

    // check the size of the parameter array
    if (data == null) {
      throw new NullPointerException();
    }

    // copy data into the buffer
    for (int i = 0; i < signals; i++) {
      buffer[(offset + length + i) % BUFFER_SIZE] = data[i];
    }
    length += signals;

  }

  /**
   * Close opened file.
   */
  public synchronized void close() {
    System.out.println("Going to close historian file...");
    stop = true;
    notifyAll();
    try {
      writerThread.join(1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected String getFilename() {
    return Long.toHexString(System.currentTimeMillis()) + ".rec.gz";
  }

  protected File createFile(String filename) {
    return new File(PATH, filename);
  }

  protected synchronized void writeHead(DataOutputStream dos) throws IOException {
    //long: identifier HISTOR01</li>
    dos.writeLong(IDENTIFIER);
    // byte: number of signals, max 127 signals
    dos.writeByte(signals);
    // long: timestamp of the first set of signals
    dos.writeLong(timestamp);
    // long: sample period [ms]
    dos.writeLong(samplePeriod);
    // double: number to add to obtain obtain original number, one value per signal
    for (int i=0; i<signals; i++) {
      dos.writeDouble(0.0d);
    }
    // double: number to multiply to obtain original number, one value per signal
    for (int i=0; i<signals; i++) {
      dos.writeDouble(1.0d);
    }
    // int: 0 - integer datatype, 1 - real datatype
    for (int i=0; i<signals; i++) {
      dos.writeInt(1);
    }
    // int: number of bytes of the datatype for each signal
    for (int i=0; i<signals; i++) {
      dos.writeInt(Float.BYTES);
    }
    // String: signal label for eacch of the signals
    for (int i=0; i<signals; i++) {
      dos.writeUTF(labels[i]);
    }
  }

  /**
   * Writes the data from the buffer into the file.
   */
  public void fetch() {

    running = true;
    File workingFile = createFile(getFilename());
    FileOutputStream fos;
    DataOutputStream os = null;

    try {
        fos = new FileOutputStream(workingFile);
        os = new DataOutputStream(fos);

      writeHead(os);
      System.out.println("File created: " + workingFile.getName());

      while (!stop) {
        // wait for some data in the buffer
        synchronized(this) {
          do {
            try {
              wait((buffer.length - length) / 2 / signals * samplePeriod);
            } catch (InterruptedException ex) {
              // just waiting
            }
          } while (length == 0 && !stop);
        }

        int archLength;
        int archOffset;

        // mark indices
        synchronized(this) {
          archOffset = offset;
          archLength = length;
          offset += length;
          length = 0;
        }

        // write the content
        for (int i = 0; i < archLength; i++) {
          os.writeFloat(buffer[(archOffset + i) % BUFFER_SIZE]);
        }
        os.flush();
        System.out.println("Data written into the history file ...");

      }


    } catch (IOException ex) {
      Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      System.out.println("Going to close the history file...");
      running = false;
      try {
      //os.flush();
      os.close();
      } catch (Exception e) {}

    }

  }

  public static void main(String[] args) {
    FileWriter fw = new FileWriter(new String[] {"sig1", "sig2", "sig3"}, 1000);
    fw.start(System.currentTimeMillis());
    float[] data = new float[3];
    for (int i = 0; i< 60 * 60; i++) {
      data[0] = (float)Math.random();
      data[1] = (float)Math.random();
      data[2] = (float)Math.random();
      fw.write(data);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {

      }
    }
    fw.close();

  }

}
