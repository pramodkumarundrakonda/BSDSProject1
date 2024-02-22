package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHandler extends AbstractHandler {

  private final int port;
  private DatagramSocket socket;
  private int clientPort;
  private InetAddress address;

  private static final ServerLogger logger = new ServerLogger();


  public UDPHandler(int port, KeyValue keyValueStore) {
    super(keyValueStore);
    this.port = port;
  }

  @Override
  public void run() {
    try {
      socket = new DatagramSocket(port);
      logger.info("UDP Server started on port " + port);
      logger.info("Waiting for new connections...");

      while (true) {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        address = packet.getAddress();
        clientPort = packet.getPort();
        logger.info("Received request from client: " + address + ":" + clientPort);

        // Handle the client request
        handleClientRequest(packet);
      }
    } catch (IOException e) {
      logger.error("Error in UDP server: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (socket != null) {
        socket.close();
        logger.info("UDP Server socket closed");
      }
    }
  }

  private void handleClientRequest(DatagramPacket packet) {
    try {
      logger.info("Handling client request");
      // Handle the request
      handleRequest(new String(packet.getData(), 0, packet.getLength()));

    } catch (Exception e) {
      logger.error("Error handling client request: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void sendResponse(String response) {
    try {
      byte[] responseData = response.getBytes();
      DatagramPacket packet = new DatagramPacket(responseData, responseData.length);
      packet.setAddress(address);
      packet.setPort(clientPort);
      socket.send(packet);
      logger.info("Response sent to client " + response);
    } catch (IOException e) {
      logger.error("Error sending response to client: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
