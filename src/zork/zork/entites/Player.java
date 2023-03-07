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
}
