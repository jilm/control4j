/*
 *  Copyright 2013, 2014, 2016, 2020 Jiri Lidinsky
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

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import java.util.Date;

/**
 *
 *  Represents a value which is transferred from the output of one module to the
 *  input of another module or modules. From java point of view, a datatype of 
 *  the value may be real number, boolean, integer, ... Moreover, the value may
 *  be unknown (invalid). For every datatype used, the class is created.
 *
 *  <p>Each signal has a name that is used to connect desired module output with
 *  proper module input or inputs. Once the control loop has been run, names are
 *  useless.
 * 
 *  <p>Once the signal is created, the value is immutable. There are static 
 *  factory methods to create signal objects, use them.
 *
 *  <p>Validity is boolean property of the signal object. It expresses accepta-
 *  bility of the value of the signal. It should be false in case when it is
 *  impossible to find out the value. It should be false when the measuring
 *  instrument is broken for instance or when the measured value is out of
 *  range. When signal is invalid the value property is not defined.
 *
 */
public abstract class Signal implements java.io.Serializable, Cloneable {

    private static final BooleanSignal TRUE_SIGNAL = new BooleanSignal(true);
    private static final BooleanSignal FALSE_SIGNAL = new BooleanSignal(false);
    private static final InvalidSignal INVALID_SIGNAL = new InvalidSignal();
    
    /**
     *  Create and return a new valid boolean Signal object with value set
     *  to <code>value</code>.
     *
     *  @param value desired value of the Signal
     *
     *  @return valid Signal object which value is set to
     *          <code>value</code>
     */
    public static Signal getSignal(boolean value) {
        if (value) {
            return TRUE_SIGNAL;
        } else {
            return FALSE_SIGNAL;
        }
    }

    /**
     *  Create and return new invalid signal object.
     *
     *  @return invalid signal object
     */
    public static Signal getSignal() {
        return INVALID_SIGNAL;
    }

    /**
     *  Create and return new valid signal with given value
     *
     *  @param value desired value of the object
     *  @return new valid object with given value
     */
    public static Signal getSignal(double value) {
        return new DoubleSignal(value);
    }

    /**
     *  Create and return new valid, vector signal object. Value of returned
     *  object is set to the vector of real numbers which are given as parameter
     *
     *  @param value desired value of the object
     *  @return new valid vector signal object with value set to
     *          <code>value</code>
     */
    public static Signal getSignal(double[] value) {
        return new VectorSignal(value);
    }

    /**
     *  Return value of the signal. This method shouldn't be called on invalid
     *  signal. In case of vector signals it returns the first number (index 0).
     *
     *  @return value of the signal
     *  @throws IndexOutOfBoundsException in case of empty vector signal
     */
    public abstract double getValue();

    /**
     *  Provide access to the elements of the vector signal. Scalar signals are
     *  treated as vector signals of size one. So the only appropriet index is
     *  zero for them.
     *
     *  @param index index of the vector element whose value is returned. Index
     *         is zero based.
     *  @return value of the vector with index <code>index</code>
     *  @throws IndexOutOfBoundsException in case the index exceeds the size of
     *         the vector
     */
    public abstract double getValue(int index);

    /**
     *  Retrun value of this object as it was a boolean value. If Signal object
     *  contains naturaly double value the interpretation is as follows: returned
     *  value will be true if and only if internal double value is greater or
     *  equal to 0.5. Otherwise it returns false. It means it returns false for
     *  values that are lower than 0.5. For vector signals it returns boolean
     *  representation of the first value (index 0).
     *
     *  @return boolean reprezentaion of the signal
     *  @throws IndexOutOfBoundsException for empty vector signals
     */
    public boolean getBoolean() {
        return getValue() >= 0.5;
    }

    /**
     *  Return size of the vector signals, it means number of elements of the
     *  vector. It returns number 1 for scalar signals.
     *
     *  @return number of values for vector signals or one for scalar signal.
     */
    public int getSize() {
        return 1;
    }
 
    /**
     *  Return validity of the signal.
     *
     *  @return true if and only if signal is valid.
     */
    public abstract boolean isValid();

