package server;

import java.util.HashMap;
import java.util.Map;

public class KeyValue {
  private Map<String, Integer> keyValueStore;

  public KeyValue() {
    keyValueStore = new HashMap<>();
  }

  // Method to insert a key-value pair into the store
  public void put(String key, int value) {
    keyValueStore.put(key, value);
  }

  // Method to retrieve the value associated with a key
  public Integer get(String key) {
    return keyValueStore.get(key);
  }

  // Method to delete a key from the store
  public void delete(String key) {
    keyValueStore.remove(key);
  }
}
