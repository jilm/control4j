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

package cz.control4j.modules;

import cz.control4j.InputModule;
import cz.control4j.RuntimeException;
import cz.control4j.Signal;
import cz.lidinsky.tools.dispatch.Client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author jilm
 */
public class IMExport extends InputModule {

  private boolean running = false;

  public IMExport() {
    ids = new ArrayList<>();
  }

  private final List<String> ids;

  @Override
  public int getInputIndex(String key) {
    if (ids.contains(key)) {
      return ids.indexOf(key);
    } else {
      ids.add(key);
      return ids.size() - 1;
    }
  }

  private Client client;

  @Override
  public void prepare() {
    try {
      client = new Client("localhost", 12349);
      client.start();
      running = true;
    } catch (IOException ex) {
      Logger.getLogger(IMExport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void put(Signal[] input, int inputLength) throws RuntimeException {
    if (running) {
      try {
        client.send(toJSON(input, inputLength));
      } catch (IOException ex) {
        Logger.getLogger(IMExport.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private JSONObject toJSON(Signal signal) {
    if (signal == null || !signal.isValid()) {
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

  private JSONObject toJSON(Signal[] signals, int length) {
    JSONObject result = new JSONObject();
    for (int i=0; i<length; i++) {
      result.put(ids.get(i), toJSON(signals[i]));
    }
    return result;
  }

}
