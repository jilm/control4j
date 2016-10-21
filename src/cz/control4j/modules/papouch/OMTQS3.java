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
package cz.control4j.modules.papouch;

import cz.control4j.ICycleEventListener;
import cz.control4j.Output;
import cz.control4j.OutputModule;
import cz.control4j.Signal;
import cz.control4j.SignalUtils;
import cz.lidinsky.spinel.PhysicalPeer;
import cz.lidinsky.spinel.SpinelException;
import cz.lidinsky.spinel.SpinelMessage;
import cz.lidinsky.spinel.Transaction;
import cz.lidinsky.tools.reflect.Setter;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Encapsulate hardware IO module TQS3 which is a thermometer. The temperature
 * is read and provided on the module output. The measurement request is sent on
 * cycleStart event.
 *
 * <p>
 * Property: address, a spinel address of the thermometer.
 *
 * Resource: a Spinel class is required
 *
 * Output: 0, The measured temperature. The output is a scalar real number. The
 * unit is a celsius degree. The timestamp is equal to the cycle start. If the
 * value is not obtained till the processingStart event, the invalid signal is
 * returned.
 *
 */
@Output(alias = "out", index = 0)
public class OMTQS3 extends OutputModule implements ICycleEventListener {

  /**
   * Spinel address of the module.
   */
  private int address;

  /* temperature measurement */
  private SpinelMessage request;

  private Transaction transaction;

  private Date timestamp;

  private int status = 10;

  private PhysicalPeer channel;

  /**
   * Creates a request for new measurement and pick-up the spinel comunication
   * channel.
   */
  @Override
  public void prepare() {
    request
        = new SpinelMessage(address, cz.lidinsky.papouch.TQS3.MEASUREMENT);
    InetSocketAddress socketAddress = new InetSocketAddress(host, port);
    channel = Resources.getInstance().get(socketAddress);
  }

  /**
   * Not used
   */
  @Override
  public void scanEnd() { }

  /**
   * Sends request for new data.
   */
  @Override
  public void scanStart() {
    // Request for temperature
    if (channel != null) {
      transaction = channel.putRequest(request);
    } else {
      transaction = null;
    }
  }

  /**
   * Gets the response message and interprets is.
   */
  @Override
  public void processingStart() { }

  @Override
  protected void get(Signal[] output, int outputLength) {
    if (transaction != null && transaction.hasResponse()) {
      try {
        double temperature = cz.lidinsky.papouch.TQS3.getOneTimeMeasurement(transaction.get(100));
        output[0] = SignalUtils.getSignal(temperature);
      } catch (SpinelException | TimeoutException ex) {
        Logger.getLogger(OMTQS3.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * @param address the address to set
   */
  @Setter("address")
  public void setAddress(int address) {
    this.address = address;
    request
        = new SpinelMessage(address, cz.lidinsky.papouch.TQS3.MEASUREMENT);
  }

  public int getAddress() {
    return this.address;
  }

  private String host;
  private int port;

  /**
   * @param host the host to set
   */
  @Setter("host")
  public void setHost(String host) {
    this.host = host;
  }

  public String getHost() {
    return this.host;
  }

  /**
   * @param port the port to set
   */
  @Setter("port")
  public void setPort(int port) {
    this.port = port;
  }

  public int getPort() {
    return this.port;
  }

}
