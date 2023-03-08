package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Room;

public class Player extends Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private ArrayList<Item> inventory;
    private ArrayList<String> moves;
    private int primeCounter;

    public Player(Location location, Room currentRoom, int health, String weapon, ArrayList<Item> inventory, int primeCounter){
        super(location, currentRoom, health, inventory);
        this.weapon = weapon;
        this.primeCounter = primeCounter;
        this.moves = move;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Room getRoom() {
        return this.currentRoom;
    }

    public void changeRoom(Room r) {
        this.currentRoom = r;
    } 

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item i) {
        this.inventory.add(i);
    }

    public void removeItem(double id) {
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).getId() == id) inventory.remove(i);
        }
    }

    public int getPrimeCounter() {
        return primeCounter;
    }

    public void setPrimeCounter(int primeCounter) {
        this.primeCounter = primeCounter;
    }

    
}
