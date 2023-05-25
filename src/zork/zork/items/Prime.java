package zork.items;

import zork.Item;

public class Prime extends Item {

    private int weight;
    private String name;
    private boolean isOpenable;
    private String color;
    private int percentFull;
    private boolean fromStore;
    private String origin;
    

    public Prime(int weight, String name, boolean isOpenable, String color, boolean fromStore, String origin) {
        super(weight, name, isOpenable, null, false);
        this.weight = weight;
        this.name = name;
        this.isOpenable = isOpenable;
        this.origin = origin;
        this.fromStore = fromStore;
    }


    public int getWeight() {
        return weight;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public boolean isOpenable() {
        return isOpenable;
    }


    public void setOpenable(boolean isOpenable) {
        this.isOpenable = isOpenable;
    }


    public String getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = color;
    }


    public int getPercentFull() {
        return percentFull;
    }


    public void setPercentFull(int percentFull) {
        this.percentFull = percentFull;
    }


    public boolean isFromStore() {
        return fromStore;
    }


    public void setFromStore(boolean fromStore) {
        this.fromStore = fromStore;
    }


    public String getOrigin() {
        return origin;
    }


    public void setOrigin(String origin) {
        this.origin = origin;
    }

    




    
}
