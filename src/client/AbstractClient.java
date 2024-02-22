package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CRC32;

public abstract class AbstractClient {
  protected String serverHost;
  protected int serverPort;
  protected Socket socket;
  protected boolean responseReceived;
  private static final ClientLogger logger = new ClientLogger();

  public AbstractClient(String serverHost, int serverPort) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
  }

  public abstract void connect() throws IOException;

  public abstract void disconnect();

  public abstract void sendRequest(String request) throws IOException;

  public abstract String receiveResponse() throws IOException, ClassNotFoundException;

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
                parts[0].equalsIgnoreCase("GET")) && parts.length == 2) || parts.length == 3){
          String response = sendWithTimeout(input);
          logger.info("Response received from server: "+ response);
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

  // Calculate CRC32 checksum for a given string
  public static long calculateChecksum(String data) {
    CRC32 crc32 = new CRC32();
    crc32.update(data.getBytes());
    return crc32.getValue();
  }

  protected String sendWithTimeout(String request) throws IOException {
    responseReceived = false;
    sendRequest(request);
    logger.info("Request sent to server: "+ request);

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
    timer.schedule(timeoutTask, 5000); // Timeout after 5 seconds

    String response = "";

    try {
      response = receiveResponse();
      responseReceived = true;
    } catch (IOException | ClassNotFoundException e) {
      logger.error("An error occurred while receiving response: "+ e.getMessage());
      e.printStackTrace();
    } finally {
      timer.cancel();
    }
    return response;
  }
}
