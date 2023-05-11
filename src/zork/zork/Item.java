package zork;

public class Item extends OpenableObject {
  private int weight;
  private String name;
  private boolean isOpenable;
  private double id;
  private boolean hasEffect;


  

  public Item(int weight, String name, boolean isOpenable, boolean hasEffect) {
    this.weight = weight;
    this.name = name;
    this.isOpenable = isOpenable;
    this.hasEffect = hasEffect;
    this.id = (Math.random() * (Integer.MAX_VALUE - 1));
  }

  public void open() {
    if (!isOpenable)
      System.out.println("The " + name + " cannot be opened.");

  }
  public boolean hasEffect() {
      return hasEffect;
    }

    public void setUsedInAFight(boolean hasEffect) {
      this.hasEffect = hasEffect;
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


}
