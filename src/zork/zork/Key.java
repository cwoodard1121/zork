package zork;

public class Key extends Item {
  private String keyId;

  public Key(String keyId, String keyName, int weight) {
    super(weight, keyName, false, null, false);
    this.keyId = keyId;
  }

  public String getKeyId() {
    return keyId;
  }
}
