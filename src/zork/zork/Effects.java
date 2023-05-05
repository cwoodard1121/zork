package zork;

import java.util.ArrayList;

public class Effects {
    private String name;
    private String type;
    static ArrayList<Effects> affectingSpeed;
    static ArrayList<Effects> affectingDamage;
    //you can add more types

    public Effects(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


    


}
