package zork;

import java.util.ArrayList;

public class Effects {
    private String name;
    private String type;
    private int turn;
   

    public Effects(String name, String type, int turn){
        this.name = name;
        this.type = type;
        this.turn = turn;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    


}
