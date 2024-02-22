package client;

/**
 * The main class for the client application.
 * It starts the appropriate client based on the specified protocol.
 */
public class ClientApp {
  private static final ClientLogger logger = new ClientLogger();

  /**
   * The entry point of the client application.
   * @param args Command line arguments: serverHost, serverPort, protocol.
   */
  public static void main(String[] args) {
    if (args.length < 3) {
      logger.error("Usage: java ClientApp <serverHost> <serverPort> <protocol>");
      return;
    }

    String serverHost = args[0];
    int serverPort = Integer.parseInt(args[1]);
    String protocol = args[2].toUpperCase();

    AbstractClient client;

    switch (protocol) {
      case "TCP":
        logger.info("Starting TCP client...");
        client = new TCPClient(serverHost, serverPort);
        break;
      case "UDP":
        logger.info("Starting UDP client...");
        client = new UDPClient(serverHost, serverPort);
        break;
      default:
        logger.error("Invalid protocol. Please specify TCP or UDP.");
        return;
    }
    client.start();
  }
}
