package cz.control4j.application;

import static cz.lidinsky.tools.Validate.notNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * To express a connection between one module output and zero or more module
 * inputs. A connection is based on IOs that points to the same signal object.
 * Once, the connection object is created, signal object is no longer
 * neccessary. IO objects and signals are created by the preprocessor.
 *
 * <p>Each connection object contains exactly one output and may contain zero
 * or more intputs. Each module input or output is either not connected or
 * it is part of exactly one connection object.
 *
 * <p>These connection objects serves as a source for constructing a graph for
 * a module sort and as a source for constructing module input and output maps.
 *
 * <p>Connection object is immutable.
 */
public class Connection {

  //private static int counter = 0;

  /**
   * Inputs that are connected to this signal. Generally, the objects may be
   * duplicate. This collection may be empty.
   */
  private final Collection<IO> consumers;

  /**
   * An output which is a source of data for the inputs. It may not be null.
   */
  private final IO producer;

  /**
   * @param producer
   *            an output that is source of data
   *
   * @param consumers
   *            a collection of all of the inputs that are connected to the
   *            output. May be an empty collection or null which is interpreted
   *            as an empty collection.
   *
   * @throws CommonException
   *            if the producer argument is null
   */
  Connection(IO producer, Collection consumers) {
    this.producer = notNull(producer);

    this.consumers = consumers == null
        ? Collections.emptyList()
        : Collections.unmodifiableCollection(new ArrayList<>(consumers));
  }

  /**
   * Returns a collection of intputs that are connected to the output. It may
   * return an empty collection. The returned collection is immutable.
   *
   * @return an immutable collection of all of the inputs
   */
  public Collection<IO> getConsumers() {
    return consumers;
  }

  /**
   * Returns a producer output for this connection.
   *
   * @return returns the output
   */
  public IO getProducer() {
    return producer;
  }

}
