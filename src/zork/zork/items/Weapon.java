package zork.items;

import zork.Effects;
import zork.Item;

public class Weapon extends Item {
    private int weight;
    private boolean isOpenable;
    private int damage;
    private Effects effect;
  
    

    

    public Weapon(int weight, String name, boolean isOpenable, int damage, Effects effect){
        super(weight, name, isOpenable, effect, true);
        this.isOpenable = false;
        this.damage = damage;
       

    }

    public String getWeapon() {
        return super.getName();
    }


    public Effects getEffect() {
        if (effect != null) {
            return effect;
        }else{
            return new Effects("placeholder", 0, 0, 0, 0);
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
        return super.getName();
    }
    
    public void setName(String name) {
        super.setName(name);
    }

    public int getDamage(){
        return damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }
}
