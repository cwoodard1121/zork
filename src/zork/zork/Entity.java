package zork;

import java.util.ArrayList;

import datatypes.Location;
import zork.Constants.EntityConstants;

public class Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private Inventory inventory = new Inventory(EntityConstants.MAX_INVENTORY_WEIGHT);

    public Entity(Location location, Room currentRoom, int health, Inventory inventory){
        this.currentRoom = currentRoom;
        this.location = location;
        this.health = health;
        this.inventory = inventory;
    }

    public Entity(Location location, Room currentRoom, int health) {
        this.currentRoom = currentRoom;
        this.location = location;
        this.health = health;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void create() {
        
    }




}