    /**
     *  Compare this signal object with signal object given as a parameter and
     *  return true if both objects are similar.
     *  Only validity and value properties are compared. So this method returns 
     *  true if and only if both of the objects
     *  are of the same type and has equal value. This means that if both of the
     *  are invalid it returns true, or if both are valid and both are real
     *  number scalars and values are set to 12, it returns true. But if one
     *  object is boolean with value false and second is real number scalar
     *  which value is 0, it returns false, even when <code>getValue()</code>
     *  method returns 0 for both of the objects.
     *
     *  <p>This method is specifically designed to detect whether the value of
     *  a signal changes over time. If you need to detect whether two signals
     *  are identical, including time, use method <code>equals</code> instead.
     *
     *  @param signal object to compare with
     *
     *  @return true if either both signals are invalid or both signals are valid
     *         and has equal values. If argument is <code>null</code>, it returns
     *         false.
     *
     *  @see #equals(Object)
     */
    public abstract boolean hasEqualValue(Signal signal);

    /**
     *  Compare two object for equality. The result is <code>true</code> if the
     *  argument is not null and a value of both objects is the
     *  same. Metainformations like name and unit are not compared.
     *
     *  @param object the object to compare with
     *
     *  @return true if and only if value of the argument and this object are equal.
     *
     *  @see #hasEqualValue(Signal)
     */
    @Override public abstract boolean equals(Object object);

    /**
     *  Return a string representation of the object. This method is not aimed
     *  to obtain human readable form. See overloaded method instead.
     *
     *  @return a string representation of the object.
     *
     *  @see #toString(SignalFormat, String, String)
     *  @see #valueToString(SignalFormat)
     */
    @Override
    public String toString() {
        char delimiter = ';';
        StringBuffer sb = new StringBuffer(50);
        return sb.append("Signal: ")
            .append(getValue())
            .toString();
    }

    /**
     *  Return string representation of the signal in human readable format.
     *  The returned string contains all of the properties separated by
     *  <code>delimiter</code> The order of properties is as follows:,
     *  name, value, 
     * 
     *  @param format
     *             determine the form and locale
     *
     *  @param delimiter
     *             delimiter which will be used to delimit properties in
     *             the returned string
     *
     *  @param label
     *             signal label which will be printed together with signal
     *             value. It should be used to distinguish the signal.
     *
     *  @return string representation of the signal
     *
     *  @see #valueToString(SignalFormat)
     */
    public String toString(SignalFormat format, String delimiter, String label) {
        var sb = new StringBuffer();
        return sb.append(label)
            .append(": ")
            .append(valueToString(format))
            .toString();
    }

    /**
     *  Return a string representation of the value.
     *  Given format parameter is used to properly display
     *  numbers according to locals. Invalid signals
     *  return "?".
     *
     *  @param format is used to propely format numbers according
     *         to locales
     *  @return a string representation of the value
     *
     *  @see #toString(SignalFormat, String, String)
     */
    public abstract String valueToString(SignalFormat format);

    @Override public abstract Object clone();

    //---------------------------------------------------------- Invalid Signal

    /**
     *  Represent an invalid signal. It means that validity property
     *  is set to false and value of this object is undefined.
     */
    static private class InvalidSignal extends Signal {

        private InvalidSignal() {
            super();
        }

        @Override public boolean isValid() {
            return false;
        }

        @Override public double getValue() {
            throw new CommonException()
                .setCode(ExceptionCode.UNSUPPORTED_OPERATION)
                .set("message", "Invalid signal value is not defined!");
        }

        @Override public double getValue(int index) {
            throw new CommonException()
                .setCode(ExceptionCode.UNSUPPORTED_OPERATION)
                .set("message", "Invalid signal value is not defined!");
        }

        @Override public boolean getBoolean() {
            throw new CommonException()
                .setCode(ExceptionCode.UNSUPPORTED_OPERATION)
                .set("message", "Invalid signal value is not defined!");
        }

        @Override public String valueToString(SignalFormat format) {
            return "?";
        }

