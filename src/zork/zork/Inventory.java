package zork;

import java.util.ArrayList;

import zork.items.Weapon;

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

  public void setCurrentWeight(int i) {
    this.currentWeight = i;
  }

  public boolean addItem(Item item) {
    if (item.getWeight() + currentWeight <= maxWeight)
      return items.add(item);
    else {
      return false;
    }
  }

  public ArrayList<Weapon> getWeapons(){
    ArrayList<Weapon> arr = new ArrayList<Weapon>();
    for(int i = 0; i<items.size(); i++){
      if(items.get(i) instanceof Weapon){
        arr.add((Weapon) items.get(i));
      }
    }
    return arr;
  }

  public ArrayList<Item> getItemsWithEffects(){
    //ONLY USE THIS WITH ITEMS THAT CAN BE USED IN A FIGHT
    ArrayList<Item> arr = new ArrayList<Item>();
    for (int i = 0; i < items.size(); i++) {
        try {
          if(items.get(i).getEffect() instanceof Effects && !items.get(i).isWeapon()){
           
            arr.add(items.get(i));
          }
        } catch (Exception e) {
          continue;
        }
        
    }

    return arr;
  }




  

}
