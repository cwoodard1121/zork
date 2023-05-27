package zork;


public class Effects {
    private String name;
    private int damage;
    private int speed;
    private int health;
    private int turn;
    private int turnCount;

   

    public Effects(String name, int turn, int damage, int speed, int health){
        this.name = name;
        this.turn = turn;
        this.damage = damage;
        this.speed = speed;
        this.health = health;

    }

    public String getName() {
        return name;
    }

    public int getDamageChange() {
        return damage;
    }

    public void setDamageChange(int damageChange) {
        this.damage = damageChange;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public int getSpeedChange() {
        return speed;
    }

    public void setSpeedChange(int speedChange) {
        this.speed = speedChange;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }




    


}
