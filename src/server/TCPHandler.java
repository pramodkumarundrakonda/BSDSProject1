package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCPHandler class handles TCP connections for the server.
 */
public class TCPHandler extends AbstractHandler {

  private final int port;
  private ServerSocket serverSocket;
  private Socket clientSocket;

  private static final ServerLogger logger = new ServerLogger();

  /**
   * Constructs a TCPHandler with the specified port and key-value store.
   *
   * @param port           The port on which the server will listen.
   * @param keyValueStore  The key-value store to handle the requests.
   */
  public TCPHandler(int port, KeyValue keyValueStore) {
    super(keyValueStore);
    this.port = port;
  }

  /**
   * Starts the TCP server and listens for incoming connections.
   */
  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      logger.info("TCP Server started on port " + port);

      while (true) {
        logger.info("Waiting for new connections...");
        clientSocket = serverSocket.accept();
        logger.info("New connection from " + clientSocket.getInetAddress());

        // Create a new thread to handle each client request
        Thread handlerThread = new Thread(() -> {
          while (true) {
            // Handle the client request
            handleClientRequest(clientSocket);
          }
        });
        handlerThread.start();
      }
    } catch (IOException e) {
      logger.error("Error in TCP server: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Close the server socket when the server is stopped
      try {
        if (serverSocket != null && !serverSocket.isClosed()) {
          serverSocket.close();
          logger.info("TCP Server socket closed");
        }
      } catch (IOException e) {
        logger.error("Error closing TCP server socket: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Handles the client request.
   *
   * @param clientSocket The client socket from which the request is received.
   */
  private void handleClientRequest(Socket clientSocket) {
    if (clientSocket == null || clientSocket.isClosed()) {
      return; // Exit method if client socket is invalid or closed
    }
    try {
      logger.info("Handling client request...");
      // Read request data from the client
      InputStream inputStream = clientSocket.getInputStream();
      DataInputStream dataInputStream = new DataInputStream(inputStream);
      // Read checksum sent by client
      long receivedChecksum = dataInputStream.readLong();
      String requestData =  dataInputStream.readUTF();
      logger.info("receivedChecksum: "+ receivedChecksum);
      logger.info("Request Data: "+ requestData);

      // Calculate checksum of received request string
      long calculatedChecksum = calculateChecksum(requestData);

      // Compare received checksum with calculated checksum
      if (receivedChecksum == calculatedChecksum) {
        // Checksums match, request data is intact
        logger.info("Checksum validation successful. Request received: " + requestData);

        // Process the request here...
      } else {
        // Checksums do not match, request data may be corrupted
        logger.error("Checksum validation failed. Request data may be corrupted.");
      }

      // Handle the request
      handleRequest(requestData);
      logger.info("Client request handled successfully.");

    } catch (IOException e) {
      logger.error("Error handling client request: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Sends a response to the client.
   *
   * @param response The response message to be sent.
   */
  @Override
  public void sendResponse(String response) {
    try {
      logger.info("Sending response to client...");
      // Send the response back to the client
      DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
      outputStream.writeUTF(response);
      //outputStream.flush();
      logger.info("Response sent successfully." + response);
    } catch (IOException e) {
      logger.error("Error sending response to client: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
