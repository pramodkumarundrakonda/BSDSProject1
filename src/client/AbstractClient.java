package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CRC32;

/**
 * Abstract class representing a client connecting to a server.
 */
public abstract class AbstractClient {
  protected String serverHost;
  protected int serverPort;
  protected Socket socket;
  protected boolean responseReceived;
  private static final ClientLogger logger = new ClientLogger();

  /**
   * Constructor to initialize the client with server host and port.
   * @param serverHost The host address of the server.
   * @param serverPort The port number of the server.
   */
  public AbstractClient(String serverHost, int serverPort) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
  }

  /**
   * Establishes connection to the server.
   * @throws IOException if an I/O error occurs when establishing the connection.
   */
  public abstract void connect() throws IOException;

  /**
   * Disconnects the client from the server.
   */
  public abstract void disconnect();

  /**
   * Sends a request to the server.
   * @param request The request to be sent to the server.
   * @throws IOException if an I/O error occurs when sending the request.
   */
  public abstract void sendRequest(String request) throws IOException;

  /**
   * Receives response from the server.
   * @return The response received from the server.
   * @throws IOException if an I/O error occurs when receiving the response.
   * @throws ClassNotFoundException if the class of the received object cannot be found.
   */
  public abstract String receiveResponse() throws IOException, ClassNotFoundException;

  /**
   * Starts the client and interacts with the server.
   */
  public void start() {
    try {
      connect();

      // Initialize and populate key-value store
      KeyValueStoreInitializer initializer = new KeyValueStoreInitializer(this);
      initializer.populateKeyValueStore();
      initializer.performOperations();

      Scanner scanner = new Scanner(System.in);
      String input;

      do {
        logger.info("Waiting for user input...");
        System.out.println("Please Enter PUT/GET/DELETE Key Value:");
        input = scanner.nextLine();

        String[] parts = input.split(" ");
        if (((parts[0].equalsIgnoreCase("DELETE") ||
                parts[0].equalsIgnoreCase("GET")) && parts.length == 2) || parts.length == 3) {
          String response = sendWithTimeout(input);
          logger.info("Response received from server: " + response);
        } else {
          System.out.println("Invalid input format. Please follow PUT/GET/DELETE Key Value format.");
          logger.warn("Invalid input format received from user: " + input);
        }
      } while (true);

    } catch (IOException e) {
      logger.error("An error occurred: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Calculates CRC32 checksum for a given string.
   * @param data The string for which the checksum needs to be calculated.
   * @return The calculated CRC32 checksum value.
   */
  public static long calculateChecksum(String data) {
    CRC32 crc32 = new CRC32();
    crc32.update(data.getBytes());
    return crc32.getValue();
  }

  /**
   * Sends a request to the server with a timeout mechanism.
   * @param request The request to be sent to the server.
   * @return The response received from the server.
   * @throws IOException if an I/O error occurs when sending or receiving data.
   */
  protected String sendWithTimeout(String request) throws IOException {
    responseReceived = false;
    sendRequest(request);
    logger.info("Request sent to server: " + request);

    TimerTask timeoutTask = new TimerTask() {
      @Override
      public void run() {
        if (!responseReceived) {
          logger.error("Server response timed out.");
          disconnect();
        }
      }
    };

    Timer timer = new Timer(true);
    timer.schedule(timeoutTask, 60000); // Timeout after 60 seconds

    String response = "";

    try {
      response = receiveResponse();
      responseReceived = true;
    } catch (IOException | ClassNotFoundException e) {
      logger.error("An error occurred while receiving response: " + e.getMessage());
      e.printStackTrace();
    } finally {
      timer.cancel();
    }
    return response;
  }
}
