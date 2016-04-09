package cz.control4j.modules.papouch;

import cz.lidinsky.spinel.PhysicalPeer;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


class Resources {

  private static Resources instance;

  static Resources getInstance() {
    if (instance == null) {
      instance = new Resources();
    }
    return instance;
  }

  private Resources() {
    clients = new HashMap<>();
  }

  private final Map<InetSocketAddress, PhysicalPeer> clients;

  PhysicalPeer get(InetSocketAddress address) {
    PhysicalPeer client = clients.get(address);
    if (client == null) {
      client = new PhysicalPeer(address.getHostString(), address.getPort());
      clients.put(address, client);
    }
    return client;
  }


}
