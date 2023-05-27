package zork.entites;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Room;

public class Npc extends Entity {
    private String dialouge;

    public Npc(Location location,Room currentRoom, int health, Inventory inventory, String dialogue){
        super(location, currentRoom, health, inventory);
        this.dialouge = dialogue;
    }

    public void printDialogue(){
        System.out.println(dialouge);
    }
}
