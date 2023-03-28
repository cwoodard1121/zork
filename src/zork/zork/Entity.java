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

    public void create() {
        
    }




}
