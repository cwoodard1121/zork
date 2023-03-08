package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Room;

public class Enemy extends Entity {
    private int health;
    private int damage;
    private String weapon;
    private ArrayList<Item> inventory;

    public Enemy(Location location, Room currentRoom, int health, int damage, String weapon, ArrayList<Item> inventory){
        super(location, currentRoom, health, inventory);
        this.damage = damage;
        this.weapon = weapon;
    }

    public void fight() {

    }

}
