package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDPClient class represents a client that communicates with a server using the UDP protocol.
 * It extends the AbstractClient class and implements the methods to connect, send requests, and receive responses.
 */
public class UDPClient extends AbstractClient {
  private static final ClientLogger logger = new ClientLogger();
  private DatagramSocket socket;

  /**
   * Constructs a UDPClient with the specified server host and port.
   *
   * @param serverHost The hostname or IP address of the server.
   * @param serverPort The port number of the server.
   */
  public UDPClient(String serverHost, int serverPort) {
    super(serverHost, serverPort);
  }

  /**
   * Connects the UDP client to the server.
   *
   * @throws IOException if an I/O error occurs when establishing the connection.
   */
  @Override
  public void connect() throws IOException {
    socket = new DatagramSocket();
    logger.info("UDP client connected to server " + serverHost + ":" + serverPort);
  }

  /**
   * Disconnects the UDP client from the server.
   */
  @Override
  public void disconnect() {
    if (socket != null && !socket.isClosed()) {
      socket.close();
      logger.info("UDP client disconnected");
    }
  }

  /**
   * Sends a request to the server using UDP.
   *
   * @param request The request string to be sent to the server.
   * @throws IOException if an I/O error occurs when sending the request.
   */
  @Override
  public void sendRequest(String request) throws IOException {
    try {
      byte[] requestData = request.getBytes();
      InetAddress serverAddress = InetAddress.getByName(serverHost);
      DatagramPacket packet = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
      socket.send(packet);
      logger.info("Request sent to server " + serverHost + ":" + serverPort);
    } catch (IOException e) {
      logger.error("Error sending request to server " + serverHost + ":" + serverPort + "-" + e.getMessage());
      throw e;
    }
  }

  /**
   * Receives a response from the server using UDP.
   *
   * @return The response received from the server.
   * @throws IOException if an I/O error occurs when receiving the response.
   */
  @Override
  public String receiveResponse() throws IOException {
    try {
      byte[] responseData = new byte[1024];
      DatagramPacket packet = new DatagramPacket(responseData, responseData.length);
      socket.receive(packet);
      logger.info("Response received from server " + serverHost + ":" + serverPort);

      return new String(packet.getData(), 0, packet.getLength());
    } catch (IOException e) {
      logger.error("Error receiving response from server " + serverHost + ":" + serverPort + "-" + e.getMessage());
      throw e;
    }
  }
}
