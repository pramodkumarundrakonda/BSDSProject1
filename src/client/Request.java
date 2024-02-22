package client;
import java.io.Serializable;

public class Request implements Serializable {
  private static final long serialVersionUID = 1L;

  private String operation;
  private String key;
  private String value;

  public Request(String operation, String key, String value) {
    this.operation = operation;
    this.key = key;
    this.value = value;
  }

  public String getOperation() {
    return operation;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Request{" +
            "operation='" + operation + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
  }
}
