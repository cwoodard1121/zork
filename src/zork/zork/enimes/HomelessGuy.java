package zork.enimes;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import datatypes.Location;
import zork.Entity;
import zork.Item;
import zork.Moves;
import zork.Room;

public class HomelessGuy extends Enemy {

    public HomelessGuy(Location location, Room currentRoom, int health, ArrayList<Item> inventory, ArrayList<Moves> moves, int money, int speed){
        super(location, currentRoom, health, inventory, moves, money, speed);
    }

    public void create() {
       //needs location
       //needs current room
        setHealth(15);
        Weapon glove = new Weapon(10, "dirty gloves", false, 2);
        ArrayList<Item> stuff = new ArrayList<>(); stuff.add(glove);
        setInventory(stuff);
        Moves punch = new Moves("punch", 0, null);
        ArrayList<Moves> attacks = new ArrayList<>(); attacks.add(punch);
        setMoves(attacks);
        setMoney(2.50);
        setSpeed(8);

    }
    
}
