package zork;

public class Item extends OpenableObject {
  private int weight;
  private String name;
  private boolean isOpenable;
  private double id;
  private Effect Effect;
  private boolean isWeapon = false;

  

  public Item(int weight, String name, boolean isOpenable, Effect Effect, boolean isWeapon) {
    this.weight = weight;
    this.name = name;
    this.isOpenable = isOpenable;
    this.isWeapon = isWeapon;
    this.Effect = Effect;
    this.id = (Math.random() * (Integer.MAX_VALUE - 1));
  }

  public void open() {
    if (!isOpenable)
      System.out.println("The " + name + " cannot be opened.");

  }
  public Effect getEffect() {
      return Effect;
    }

    public void setUsedInAFight(Effect Effect) {
      this.Effect = Effect;
    }
  public double getId() {
    return id;
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

  public boolean isWeapon() {
    return isWeapon;
  }

  public void setWeapon(boolean isWeapon) {
    this.isWeapon = isWeapon;
  }

  public void setEffect(Effect effect) {
    Effect = effect;
  }


}
