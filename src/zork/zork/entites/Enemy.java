package zork.entites;


import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Room;

public class Enemy extends Entity {
    private double moneyDroped;
    private int speed;
    private String name;



    public Enemy(Location location, Room currentRoom, int health, Inventory inventory, int money, String name){

        super(location, currentRoom, health, inventory);
        this.moneyDroped = money;
        this.name = name;
    }


    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getMoney(){
        return moneyDroped;
    }

    public void setMoney(double d){
        this.moneyDroped = d;
    }


    public double getMoneyDroped() {
        return moneyDroped;
    }


    public void setMoneyDroped(double moneyDroped) {
        this.moneyDroped = moneyDroped;
    }


    public String getName() {
        return name;
    }

}
