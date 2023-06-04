package zork;

/**
 * Exit
 */
public class Exit extends OpenableObject {
  private String direction;
  private Room adjacentRoom;
  private boolean isExitLocked;
  private String lockedDescription;
  public boolean isLockedDescription = false;

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

  public Exit(String direction, Room adjacentRoom, boolean isExitLocked) {
    this.direction = direction;
    this.adjacentRoom = adjacentRoom;
    this.isExitLocked = isExitLocked;
  }

  public Exit(String direction, Room adjacentRoom, String lockedDescription, boolean isExitLocked) {
    this.direction = direction;
    this.adjacentRoom = adjacentRoom;
    this.isExitLocked = isExitLocked;
    this.lockedDescription = lockedDescription;
    isLockedDescription = true;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getLockedDescription() {
    return lockedDescription;
  }

  public void setLockedDescription(String lockedDescription) {
    this.lockedDescription = lockedDescription;
  }

  public boolean hasLockedDescription() {
    return isLockedDescription;
  }
  public Room getAdjacentRoom() {
    return adjacentRoom;
  }

  public void setAdjacentRoom(Room adjacentRoom) {
    this.adjacentRoom = adjacentRoom;
  }

  public void setIsExitLocked(boolean Locked) {
    isExitLocked = Locked;
  }

  public boolean getIsExitLocked() {
    return isExitLocked;
  }


 
}