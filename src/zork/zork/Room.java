package zork;

import java.util.ArrayList;

import zork.entites.Enemy;

public class Room {

  private String roomName;
  private String description;
  private ArrayList<Item> groundItems;
  private ArrayList<Enemy> enemies;
  private ArrayList<Exit> exits = new ArrayList<>();
  private boolean isSubway;

  public ArrayList<Exit> getExits() {
    return exits;
  }

  public void setExits(final ArrayList<Exit> exits) {
    this.exits = exits;
  }


  

  /**
   * gets the items on the ground in the room
   * @return
   */
  public ArrayList<Item> getGroundItems() {
    return groundItems;
  }


  /**
   * removes it from the ground
   * @param i
   * @return the item that got removed
   */
  public Room removeFromGround(Item i) {
    groundItems.remove(i);
    return this;
  }

  /**
   * Create a room described "description". Initially, it has no exits.
   * "description" is something like "a kitchen" or "an open court yard".
   */
  public Room(final String description) {
    this.description = description;
    exits = new ArrayList<Exit>();
  }

  public Room() {
    roomName = "DEFAULT ROOM";
    description = "DEFAULT DESCRIPTION";
    exits = new ArrayList<Exit>();
  }

  public void addExit(final Exit exit) {
    exits.add(exit);
  }

  /**
   * Return the description of the room (the one that was defined in the
   * constructor).
   */
  public String shortDescription() {
    return "Room: " + roomName + "\n\n" + description;
  }

  /**
   * Return a long description of this room, on the form: You are in the kitchen.
   * Exits: north west
   */
  public String longDescription() {

    return "Room: " + roomName + "\n\n" + description + "\n" + exitString();
  }

  /**
   * Return a string describing the room's exits, for example "Exits: north west
   * ".
   */
  private String exitString() {
    String returnString = "Exits: ";
    for (final Exit exit : exits) {
      returnString += exit.getDirection() + " ";
    }

    return returnString;
  }

  /**
   * Return the room that is reached if we go from this room in direction
   * "direction". If there is no room in that direction, return null.
   */
  public Room nextRoom(final String direction) {
    try {
      for (final Exit exit : exits) {

        if (exit.getDirection().equalsIgnoreCase(direction)) {

          return exit.getAdjacentRoom();
        }

      }
    } catch (final IllegalArgumentException ex) {
      System.out.println(direction + " is not a valid direction.");
      return null;
    }

    System.out.println(direction + " is not a valid direction.");
    return null;
  }

  /*
   * private int getDirectionIndex(String direction) { int dirIndex = 0; for
   * (String dir : directions) { if (dir.equals(direction)) return dirIndex; else
   * dirIndex++; }
   * 
   * throw new IllegalArgumentException("Invalid Direction"); }
   */
  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(final String roomName) {
    this.roomName = roomName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
