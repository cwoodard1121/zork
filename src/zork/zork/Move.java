package zork;

public class Move {
    private String move;
    private int damage;
    private String effects;
    
    public Move(String move, int damage, String effects){
        this.move = move;
        this.damage = damage;
        this.effects = effects;
    }

    public String readEffects(String effects){
        return null;
    }

    public int getDamage(){
        return damage;
    }

    public void setDamage(int d){
        this.damage = d;
    }

    public String getMove(){
        return move;
    }

    public String getEffects(){
        return effects;
    }

    public void setEffects(String d){
        this.effects = d;
    }
}
