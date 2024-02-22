package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClient extends AbstractClient {
  private static final ClientLogger logger = new ClientLogger();

  private Socket socket;
  private DataOutputStream outputStream;
  private DataInputStream inputStream;

  public TCPClient(String serverHost, int serverPort) {
    super(serverHost, serverPort);
  }

  @Override
  public void connect() throws IOException {
    try {
      logger.info("Connecting to the socket");
      socket = new Socket(serverHost, serverPort);
      logger.info("Connected to server "+ serverHost+":" +serverPort);
    } catch (IOException e) {
      logger.error("Error connecting to server "+serverHost+":"+ serverPort+"-"+ e.getMessage());
      throw e;
    }
  }

  @Override
  public void disconnect() {
    logger.info("Calling disconnect in the method");
    try {
      if (outputStream != null) {
        outputStream.close();
        logger.info("Output stream closed");
      }
      if (inputStream != null) {
        inputStream.close();
        logger.info("Input stream closed");
      }
      if (socket != null) {
        socket.close();
        logger.info("Socket closed");
      }
      logger.info("Disconnected from server "+ serverHost+":"+ serverPort);
    } catch (IOException e) {
      logger.error("Error disconnecting from server "+serverHost+":"+ serverPort+"-"+ e.getMessage());
    }
  }

  @Override
  public void sendRequest(String request) throws IOException {
    try {
      // Calculate checksum for request string
      long checksum = calculateChecksum(request);
      outputStream = new DataOutputStream(socket.getOutputStream());
      outputStream.writeLong(checksum);
      outputStream.writeUTF(request);
      outputStream.flush();
      logger.info("Request sent to server "+ serverHost+":"+ serverPort);
    } catch (IOException e) {
      logger.error("Error sending request to server " +serverHost+":"+ serverPort+"-"+ e.getMessage());
      throw e;
    }
  }

  @Override
  public String receiveResponse() throws IOException {
    try {
      inputStream = new DataInputStream(socket.getInputStream());
      String response = inputStream.readUTF();
      logger.info("Response received from server "+serverHost+":"+serverPort+" Response: "+ response);
      return response;
    } catch (IOException e) {
      logger.error("Error receiving response from server " +serverHost+ ":"  + serverPort +" error:" + e.getMessage());
      throw e;
    }
  }
}
