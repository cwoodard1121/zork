package zork.entites;

import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;

import zork.Room;
import zork.items.Prime;
import zork.items.Weapon;

public class Player extends Entity {
    private Location location;
    private Room currentRoom;
    private int health;
    private Inventory inventory;

    private int primeCounter;
    private int speed;
    private boolean isInMoveMenu;
 
    private boolean isInWeaponMenu;
    private Weapon currentWeapon;
    private String name;
    

    

    private boolean isCurrentMove;

    public Player(Location location, Room currentRoom, int health, Inventory inventory, int primeCounter, String name){
        super(location, currentRoom, health, inventory);
        this.primeCounter = primeCounter;
        
        this.name = name;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

   

    public Weapon getCurrentWeapon(){
        return currentWeapon;
    }
    

    public String getName() {
        return name;
    }

    public boolean isInWeaponMenu() {
        return isInWeaponMenu;
    }

    public void setInWeaponMenu(boolean isInWeaponMenu) {
        this.isInWeaponMenu = isInWeaponMenu;
    }

   


    public boolean isCurrentMove() {
        return isCurrentMove;
    }


    public void setIsCurrentMove(boolean isCurrentMove) {
        this.isCurrentMove = isCurrentMove;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void changeRoom(Room r) {
        this.currentRoom = r;
    } 

    public void setMoveMenu(boolean i) {
        this.isInMoveMenu = i;
    } 

    public boolean getIsMoveMenu() {
        return isInMoveMenu;
    } 

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public Inventory getInventory() {
       return inventory;
    }

    public int getPrimeCounter() {
        return primeCounter;
    }

    public void calculatePrimeCounter() {
        inventory.getItems().forEach(item -> {
            if(item instanceof Prime) primeCounter++;
        });
    }

    public void setPrimeCounter(int primeCounter) {
        this.primeCounter = primeCounter;
    }

    public void gameOver(){
      
        //tell them they died and give them their health back and set their room to a spawnpoint
    }
    
}
