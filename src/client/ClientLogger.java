package client;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientLogger {
  private static final Logger logger = LogManager.getLogger(ClientLogger.class);

  public void info(String message) {
    logger.info(message);
  }

  public void warning(String message) {
    logger.warn(message);
  }

  public void error(String message) {
    logger.error(message);
  }

  public void severe(String message) {
    logger.fatal(message);
  }



  public void info(String s, String toString) {
  }

  public void warn(String s) {
  }
}
