package zork.items;

import zork.Effects;
import zork.Item;

public class Weapon extends Item {
    private int weight;
    private String name;
    private boolean isOpenable;
    private int damage;
    private Effects effect;
  
    

    

    public Weapon(int weight, String name, boolean isOpenable, int damage, Effects effect){
        super(weight, name, isOpenable, true);
        this.isOpenable = false;
        this.damage = damage;
        this.effect = effect;

    }

    public String getWeapon() {
        return name;
    }


    public Effects getEffect() {
        try {
            return effect;
        } catch (Exception e) {
            return new Effects("placeholder", 0, 0, 0);
        }
        
    }

    public void setEffect(Effects effect) {
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
