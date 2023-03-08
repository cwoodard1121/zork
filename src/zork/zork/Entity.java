package zork;

import java.util.ArrayList;

import datatypes.Location;

public class Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private ArrayList<Item> inventory;

    public Entity(Location location, Room currentRoom, int health, ArrayList<Item> inventory){
        this.currentRoom = currentRoom;
        this.location = location;
        this.health = health;
        this.inventory = inventory;
    }




}
