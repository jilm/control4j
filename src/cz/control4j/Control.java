/*
 */

package cz.control4j;

import cz.control4j.application.Preprocessor;
import cz.control4j.application.ScopeHandler;
import java.util.logging.Logger;

/**
 * An entry point of the control4j application. This object is a singleton.
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
  public static void main(String[] args) throws Exception {
    // application logger
    applicationLogger = Logger.getLogger("cz.control4j");
    // create an instance
    instance = new Control();
    // load application
    String filename = args[0];
    instance.loadApplication(filename);
    // create and run control loop

    // TODO code application logic here
  }

  private void loadApplication(String filename) throws Exception {
    // load application
    applicationLogger.info(
        String.format("Going to load an application from the file: %s", filename));
    cz.control4j.application.c4j.C4j2Control translator
        = new cz.control4j.application.c4j.C4j2Control();
    cz.control4j.application.c4j.XMLHandler c4jHandler
        = new cz.control4j.application.c4j.XMLHandler(new ScopeHandler());
    c4jHandler.setDestination(translator);
    cz.lidinsky.tools.xml.XMLReader reader
        = new cz.lidinsky.tools.xml.XMLReader();
    reader.addHandler(c4jHandler);
    reader.load(new java.io.File(filename));
    // translate application
    applicationLogger.info("Going to translate the application...");
    Preprocessor preprocessor = new Preprocessor();
    translator.process(preprocessor);
    // preprocess application
    applicationLogger.info("Going to preprocess the application...");
    preprocessor.process();
  }

}
