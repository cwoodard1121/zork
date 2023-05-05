package zork.items;

import zork.Item;

public class Weapon extends Item {
    private int weight;
    private String name;
    private boolean isOpenable;
    private int damage;
    private String effect;
    

    

    public Weapon(int weight, String name, boolean isOpenable, int damage, String effect){
        super(weight, name, isOpenable);
        this.isOpenable = false;
        this.damage = damage;
        this.effect = effect;
       
    }

    public String getWeapon() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
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
