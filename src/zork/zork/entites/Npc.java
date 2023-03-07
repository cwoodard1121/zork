package zork.entites;

import java.util.ArrayList;

import zork.Entity;
import zork.Item;

public class Npc extends Entity {
    private int x;
    private int y;
    private int health;
    private ArrayList<Item> inventory;
    private String dialouge;

    public Npc(int x, int y, int health, ArrayList<Item> inventory, String dialogue){
        super(x, y, health, inventory);
        this.dialouge = dialogue;
    }

    public void printDialogue(){
        System.out.println(dialouge);
    }
}
