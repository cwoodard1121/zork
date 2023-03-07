package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Enemy extends Entity {
    private int x;
    private int y;
    private int health;
    private int damage;
    private ArrayList<String> moves;
    private ArrayList<Item> inventory;

    public Enemy(int x, int y, int health, int damage, String weapon, ArrayList<Item> inventory, ArrayList<String> move){
        super(x, y, health, inventory);
        this.damage = damage;
        this.moves = move;
        
    }

    public static void fight(){
        //when fighting item has to be a weapon 
    }

    public int getHealth(){
        return health;
    }

    public int getDamage(){
        return damage;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public ArrayList<String> getMoves(){
        return moves;
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }

}
