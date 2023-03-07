package zork;

import java.util.ArrayList;

public class Entity {
    private int x;
    private int y;
    private int health;
    private ArrayList<Item> inventory;

    public Entity(int x, int y, int health, ArrayList<Item> inventory){
        this.x = x;
        this.y = y;
        this.health = health;
        this.inventory = inventory;
    }

}
