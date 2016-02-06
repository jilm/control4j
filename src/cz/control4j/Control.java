/*
 */

package cz.control4j;

import java.util.logging.Logger;

/**
 * An entry point of the control4j application.
 */
public class Control {

  private static Control instance;

  private ControlLoop engine;

  private Control() {}

  private static Logger applicationLogger;

  /**
   * Returns application logger.
   *
   * @return application logger.
   */
  public static Logger getLogger() {
    return applicationLogger;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // application logger
    applicationLogger = Logger.getLogger("cz.control4j");
    // create instance
    instance = new Control();
    // create and run control loop
    // TODO code application logic here
  }

}
