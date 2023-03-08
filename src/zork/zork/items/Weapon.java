package zork.items;

import zork.Item;

public class Weapon extends Item {
    private int weight;
    private String name;
    private boolean isOpenable;
    private int damage;

    public Weapon(int weight, String name, boolean isOpenable, int damage){
        super(weight, name, isOpenable);
        this.isOpenable = false;
        this.damage = damage;
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

    public int getDamage(){
        return damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }
}
