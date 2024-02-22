package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends AbstractClient {
  private static final ClientLogger logger = new ClientLogger();

  private DatagramSocket socket;

  public UDPClient(String serverHost, int serverPort) {
    super(serverHost, serverPort);
  }

  @Override
  public void connect() throws IOException {
    socket = new DatagramSocket();
    logger.info("UDP client connected to server "+ serverHost+":"+ serverPort);
  }

  @Override
  public void disconnect() {
    if (socket != null && !socket.isClosed()) {
      socket.close();
      logger.info("UDP client disconnected");
    }
  }

  @Override
  public void sendRequest(String request) throws IOException {
    try {
      byte[] requestData = request.getBytes();
      InetAddress serverAddress = InetAddress.getByName(serverHost);
      DatagramPacket packet = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
      socket.send(packet);
      logger.info("Request sent to server "+ serverHost+":"+ serverPort);
    } catch (IOException e) {
      logger.error("Error sending request to server  "+serverHost+":"+ serverPort+"-"+ e.getMessage());
      throw e;
    }
  }

  @Override
  public String receiveResponse() throws IOException {
    try {
      byte[] responseData = new byte[1024];
      DatagramPacket packet = new DatagramPacket(responseData, responseData.length);
      socket.receive(packet);
      logger.info("Response received from server "+ serverHost+":"+ serverPort);

      return new String(packet.getData(), 0, packet.getLength());
    } catch (IOException e) {
      logger.error("Error receiving response from server "+serverHost+":"+ serverPort+"-"+ e.getMessage());
      throw e;
    }
  }
}
