package zork.items;

import zork.Effects;
import zork.Item;
public class TestItem extends Item {
    private int weight;
    private String name;
    private boolean isOpenable;
    private int damage;
    private Effects effect;
    public TestItem(int weight, String name, boolean isOpenable, Effects effect) {
        super(weight, name, isOpenable, true);
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
    public Effects getEffect() {
        return effect;
    }
    public void setEffect(Effects effect) {
        this.effect = effect;
    }
}
