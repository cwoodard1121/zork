package zork.enemies;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import datatypes.Location;
import zork.Effects;
import zork.Inventory;
import zork.Move;
import zork.Room;
import zork.Constants.EntityConstants;

public class HomelessGuy extends Enemy {


    public HomelessGuy(Location location, Room currentRoom, int health, Inventory inventory, ArrayList<Move> moves, int money, int speed, String name){
        super(location, currentRoom, health, inventory, moves, money, name);
        
        Location notRealLocation = new Location(2, 3);
        setLocation(notRealLocation);
        Room notRealRoom = new Room();
        setCurrentRoom(notRealRoom);
        Effects poison = new Effects("poison", 2, 5, 0);
        setHealth(15);
        Weapon glove = new Weapon( 10, "dirty gloves", false, 2, poison);
        Inventory i = new Inventory(EntityConstants.MAX_INVENTORY_WEIGHT);
        i.addItem(glove);
        setInventory(i);
        Move punch = new Move("punch", 0, null);
        ArrayList<Move> attacks = new ArrayList<>(); attacks.add(punch);
        setMoves(attacks);
        setMoney(2.50);
        setSpeed(8);

    }
}
