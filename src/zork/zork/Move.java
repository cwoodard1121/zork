package zork;

public class Move {
    private String move;
    private int damage;
    
    
    public Move(String move, int damage, String effects){
        this.move = move;
        this.damage = damage;
        
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

    
}
