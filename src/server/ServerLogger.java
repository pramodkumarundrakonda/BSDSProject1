package server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerLogger {
  private static final Logger logger = LogManager.getLogger(ServerLogger.class);

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
}
