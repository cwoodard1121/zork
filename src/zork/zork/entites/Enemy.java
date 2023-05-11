package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;
import zork.Room;

public class Enemy extends Entity {
    private double moneyDroped;
    private int speed;
    private String name;



    public Enemy(Location location, Room currentRoom, int health, Inventory inventory, int money, String name){

        super(location, currentRoom, health, inventory);
        this.moneyDroped = money;
        this.speed = speed;
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
