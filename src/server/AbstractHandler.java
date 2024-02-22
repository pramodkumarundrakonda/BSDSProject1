package server;

import java.util.zip.CRC32;

/**
 * AbstractHandler class defines the structure for handling client requests.
 * It provides methods to calculate checksum, parse requests, and perform operations on the key-value store.
 */
public abstract class AbstractHandler implements Runnable {
  protected KeyValue keyValueStore;
  private static final ServerLogger logger = new ServerLogger();

  /**
   * Constructs an AbstractHandler with the specified key-value store.
   *
   * @param keyValueStore The key-value store to be used for handling requests.
   */
  public AbstractHandler(KeyValue keyValueStore) {
    this.keyValueStore = keyValueStore;
  }

  /**
   * Sends a response back to the client.
   *
   * @param response The response message to be sent.
   */
  public abstract void sendResponse(String response);

  /**
   * Executes the handler logic when the handler thread is started.
   */
  public abstract void run();

  /**
   * Calculates the CRC32 checksum for the given data string.
   *
   * @param data The data string for which the checksum is calculated.
   * @return The CRC32 checksum value.
   */
  public static long calculateChecksum(String data) {
    CRC32 crc32 = new CRC32();
    crc32.update(data.getBytes());
    return crc32.getValue();
  }

  /**
   * Handles the incoming request by parsing the request string,
   * extracting the operation and data, and performing the corresponding action on the key-value store.
   *
   * @param requestData The request data string received from the client.
   */
  protected void handleRequest(String requestData) {
    // Parse the request and extract the operation (PUT, GET, DELETE) and data (key, value)
    String requestString = requestData.trim();
    String[] parts = requestString.split(" ");
    String operation = parts[0];
    String key = parts[1];
    String value = parts.length > 2 ? parts[2] : null;

    try {
      switch (operation) {
        case "PUT":
          int putValue = Integer.parseInt(value);
          keyValueStore.put(key, putValue);
          sendResponse("Inserted Key & value using PUT: " + "Key: " + key + " Value: " + putValue);
          logger.info("Inserted Key & value using PUT: " + "Key: " + key + " Value: " + putValue);
          break;
        case "GET":
          Integer retrievedValue = keyValueStore.get(key);
          sendResponse("Retrieved Key value using GET: " + "Key: " + key + " Value: " + retrievedValue);
          logger.info("Retrieved Key value using GET: " + "Key: " + key + " Value: " + retrievedValue);
          break;
        case "DELETE":
          keyValueStore.delete(key);
          sendResponse("Deletion of  Key Successful: " + "Key: " + key);
          logger.info("Deletion of  Key Successful: " + "Key: " + key);
          break;
        default:
          // Handle invalid operation
          sendResponse("Invalid operation. Must be one of (PUT, GET, DELETE)");
          logger.error("Invalid operation. Must be one of (PUT, GET, DELETE)");
          break;
      }
    } catch (Exception e) {
      // Handle exceptions
      e.printStackTrace();
      sendResponse("Error processing request: " + e.getMessage());
      logger.error("Error processing request: " + e.getMessage());
    }
  }
}
