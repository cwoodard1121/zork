package zork.items;

import zork.Effect;
import zork.Item;
public class TestItem extends Item {
    private int weight;
    private String name;
    private boolean isOpenable;
    private int damage;
    private Effect effect;
    public TestItem(int weight, String name, boolean isOpenable, Effect effect) {
        super(weight, name, isOpenable, effect, false);
        this.isOpenable = false;
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
    public boolean isOpenable() {
        return isOpenable;
    }
    public void setOpenable(boolean isOpenable) {
        this.isOpenable = isOpenable;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public Effect getEffect() {
        return effect;
    }
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
