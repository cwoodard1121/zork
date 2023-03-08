package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Room;

public class Enemy extends Entity {
    private int health;
    private ArrayList<String> moves;
    private ArrayList<Item> inventory;


    public static void fight(){
        //when fighting item has to be a weapon 
    }

    public int getHealth(){
        return health;
    }


    public ArrayList<String> getMoves(){
        return moves;
    }

    public Enemy(Location location, Room currentRoom, int health, String weapon, ArrayList<Item> inventory){
        super(location, currentRoom, health, inventory);
        this.weapon = weapon;
    }


    public ArrayList<Item> getInventory(){
        return inventory;
    }

}
