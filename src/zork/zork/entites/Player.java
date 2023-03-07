package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Player extends Entity {
    private int x;
    private int y;
    private int health;
    private int damage;
    private ArrayList<Item> inventory;
    private ArrayList<String> moves;
    private int primeCounter;

    public Player(int x, int y, int health, int damage, ArrayList<Item> inventory, int primeCounter, ArrayList<String> move){
        super(x, y, health, inventory);
        this.damage = damage;
        this.primeCounter = primeCounter;
        this.moves = move;
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

    public int numPrime(){
        return primeCounter;
    }

}
