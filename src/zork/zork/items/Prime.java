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
        super(weight, name, isOpenable);
        this.weight = weight;
        this.name = name;
        this.isOpenable = isOpenable;
        this.origin = origin;
        this.fromStore = fromStore;
    }

    

    
}
