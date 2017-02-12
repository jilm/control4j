/*
 *  Copyright 2016 Jiri Lidinsky
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

import cz.lidinsky.signalserver.MessageUtils;
import cz.lidinsky.spinel.SpinelMessage;
import static cz.lidinsky.tools.Validate.notNull;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 */
public class SignalUtils
{

  /** Prevent instantiation. */
  private SignalUtils() {}

  /**
   *  Create and return a valid boolean Signal object with value set to
   *  <code>value</code> and timestamp set to actual system time.
   *
   *  @param value desired Signal value
   *
   *  @return new valid Signal object, value set to <code>value</code>
   *          and timestamp set to actual system time
   */
  public static Signal getSignal(boolean value) {
    return Signal.getSignal(value, new Date());
  }

  /**
   *  Create and return invalid signal. Timestamp is set tu current
   *  system time.
   *
   *  @return new invalid signal object
   */
  public static Signal getSignal() {
    return Signal.getSignal(new Date());
  }

  /**
   *  Create and return new valid signal with given value.
   *  Timestamp is set to current system time.
   *
   *  @param value desired value of the returned object
   *  @return new valid signal with given value
   */
  public static Signal getSignal(double value) {
    return Signal.getSignal(value, new Date());
  }

  /**
   *  Create and return new valid, vector signal. Value of returned object is
   *  set to vector of real numbers which are given as parameter. Timestamp is
   *  set to the current system time.
   *
   *  @param value desired value of the object
   *  @return new valid signal with value set to the given vector (array) of
   *          real numbers.
   */
  public static Signal getSignal(double[] value) {
    return Signal.getSignal(value, new Date());
  }

  /**
   * Fill-in invalid signals instead of null values.
   *
   * @param array
   *            an array to fill
   *
   * @param length
   *            how many elements to fill
   */
  public static void fillInvalid(Signal[] array, int length) {
    for (int i = 0; i < length; i++) {
      if (array[i] == null) {
        array[i] = getSignal();
      }
    }
  }

  public static int getValidityStatus(Signal[] input, int inputLength) {
    if (inputLength < 0 && inputLength > Integer.BYTES * 8) {
      throw new IllegalArgumentException();
    } else {
      int result = 0;
      for (int i = 0; i < inputLength; i++) {
        result *= 2;
        result |= input.length > i && input[i] != null && input[i].isValid()
            ? 1 : 0;
      }
      return result;
    }
  }

  public static double getValue(Signal[] input, int inputLength, int index) {
    if (index < input.length
        && index < inputLength
        && input[index] != null
        && input[index].isValid()) {
      return input[index].getValue();
    } else {
      return Double.NaN;
    }
  }

  public static boolean allValid(Signal[] signals, int length) {
    return Arrays.stream(signals, 0, length)
      .allMatch(signal -> signal != null && signal.isValid());
  }

  /**
   * Returns JSON representation of the given signal.
   *
   * @param signal
   *
   * @return
   */
  public static JSONObject toJSON(Signal signal) {
    if (!notNull(signal).isValid()) {
      return new JSONObject()
          .put("class", "cz.control4j.Signal")
          .put("validity", false);
    } else {
      return new JSONObject()
          .put("class", "cz.control4j.Signal")
          .put("validity", true)
          .put("value", signal.getValue());
    }
  }

  /**
   * Encode given signal into form of spinel message. The data of the spinel
   * message is as follows:
   *
   * 16bytes - first eigth characters of the given identifier
   * 4bytes - timestamp
   * 1byte  - validity
   * 8bytes - value
   *
   * @param id
   *            identifier of the signal. Only first eight characters of the
   *            identifier is used.
   *
   * @param signal
   *            signal to encode
   *
   * @return spinel message that contains given signal and identifier
   */
  public static ByteBuffer toSpinelData(String id, Signal signal) {
    ByteBuffer buffer = ByteBuffer.allocate(29);
    for (int i = 0; i < 8; i++) {
      buffer.putChar(id.length() <= i ? ' ' : id.charAt(i));
    }
    buffer.putLong(signal.getTimestamp().toInstant().toEpochMilli());
    buffer.put((byte) (signal.isValid() ? 0 : 1));
    buffer.putDouble(signal.isValid() ? signal.getValue() : Double.NaN);
    return buffer;
  }



  public static final int SPINEL_BUFFER_SIZE = Long.BYTES + Double.BYTES
      + MessageUtils.SPINEL_IDENTIFIER_SIZE * Character.BYTES + Byte.BYTES;

  public static SpinelMessage toSpinel(final int address,
      final int instruction, final String id, final Signal signal) {

    // data for the message
    ByteBuffer buffer = ByteBuffer.allocate(SPINEL_BUFFER_SIZE);
    for (int i = 0; i < MessageUtils.SPINEL_IDENTIFIER_SIZE; i++) {
      buffer.putChar(id.length() <= i ? ' ' : id.charAt(i));
    }
    buffer.putLong(signal.getTimestamp().toInstant().toEpochMilli());
    buffer.put((byte) (signal.isValid() ? 0 : 1));
    buffer.putDouble(signal.isValid() ? signal.getValue() : Double.NaN);
    // the whole message
    return new SpinelMessage(address, instruction, buffer);
  }


  public static Signal fromSpinel(SpinelMessage message) {

    ByteBuffer buffer = message.getData();
    buffer.rewind();
    // skip the id
    for (int i = 0; i < MessageUtils.SPINEL_IDENTIFIER_SIZE; i++) {
      buffer.getChar();
    }
    long timestamp = buffer.getLong();
    boolean valid = buffer.get() == 0;
    double value = buffer.getDouble();
    if (valid) {
      return Signal.getSignal(value, new Date(timestamp));
    } else {
      return Signal.getSignal(new Date(timestamp));
    }
  }

}
