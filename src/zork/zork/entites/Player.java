package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Player extends Entity {
    private int x;
    private int y;
    private int health;
    private ArrayList<Item> inventory;
    private ArrayList<String> moves;
    private int primeCounter;

    public Player(int x, int y, int health, ArrayList<Item> inventory, int primeCounter, ArrayList<String> move){
        super(x, y, health, inventory);
        this.primeCounter = primeCounter;
        this.moves = move;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
            if(inventory.get(i).getId() == id) inventory.remove(inventory);
        }
    }

    public int getPrimeCounter() {
        return primeCounter;
    }

    public void setPrimeCounter(int primeCounter) {
        this.primeCounter = primeCounter;
    }

    
}
