package zork.entites;


import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;

import zork.Room;
import zork.items.Prime;
import zork.items.Weapon;

public class Player extends Entity {
    private Location location;

    private int primeCounter;
    private int speed;
    
    private boolean isInItemMenu;
    private boolean isInWeaponMenu;
    private Weapon currentWeapon;
    private Item currentItem;
    private boolean isChoosingMenu = true;
    private boolean isInFight;


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
    
    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }
    
    public boolean isInFight() {
        return isInFight;
    }

    public void setInFight(boolean isInFight) {
        this.isInFight = isInFight;
    }

    public void setInItemMenu(boolean isInItemMenu) {
        this.isInItemMenu = isInItemMenu;
    }

    public boolean isChoosingMenu() {
        return isChoosingMenu;
    }

    public void setChoosingMenu(boolean isChoosingMenu) {
        this.isChoosingMenu = isChoosingMenu;
    }

    public Weapon getCurrentWeapon(){
        return currentWeapon;
    }

    public void setName(String name) {
        this.name = name;
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
        System.out.println(r.isLocked());
        if(r.isLocked()) {
            System.out.println("Unscheduled maintainence has been scheduled. Come back later.");
            return;
        }
        if(r.hasRunnable()) {
            r.getRunnable().run();
        }
        super.setCurrentRoom(r);
    } 

    public void setItemMenu(boolean i) {
        this.isInItemMenu = i;
    } 

    public boolean getIsItemMenu() {
        return isInItemMenu;
    } 

    public int getHealth() {
        return super.getHealth();
    }

    public void setHealth(int health) {
        super.setHealth(health);
    }


    public Inventory getInventory() {
       return super.getInventory();
    }

    public int getPrimeCounter() {
        return primeCounter;
    }

    public void calculatePrimeCounter() {
        super.getInventory().getItems().forEach(item -> {
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