        @Override public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj instanceof InvalidSignal) {
                return true;
            } else {
                return false;
            }
        }

        @Override public boolean hasEqualValue(Signal signal) {
            if (signal == null) return false;
            return signal instanceof InvalidSignal;
        }

        @Override public Object clone() {
            InvalidSignal clone = new InvalidSignal();
            return clone;
        }

        /**
         *  Return a string representation of the object. This method is not aimed
         *  to abtain human readable form. See overloaded method instead.
         *
         *  @return a string representation of the object.
         *
         *  @see #toString(SignalFormat, String, String)
         *  @see #valueToString(SignalFormat)
         */
        @Override
        public String toString() {
            char separator = ';';
            StringBuffer sb = new StringBuffer(50);
            sb.append(isValid());
            return sb.toString();
        }

    }

    /**
     *  Abstract class which is common base class for all of
     *  the valid signals.
     */
    static private abstract class ValidSignal extends Signal {

        private ValidSignal() {
            super();
        }

        @Override public boolean isValid() {
            return true;
        }

    }

    /**
     *  Valid signal whose value is of type boolean. This
     *  value is tranparently typecast to real number as
     *  follows: false - 0.0, true - 1.0.
     */
    static private class BooleanSignal extends ValidSignal
    {
        private boolean value;

        BooleanSignal(boolean value) {
            super();
            this.value = value;
        }

        @Override public double getValue() {
            return value ? 1.0 : 0.0;
        }

        @Override public double getValue(int index) {
            if (index != 0)
                throw new IndexOutOfBoundsException();
            else
                return getValue();
        }

        @Override public boolean getBoolean() {
            return value;
        }

        @Override public String valueToString(SignalFormat format) {
            return format.format(value);
        }

        @Override public boolean hasEqualValue(Signal signal) {
            if (signal == null) return false;
            if (signal instanceof BooleanSignal)
                return this.value == signal.getBoolean();
            else
                return false;
        }

        @Override public boolean equals(Object object) {
            if (object == null) return false;
            if (object instanceof BooleanSignal) {
                BooleanSignal signal = (BooleanSignal)object;
                if (this.value == signal.value)
                    return true;
            }
            return false;
        }

        @Override public int hashCode() {
            return Boolean.valueOf(value).hashCode();
        }

        @Override public Object clone() {
            BooleanSignal clone = new BooleanSignal(value);
            return clone;
        }

    }

    /**
     *  Object which represents signals with single real number value.
     */
    static private class DoubleSignal extends ValidSignal {

        private double value;

        DoubleSignal(double value) {
            super();
            this.value = value;
        }

        @Override public double getValue() {
            return value;
        }

        @Override public double getValue(int index) {
            if (index != 0)
                throw new IndexOutOfBoundsException();
            else
                return value;
        }

        @Override public String valueToString(SignalFormat format) {
            return format.format(value);
        }

        @Override public boolean hasEqualValue(Signal signal) {
            if (signal == null) return false;
            if (signal instanceof DoubleSignal)
                return this.value == signal.getValue();
            else
                return false;
        }

        @Override public boolean equals(Object object) {
            if (object == null) return false;
            if (object instanceof DoubleSignal) {
                DoubleSignal signal = (DoubleSignal)object;
                if (this.value == signal.value)
                    return true;
            }
            return false;
        }

        @Override public int hashCode() {
            return Double.valueOf(value).hashCode();
        }

        @Override public Object clone() {
            DoubleSignal clone = new DoubleSignal(value);
            return clone;
        }

    }

    /**
     *  Represents valid signal which contains vector of
     *  real values.
     */
    private static class VectorSignal extends ValidSignal  {

        private double[] value;

        VectorSignal(double[] value)  {
            super();
            this.value = value;
        }

        @Override public int getSize() {
            return value.length;
        }

        @Override public double getValue() {
            return value[0];
        }

        @Override public double getValue(int index) {
            return value[index];
        }

        @Override public boolean getBoolean() {
            return value[0] < 0.5 ? false : true;
        }

        @Override public String valueToString(SignalFormat format) {
            StringBuffer sb = new StringBuffer();
            sb.append('[');
            if (value.length > 0) {
                sb.append(format.format(value[0]));
                for (int i=1; i<value.length; i++) {
                    sb.append("; ");
                    sb.append(format.format(value[i]));
                }
            }
            sb.append(']');
            return sb.toString();
        }

        @Override public boolean hasEqualValue(Signal signal) {
            if (signal == null) return false;
            if (signal instanceof VectorSignal && this.value.length == signal.getSize()) {
                for (int i=0; i<this.value.length; i++)
                if (this.value[i] != signal.getValue(i)) return false;
                return true;
            }
            return false;
        }

        @Override public boolean equals(Object object) {
            if (object == null) return false;
            if (object instanceof VectorSignal) {
                VectorSignal signal = (VectorSignal)object;
                if (this.value.length != signal.value.length)
                    return false;
                for (int i=0; i<this.value.length; i++)
                    if (this.value[i] != signal.value[i])
                        return false;
                return true;
            }
            return false;
        }

        @Override public int hashCode() {
            int hash = 0;
            for (int i=0; i<value.length; i++)
                hash = 11 * hash + Double.valueOf(value[i]).hashCode();
            return hash;
        }

        @Override public Object clone() {
            VectorSignal clone = new VectorSignal(value);
            return clone;
        }

    }

}
