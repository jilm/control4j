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
import cz.control4j.InputModule;
import cz.lidinsky.spinel.PhysicalPeer;
import cz.lidinsky.spinel.SpinelMessage;
import cz.lidinsky.spinel.Transaction;
import cz.lidinsky.tools.reflect.Setter;
import java.net.InetSocketAddress;

/**
 */
abstract class IMPapouch extends InputModule implements ICycleEventListener {

  /**
   * Spinel address of the module.
   */
  protected int address;
  protected PhysicalPeer channel;

  @Override
  public void prepare() {
    InetSocketAddress socketAddress = new InetSocketAddress(host, port);
    channel = Resources.getInstance().get(socketAddress);
  }

  protected SpinelMessage request;
  protected Transaction transaction;

  /**
   * send message
   */
  @Override
  public void scanEnd() {
    if (request != null) {
      transaction = channel.putRequest(request);
    }
  }

  /**
   * Sends request for new data.
   */
  @Override
  public void scanStart() { }

  protected SpinelMessage responseMessage;

  /**
   * Gets the response message and interprets is.
   */
  @Override
  public void processingStart() { }

  /**
   * @param address the address to set
   */
  @Setter("address")
  public void setAddress(int address) {
    this.address = address;
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

  /**
   * @param port the port to set
   */
  @Setter("port")
  public void setPort(int port) {
    this.port = port;
  }

}
