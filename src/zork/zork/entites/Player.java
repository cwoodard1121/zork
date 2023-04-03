package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;
import zork.Move;
import zork.Room;
import zork.items.Prime;

public class Player extends Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private Inventory inventory;
    private ArrayList<Move> move;
    private int primeCounter;
    private int speed;

    public Player(Location location, Room currentRoom, int health, int damage, Inventory inventory, int primeCounter, ArrayList<Move> move){
        super(location, currentRoom, health, inventory);
        this.primeCounter = primeCounter;
        this.move = move;
        this.speed = speed;
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

    public int getSpeed() {
        return speed;
    }

    public ArrayList<Move> getMove() {
        return move;
    }

    public void setMove(ArrayList<Move> move) {
        this.move = move;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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


    public Inventory getInventory() {
       return inventory;
    }

    public int getPrimeCounter() {
        return primeCounter;
    }

    public void calculatePrimeCounter() {
        inventory.getItems().forEach(item -> {
            if(item instanceof Prime) primeCounter++;
        });
    }

    public void setPrimeCounter(int primeCounter) {
        this.primeCounter = primeCounter;
    }

    
}
