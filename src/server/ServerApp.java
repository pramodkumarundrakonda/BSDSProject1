package server;

/**
 * Main class for the server application.
 */
public class ServerApp {
  private static final ServerLogger logger = new ServerLogger();

  /**
   * Main method to start the server application.
   *
   * @param args Command-line arguments containing the port number and server type.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      logger.error("Insufficient arguments. Usage: java ServerApp <Port> <serverType>");
      return;
    }

    int port = Integer.parseInt(args[0]);
    String serverType = args[1].toUpperCase();

    KeyValue keyValueStore = new KeyValue();

    if ("TCP".equals(serverType)) {
      logger.info("Starting TCP Server...");
      TCPHandler tcpHandler = new TCPHandler(port, keyValueStore);
      Thread tcpThread = new Thread(tcpHandler);
      tcpThread.start();
    } else if ("UDP".equals(serverType)) {
      logger.info("Starting UDP Server...");
      UDPHandler udpHandler = new UDPHandler(port, keyValueStore);
      Thread udpThread = new Thread(udpHandler);
      udpThread.start();
    } else {
      logger.error("Invalid server type. Please specify TCP or UDP.");
    }
  }
}
