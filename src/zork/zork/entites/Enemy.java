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

    public Enemy(Location location, Room currentRoom, int health, ArrayList<Item> inventory, ArrayList<String> moves){
        super(location, currentRoom, health, inventory);
        this.moves = moves;
    }


    public int getHealth(){
        return health;
    }


    public ArrayList<String> getMoves(){
        return moves;
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }

}
