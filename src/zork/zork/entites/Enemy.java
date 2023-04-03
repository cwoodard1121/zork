package zork.entites;

import java.util.ArrayList;

import datatypes.Location;
import zork.Entity;
import zork.Inventory;
import zork.Item;
import zork.Move;
import zork.Room;

public class Enemy extends Entity {
    private ArrayList<Move> moves;
    private double moneyDroped;
    private int speed;



    public Enemy(Location location, Room currentRoom, int health, Inventory inventory, ArrayList<Move> moves, int money){

        super(location, currentRoom, health, inventory);
        this.moves = moves;
        this.moneyDroped = money;
        this.speed = speed;
    }


    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public ArrayList<Move> getMoves() {
        return moves;
    }


    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public double getMoney(){
        return moneyDroped;
    }

    public void setMoney(double d){
        this.moneyDroped = d;
    }

}
