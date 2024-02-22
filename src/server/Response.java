package server;

public class Response {
  private String message;
  private boolean success;

  // Constructor
  public Response(String message, boolean success) {
    this.message = message;
    this.success = success;
  }

  // Getter methods
  public String getMessage() {
    return message;
  }

  public boolean isSuccess() {
    return success;
  }
}
