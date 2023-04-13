package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import java.util.Scanner;

public class Fight {
    static Scanner in = new Scanner(System.in);
    private Enemy enemy;

    public Fight(Enemy bad){
        this.enemy = bad;
    }

    private int playerHealth = Game.getGame().getPlayer().getHealth();
    private int enemyHealth = enemy.getHealth();

    private int playerSpeed = Game.getGame().getPlayer().getSpeed();
    private int enemySpeed = enemy.getSpeed();
    
    private ArrayList<Item> playerStuff = Game.getGame().getPlayer().getInventory().getItems();
    private ArrayList<Item> enemyStuff = enemy.getInventory().getItems();

    private ArrayList<Move> playerMoves = Game.getGame().getPlayer().getMove();
    private ArrayList<Move> enemyMoves = enemy.getMoves();

    public void fight(){
         boolean didWin;
        while(playerHealth>0 || enemyHealth>0){
            if(playerSpeed>enemySpeed){
                didWin = fightingResults(true);
            }else{
                didWin = fightingResults(false);
            }
        }

        /*Fighting
         * Whoever has most speed goes first, lets say its player
         * player is given their moves and asked which weapon they want to use if they have more than one
         * if they dont it will give them the only weapon they have
         * they choose which move and it does it 
         *damage from the weapon is added to the damage of the move
         * 
         * 
         * 
         * 
         * 
         */

    }

    private boolean fightingResults(boolean isPlayerFirst) {
        if(isPlayerFirst){
            Move move = askQuestion1();
            Weapon weapon = askQuestion2();
        }

        return false;

    }
    
    private Move askQuestion1() {
        Game.getGame().getPlayer().setMoveMenu(true);
        System.out.println("What move do you want to use?");
        System.out.println(Game.getGame().getPlayer().getMove());
        while(Game.getGame().getPlayer().isCurrentMove() == false){
           
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Move move = Game.getGame().getPlayer().getCurrentMove();

        return move;

    }
    
    private Weapon askQuestion2() {
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Game.getGame().getPlayer().setInWeaponMenu(true);
        ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
        if(arr.size() > 1){
        System.out.println("What weapon do you want to use?");
        System.out.println(arr);
        while (Game.getGame().getPlayer().isCurrentMove() == false) {
            
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        
        }else{
            return arr.get(0);
        }
    }

    

    
}
