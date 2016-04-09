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

import java.util.Arrays;
import java.util.Date;

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
   *
   * @param length
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

}
