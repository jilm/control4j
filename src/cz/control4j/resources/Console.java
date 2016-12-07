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
package cz.control4j.resources;

import cz.control4j.Control;
import cz.control4j.ICycleEventListener;
import cz.control4j.Signal;
import cz.control4j.SignalFormat;
import cz.control4j.SignalUtils;
import cz.lidinsky.tools.text.Formatter;
import cz.lidinsky.tools.text.MessageBuilder;
import cz.lidinsky.tools.text.TableBuilder;

/**
 *
 */
public class Console implements ICycleEventListener {

  private MessageBuilder messageBuilder;

  private TableBuilder values;

  public void print(String label, Signal signal) {
    values.newRow();
    values.appendValue("label", label);
    values.appendValue("value", signal.valueToString(new SignalFormat()));
  }

  //--------------------------------------- Scan event listener implementation.

  @Override
  public void scanStart() {
    messageBuilder = new MessageBuilder();
    messageBuilder.appendParagraph().add(
        String.format("---- Scan #%d", Control.getScanNumber()));
    values = messageBuilder.appendTable();
  }

  @Override
  public void processingStart() {
  }

  @Override
  public void scanEnd() {
    System.out.println(
        new Formatter().format(messageBuilder.serialize()));
  }

  public static void main(String[] args) {
    Console instance = new Console();
    MessageBuilder messageBuilder = new MessageBuilder();
    String str = String.format("---- Scan #%d", 10);
    messageBuilder.appendParagraph().add(str);
    instance.values = messageBuilder.appendTable();
    instance.print("sig1", SignalUtils.getSignal(54.6));
    instance.print("sig2", SignalUtils.getSignal());
    str = messageBuilder.serialize();
    str = new Formatter().format(str);
    System.out.println(str);
  }

}
