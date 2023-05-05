package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.entites.Player;
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
         boolean didPlayerWin;
        
        if(playerSpeed>enemySpeed){
            didPlayerWin = fightingResults();
        }else{
            didPlayerWin = fightingResults();
        }
        

        /*Fighting
         * Whoever has most speed goes first, lets say its player
         * player is given their moves and asked which weapon they want to use if they have more than one
         * if they dont it will give them the only weapon they have
         * they choose which move and it does it 
         *damage from the weapon is added to the damage of the move
         * effects are put overtime
         * 
         * 
         * 
         * 
         */

    }



    private boolean fightingResults() {
        while(true){

            if(playerSpeed>enemySpeed){
                    Move pMove = askMove();
                    Weapon pWeapon = askWeapon();

                    int ran = (int)(Math.random()*enemyMoves.size());
                    Move eMove = enemyMoves.get(ran);
                    int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                    Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 


                    int pDamge = pMove.getDamage() + pWeapon.getDamage();
                    int eDamage = eMove.getDamage() + eWeapon.getDamage();

                    //effects are here depending on teh effects we going to have to change stuff
                    //have each effect under a type like recourring damage or extra damage smth like that
                    enemyHealth -= pDamge;
                    if(enemyHealth<=0){
                        return true;
                    }
                    playerHealth -= eDamage;
                    if(playerHealth<= 0){
                        return false;
                    }



            }else{
            
                    Move pMove = askMove();
                    Weapon pWeapon = askWeapon();

                    int ran = (int)(Math.random()*enemyMoves.size());
                    Move eMove = enemyMoves.get(ran);
                    int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                    Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 


                    int pDamge = pMove.getDamage() + pWeapon.getDamage();
                    int eDamage = eMove.getDamage() + eWeapon.getDamage();

                    //effects are here depending on teh effects we going to have to change stuff
                    //have each effect under a type like recourring damage or extra damage smth like that
                    playerHealth -= eDamage;
                    if(playerHealth<= 0){
                        return false;
                    }
                    
                    enemyHealth -= pDamge;
                    if(enemyHealth<=0){
                        return true;
                    }
                    
                

            }
        }
        
    }
    
    private Move askMove() {
        Game.getGame().getPlayer().setMoveMenu(true);
        System.out.println("What move do you want to use?");
        System.out.println(Game.getGame().getPlayer().getMove());
        while(Game.getGame().getPlayer().isCurrentMove() == false){
           
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Move move = Game.getGame().getPlayer().getCurrentMove();

        return move;

    }
    
    private Weapon askWeapon() {
        Game.getGame().getPlayer().setInWeaponMenu(true);
        ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
        if(arr.size() > 1){
        System.out.println("What weapon do you want to use?");
        System.out.println(arr);
        while (Game.getGame().getPlayer().isCurrentMove() == false) {
            
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Weapon weapon = Game.getGame().getPlayer().getCurrentWeapon();

        return weapon;
        
        }else{
            return arr.get(0);
        }
    }

    

    
}
