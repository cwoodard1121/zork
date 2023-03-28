package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;
import zork.Moves;
import zork.Room;

public class Enemy extends Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private ArrayList<Item> inventory;
    private ArrayList<Moves> moves;
    private double moneyDroped;


    public Enemy(Location location, Room currentRoom, int health, Inventory inventory, ArrayList<Moves> moves, int money){
        super(location, currentRoom, health, inventory);
        this.moves = moves;
        this.moneyDroped = money;
    }


    public int getHealth(){
        return health;
    }


    public ArrayList<Moves> getMoves(){
        return moves;
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }


    public Location getLocation() {
        return location;
    }


    public void setLocation(Location location) {
        this.location = location;
    }


    public Room getCurrentRoom() {
        return currentRoom;
    }


    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }


    public void setHealth(int health) {
        this.health = health;
    }


    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }


    public void setMoves(ArrayList<Moves> moves) {
        this.moves = moves;
    }

    public double getMoney(){
        return moneyDroped;
    }

    public void setMoney(double d){
        this.moneyDroped = d;
    }

}
