package zork;

/**
 * Exit
 */
public class Exit extends OpenableObject {
  private String direction;
  private Room adjacentRoom;

  public Exit(String direction, Room adjacentRoom, boolean isLocked, String keyId) {
    super(isLocked, keyId);
    this.direction = direction;
    this.adjacentRoom = adjacentRoom;
  }

  public Exit(String direction, Room adjacentRoom, boolean isLocked, String keyId, Boolean isOpen) {
    super(isLocked, keyId, isOpen);
    this.direction = direction;
    this.adjacentRoom = adjacentRoom;
  }

  public Exit(String direction, Room adjacentRoom) {
    this.direction = direction;
    this.adjacentRoom = adjacentRoom;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public Room getAdjacentRoom() {
    return adjacentRoom;
  }

  public void setAdjacentRoom(Room adjacentRoom) {
    this.adjacentRoom = adjacentRoom;
  }

 
}