package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Moves;
import zork.Room;

public class Enemy extends Entity {
    private int health;
    private ArrayList<Moves> moves;
    private ArrayList<Item> inventory;

    public Enemy(Location location, Room currentRoom, int health, ArrayList<Item> inventory, ArrayList<Moves> moves){
        super(location, currentRoom, health, inventory);
        this.moves = moves;
    }


    public int getHealth(){
        return health;
    }


    public ArrayList<Moves> getMoves(){
        return moves;
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }

}
