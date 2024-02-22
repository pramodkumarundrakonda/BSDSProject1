package client;

import java.io.IOException;

import static client.AbstractClient.calculateChecksum;

public class KeyValueStoreInitializer {

  private static final ClientLogger logger = new ClientLogger();

  private AbstractClient client;

  public KeyValueStoreInitializer(AbstractClient client) {
    this.client = client;
  }

  public void populateKeyValueStore() {
    logger.info("-------------------Initializing the key-value store with 10 pairs---------------");
    for (int i = 1; i <= 10; i++) {
      String key = "key" + i;
      performPutOperation(key, i);
    }
    logger.info("---------------Initialization Done !!! -------------------------------------------");
  }

  public void performOperations() {
    logger.info("---------------Performing 5 PUT/GET/DELETE Operations ------------------------");
    for (int i = 11; i <= 15; i++) {
      String key = "key" + i;

      // Perform PUT operation
      performPutOperation(key, i);

      // Perform GET operation
      performGetOperation(key);

      // Perform DELETE operation
      performDeleteOperation(key);
    }
  }

  private void performPutOperation(String key, int value) {
    try {
      String request = "PUT " + key + " " + value;
      client.sendRequest(request );
      logger.info("PUT: Key=" + key + ", Value=" + value);
      String response = client.receiveResponse();
      logger.info("Response: " + response);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void performGetOperation(String key) {
    try {
      String request = "GET " + key;
      long checksum = calculateChecksum(request);
      client.sendRequest(request + " " + checksum); // Append checksum to the request
      logger.info("GET: Key=" + key + ", Checksum=" + checksum);
      String response = client.receiveResponse();
      logger.info("Response: " + response);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void performDeleteOperation(String key) {
    try {
      String request = "DELETE " + key;
      long checksum = calculateChecksum(request);
      client.sendRequest(request + " " + checksum); // Append checksum to the request
      logger.info("DELETE: Key=" + key + ", Checksum=" + checksum);
      String response = client.receiveResponse();
      logger.info("Response: " + response);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
