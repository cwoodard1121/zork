package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Player extends Entity {
    private int x;
    private int y;
    private int health;
    private int damage;
    private String weapon;
    private ArrayList<Item> inventory;
    private int primeCounter;

    public Player(int x, int y, int health, int damage, String weapon, ArrayList<Item> inventory, int primeCounter){
        super(x, y, health, inventory);
        this.damage = damage;
        this.weapon = weapon;
        this.primeCounter = primeCounter;
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
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
