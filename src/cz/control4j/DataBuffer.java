/*
 *  Copyright 2013, 2014, 2015, 2016 Jiri Lidinsky
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

import java.util.Arrays;

/**
 *  In principle, the DataBuffer is just an array of signals, which
 *  are identified by zero based index.
 *
 *  <p>Serves as a data exchange point between computation modules.
 *  There are two fundamental methods in this class. Call {@link #get}
 *  method to obtain an array of signals which are needed as an
 *  input for some module. Call {@link #put} method after a module is
 *  finished to store the output of the module for the future use.
 *
 *  <p>Methods <code>put</code> and <code>get</code> needs an extra
 *  parameter which maps index of input or output of the particular
 *  module into the index of the <code>DataBuffer</code> array.
 *
 *  <p>For the purpose of signal exchange between modules and this
 *  data buffer the only array is used over and over again. This array
 *  is big enough to contain data for a module with the bigest number
 *  of input signals. The relevant data are filled in into the lower
 *  part of the array and the extra elements are left behind. Therefore,
 *  the module should not relay on the size of the array it gets as
 *  an input!
 *
 *  <p>At the beginnig of each scan the DataBuffer should be erased
 *  so that it is empty.
 */
class DataBuffer
{

  private final Signal[] buffer;
  private Signal[] crate;

  /**
   *  Alocates the internal store with the given size.
   *
   *  @param size
   *             required capacity of the buffer
   */
  public DataBuffer(int size)
  {
    this.buffer = new Signal[size];
    this.crate = new Signal[10];
  }

  /**
   *  Erase all of the values in the buffer. This method should be
   *  called before each scan starts. After it is called, the
   *  buffer is empty.
   */
  public void clear()
  {
    Arrays.fill(buffer, null);
  }

  /**
   *  Returns requested signals. Purpose of this
   *  method is to obtain input signals for the
   *  module to be executed.
   *
   *  @param requestedSignals
   *             must be an array of
   *             indices of signals that are to be returned.
   *             If some position is unutilized, it should
   *             contain -1.
   *
   *  @return an array of signals whose indices
   *             are given as parameter. The size of
   *             the array may be greater than the size of parameter
   *             requestedSignals. Position of returned signals correspond
   *             to the position of indices in the param array.
   *
   *  @throws IndexOutOfBoundsException
   *             if requestedSignals array
   *             contains index which is out of bounds of the internal
   *             buffer.
   */
  Signal[] get(int[] requestedSignals)
  {
    int length = requestedSignals.length;
    if (length > crate.length) crate = new Signal[length];
    for (int i=0; i<length; i++)
    {
      if (requestedSignals[i] >= 0)
        crate[i] = buffer[requestedSignals[i]];
      else
        crate[i] = null;
    }
    return crate;
  }

  /**
   *  Stores signals into the buffer. Signals to store are passed
   *  as an argument.
   *  Purpose of this method is to store signals produced as the
   *  output of some module. Not all of the signals are to be stored.
   *
   *  @param signals
   *             an array of signals that are be stored
   *             into the data buffer.
   *
   *  @param map
   *             for each signal, it contains index inside the buffer.
   *             indices specify where to store corresponding
   *             signal. If the corresponding signal is not to
   *             be stored, the index must be -1. The map array
   *             may be smaller than the signals array. Extra
   *             signals are silently ignored.
   *
   *  @throws IndexOutOfBoundsException
   *             if some index in the map
   *             array is greater than internal buffer size
   *
   *  @throws NullPointerException
   *             if some signal in signals array
   *             contain null value and the corresponding index in
   *             the map array is positive
   */
  public void put(Signal[] signals, int[] map)
  {
    for (int i=0; i<map.length; i++)
      if (map[i] >= 0)
      {
        buffer[map[i]] = signals[i];
      }
  }

  /**
   *  Return size of internal buffer.
   *
   *  @return size of internal buffer
   */
  public int size()
  {
    return buffer.length;
  }

  /**
   *  Writes a content of the data buffer to the given print writer.
   *  serves for debug purposes.
   *
   *  @param writer
   *             a writer where the content of the buffer will be printed
   */
  void dump(java.io.PrintWriter writer)
  {
    writer.println("DATA BUFFER CONTENT:");
    for (int i=0; i<buffer.length; i++)
    {
      if (buffer[i] != null) {
        writer.println(" " + i + ": " + buffer[i].toString());
      } else {
        writer.println(" " + i + ": null");
      }
    }
  }

}
