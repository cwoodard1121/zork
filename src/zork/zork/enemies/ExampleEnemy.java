package zork.enemies;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import datatypes.Location;
import zork.Effects;
import zork.Inventory;
import zork.Room;
import zork.Constants.EntityConstants;

public class ExampleEnemy extends Enemy {


    public ExampleEnemy(Location location, Room currentRoom, int health, Inventory inventory, int money, int speed, String name){
        super(location, currentRoom, health, inventory, money, name);
        
        // Location notRealLocation = new Location(2, 3);
        // setLocation(notRealLocation);
        // Room notRealRoom = new Room();
        // setCurrentRoom(notRealRoom);
     
        Weapon glove = new Weapon( 10, "dirty gloves", false, 2, new Effects("Poison", 2, 4, 1, 0));
        Inventory i = new Inventory(EntityConstants.MAX_INVENTORY_WEIGHT);
        i.addItem(glove);
        setInventory(i);
        setMoney(2.50);
        setSpeed(8);

    }
}
