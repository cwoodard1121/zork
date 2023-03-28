package zork;

import java.util.ArrayList;

public class Inventory {
  private ArrayList<Item> items;
  private int maxWeight;
  private int currentWeight;

  public Inventory(int maxWeight) {
    this.items = new ArrayList<Item>();
    this.maxWeight = maxWeight;
    this.currentWeight = 0;
  }


  @Deprecated
  /**
   * Should not be used because of multithreading
   * @return
   */
  public ArrayList<Item> getItems() {
    return this.items;
  }

  public int getMaxWeight() {
    return maxWeight;
  }

  public Inventory removeItem(Item i) {
    this.items.remove(i);
    return this;
  }

  public Item getItem(int i) {
    return items.get(i);
  }

  public boolean hasItem(Item i) {
    return items.contains(i);
  }

  public int getItemCount() {
    return items.size();
  }

  public int getCurrentWeight() {
    return currentWeight;
  }

  public boolean addItem(Item item) {
    if (item.getWeight() + currentWeight <= maxWeight)
      return items.add(item);
    else {
      return false;
    }
  }

}
