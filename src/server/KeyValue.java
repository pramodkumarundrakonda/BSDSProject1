package server;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a key-value store implementation.
 */
public class KeyValue {
  private Map<String, Integer> keyValueStore;

  /**
   * Constructs a new KeyValue object with an empty key-value store.
   */
  public KeyValue() {
    keyValueStore = new HashMap<>();
  }

  /**
   * Inserts a key-value pair into the store.
   *
   * @param key   The key to be inserted.
   * @param value The value associated with the key.
   */
  public void put(String key, int value) {
    keyValueStore.put(key, value);
  }

  /**
   * Retrieves the value associated with a given key.
   *
   * @param key The key for which to retrieve the value.
   * @return The value associated with the key, or null if the key is not found.
   */
  public Integer get(String key) {
    return keyValueStore.get(key);
  }

  /**
   * Deletes a key from the store.
   *
   * @param key The key to be deleted.
   */
  public void delete(String key) {
    keyValueStore.remove(key);
  }
}
