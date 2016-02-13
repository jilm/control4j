package cz.control4j.application;

import cz.control4j.Module;
import static cz.lidinsky.tools.Validate.notBlank;
import static cz.lidinsky.tools.Validate.notNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * To express a connection between one output and many inputs.
 */
public class Connection extends IO {

  private static int counter = 0;

  /**
   * Inputs that are connected to this signal. Generally, the objects may be
   * duplicate.
   */
  private final Collection<IO> consumers;

  /**
   * @param output
   *
   * @throws CommonException
   *            if the signal parameter is null
   */
  Connection(Module module, String key) {
    super(module, notBlank(key));
    consumers = new ArrayList<>();
    this.pointer = counter++;
  }

  void addConsumer(IO consumer) {
    consumers.add(notNull(consumer));
    consumer.setPointer(pointer);
  }

  Collection<IO> getConsumers() {
    return consumers;
  }

}
