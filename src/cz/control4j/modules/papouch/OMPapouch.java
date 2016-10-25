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
package cz.control4j.modules.papouch;

import cz.control4j.ICycleEventListener;
import cz.control4j.OutputModule;
import cz.lidinsky.spinel.PhysicalPeer;
import cz.lidinsky.spinel.SpinelMessage;
import cz.lidinsky.spinel.Transaction;
import cz.lidinsky.tools.reflect.Setter;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * Base class for papouch output modules.
 */
abstract class OMPapouch extends OutputModule implements ICycleEventListener {

  /** Spinel address of the module. */
  protected int address;

  /** Object which handle communication with papouch module. */
  private PhysicalPeer channel;

  /** Last transaction. */
  private Transaction transaction;

  /** Host. */
  protected String host;

  /** Port. */
  protected int port;

  /**
   * It have to return request message. This message will be sent to the
   * papouch hw in order to obtain measurement.
   *
   * @return request message
   */
  protected abstract SpinelMessage getRequest();

  /**
   * Returns response message if available.
   *
   * @return Message received from papouch hw module or null
   */
  protected SpinelMessage getResponse() {
    if (transaction != null && transaction.hasResponse()) {
      try {
        return transaction.get(100);
      } catch (TimeoutException ex) {
        // response is not just available
      }
    }
    return null;
  }

  /**
   * Get the object to handle communication. Such object may be shared between
   * more than just one module, that is why it is not possible to create it
   * directly! Call it if overwritten.
   */
  @Override
  public void prepare() {
    InetSocketAddress socketAddress = new InetSocketAddress(host, port);
    channel = Resources.getInstance().get(socketAddress);
  }

  //------------------------------------------------ Object Propertyes Setters.

  /**
   * Set the spinel address of the papouch hw.
   *
   * @param address the address to set
   */
  @Setter("address")
  public void setAddress(int address) {
    this.address = address;
  }

  /**
   * Set the host.
   *
   * @param host the host to set
   */
  @Setter("host")
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Set the port.
   *
   * @param port the port to set
   */
  @Setter("port")
  public void setPort(int port) {
    this.port = port;
  }

  //---------------------------- Cycle Event Listener interface implementation.

  /**
   * Not used.
   */
  @Override
  public void scanEnd() { }

  /**
   * Not used.
   */
  @Override
  public void processingStart() { }

  /**
   * Sends the request for new data.
   */
  @Override
  public void scanStart() {
    // Request for temperature
    if (channel != null) {
      transaction = channel.putRequest(getRequest());
    } else {
      transaction = null;
    }
  }

}
