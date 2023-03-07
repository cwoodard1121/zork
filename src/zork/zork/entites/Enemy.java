package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Enemy extends Entity {
    private int x;
    private int y;
    private int health;
    private int damage;
    private String weapon;
    private ArrayList<Item> inventory;

    public Enemy(int x, int y, int health, int damage, String weapon, ArrayList<Item> inventory){
        super(x, y, health, inventory);
        this.damage = damage;
        this.weapon = weapon;
        
    }

    public void fight(){

    }

}
