package zork;

import java.util.ArrayList;

import zork.enemies.ExampleEnemy;
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
    
   

   
    public void fight(){
        int playerSpeed = Game.getGame().getPlayer().getSpeed();
        int enemySpeed = enemy.getSpeed();
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
    int playerHealth = Game.getGame().getPlayer().getHealth();
      int enemyHealth = enemy.getHealth();

      int playerSpeed = Game.getGame().getPlayer().getSpeed();
      int enemySpeed = enemy.getSpeed();
    
      ArrayList<Item> playerStuff = Game.getGame().getPlayer().getInventory().getItems();
      ArrayList<Item> enemyStuff = enemy.getInventory().getItems();

      ArrayList<Effects> playerEffects = new ArrayList<>();
      ArrayList<Effects> enemyEffects  = new ArrayList<>();

        while(true){
            
            

                    if(Game.getGame().getPlayer().isInWeaponMenu() == true){
                    
                        Weapon pWeapon = askWeapon();
                        int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                        Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 


                        int pDamge = pWeapon.getDamage();
                        System.out.println(Game.getGame().getPlayer().getName() + "used" + " " + pWeapon.getName());
                        int eDamage = eWeapon.getDamage();
                        System.out.println(enemy.getName() + "used" + " " + eWeapon.getName());

                        playerEffects.add(eWeapon.getEffect());
                        enemyEffects.add(pWeapon.getEffect());

                        for (int i = 0; i < playerEffects.size(); i++) {
                        int dam = playerEffects.get(i).getDamageChange();
                        int sped = playerEffects.get(i).getSpeedChange();
                        if(playerEffects.get(i).getTurn() != playerEffects.get(i).getTurnCount()){
                                playerHealth-=dam;
                                playerSpeed-=sped;
                                playerEffects.get(i).setTurnCount(playerEffects.get(i).getTurnCount()+1);
                            }else{
                                playerEffects.get(i).setTurnCount(0);
                            }
                        }

                        for (int i = 0; i < enemyEffects.size(); i++) {
                            int dam = enemyEffects.get(i).getDamageChange();
                            int sped = enemyEffects.get(i).getSpeedChange();
                            
                            if(enemyEffects.get(i).getTurn() != enemyEffects.get(i).getTurnCount()){
                                enemyHealth-=dam;
                                enemySpeed-=sped;
                                enemyEffects.get(i).setTurnCount(enemyEffects.get(i).getTurnCount()+1);
                            }else{
                                enemyEffects.get(i).setTurnCount(0);
                            }
                        }
                        

                        if(playerSpeed>enemySpeed){
                            System.out.println("You did " + pDamge + " Damage");
                            enemyHealth -= pDamge;
                            if(enemyHealth<=0){
                                System.out.println(enemy.getName() + " Died! YOU WIN!!!");
                                return true;
                            }
                            System.out.println(enemy.getName() + " did " + pDamge + " Damage");
                            playerHealth -= eDamage;
                            if(playerHealth<= 0){
                                System.out.println(enemy.getName() + " Won! YOU DIED!!!");
                                Game.getGame().getPlayer().gameOver();
                                return false;
                            }
                        }else{
                            System.out.println(enemy.getName() + " did " + pDamge + " Damage");
                            playerHealth -= eDamage;
                            if(playerHealth<= 0){
                                System.out.println(enemy.getName() + " Won! YOU DIED!!!");
                                Game.getGame().getPlayer().gameOver();
                                return false;
                            }
                            System.out.println("You did " + pDamge + " Damage");
                            enemyHealth -= pDamge;
                            if(enemyHealth<=0){
                                System.out.println(enemy.getName() + " Died! YOU WIN!!!");
                                return true;
                            }
                            
                        }
                    }else if(Game.getGame().getPlayer().getIsItemMenu() == true){
                        Item item = askItem();
                    }


            
        }
        
    }
    
    



 
    
    private Item askItem() {
        Game.getGame().getPlayer().setInItemMenu(true);
        ArrayList<Item> arr = Game.getGame().getPlayer().getInventory().getItemsWithEffects();
        
        System.out.println("What item do you want to use?");
        System.out.println(arr);
        while (Game.getGame().getPlayer().isCurrentMove() == false) {
            
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Item item = Game.getGame().getPlayer().getCurrentItem();

        return item;
    }




    private Weapon askWeapon() {
        Game.getGame().getPlayer().setInWeaponMenu(true);
        ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
        if(arr.size() >= 1){
        System.out.println("What weapon do you want to use?");
        System.out.println(arr);
        while (Game.getGame().getPlayer().isCurrentMove() == false) {
            
        }
        Game.getGame().getPlayer().setIsCurrentMove(true);
        Weapon weapon = Game.getGame().getPlayer().getCurrentWeapon();

        return weapon;
        
        }else{
            return new Weapon(0, "no weapon", false, 0, new Effects("no effect", 0, 0, 0));
        }
    }

    

    
}
