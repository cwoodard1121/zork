package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Room;

public class Npc extends Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private ArrayList<Item> inventory;
    private String dialouge;

    public Npc(Location location,Room currentRoom, int health, ArrayList<Item> inventory, String dialogue){
        super(location, currentRoom, health, inventory);
        this.dialouge = dialogue;
    }

    public void printDialogue(){
        System.out.println(dialouge);
    }
}
