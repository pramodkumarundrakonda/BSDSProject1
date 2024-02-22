package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHandler extends AbstractHandler {

  private final int port;
  private ServerSocket serverSocket;
  private Socket clientSocket;

  private static final ServerLogger logger = new ServerLogger();

  public TCPHandler(int port, KeyValue keyValueStore) {
    super(keyValueStore);
    this.port = port;
  }

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      logger.info("TCP Server started on port " + port);
      logger.info("Waiting for new connections...");
      clientSocket = serverSocket.accept();
      logger.info("New connection from " + clientSocket.getInetAddress());

      while (true) {
        // Handle the client request
        handleClientRequest(clientSocket);
      }
    } catch (IOException e) {
      logger.error("Error in TCP server: " + e.getMessage());
      e.printStackTrace();
    } finally {
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
