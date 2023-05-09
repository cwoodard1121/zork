package zork;

import java.util.ArrayList;

public class Effects {
    private String name;
    private int damageChange;
    private int speedChange;
    private int turn;
   

    public Effects(String name, int turn, int damageChange, int speedChange){
        this.name = name;
        this.turn = turn;
        this.damageChange = damageChange;
        this.speedChange = speedChange;
    }

    public String getName() {
        return name;
    }

    public int getDamageChange() {
        return damageChange;
    }

    public void setDamageChange(int damageChange) {
        this.damageChange = damageChange;
    }

    public int getSpeedChange() {
        return speedChange;
    }

    public void setSpeedChange(int speedChange) {
        this.speedChange = speedChange;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    


}
