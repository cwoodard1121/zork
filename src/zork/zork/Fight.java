package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.entites.Player;
import zork.items.Weapon;
import java.lang.Runnable;
import java.util.Scanner;

public class Fight {
    private Enemy enemy;
    static Scanner in = new Scanner(System.in);

    public Fight(Enemy bad){
        this.enemy = bad;
        
    }
    
   
    public void fight(){
        Game.getGame().getPlayer().setChoosingMenu(false);
        Game.getGame().getPlayer().setInFight(true);     
        boolean didPlayerWin = false;        
        didPlayerWin = fightingResults();
        if(didPlayerWin)
            System.out.println("won");
        else
            System.out.println("lost");
                        
                    
                    //game over thing will have all the menu things set to default and your location back to a spawnpoint
                    //win expect player serperatly gets stuff from enemys dead body
                    //example, you beat them in a fight so you search their dead body so you can pick up the sutff that wont overload your inventory
                
            
            
       
        
    }





    private boolean fightingResults() {
                     int playerHealth = Game.getGame().getPlayer().getHealth();
                    int enemyHealth = enemy.getHealth();

                    int playerSpeed = Game.getGame().getPlayer().getSpeed();
                    int enemySpeed = enemy.getSpeed();
                        
                    
                    ArrayList<Effects> playerEffects = new ArrayList<>();
                    ArrayList<Effects> enemyEffects  = new ArrayList<>();
              while (true){      
                   
                   
                    System.out.println("Do you choose the weapon menu or the item menu (for now u cant go back)");
                    while( Game.getGame().getPlayer().getIsItemMenu() == false && Game.getGame().getPlayer().isInWeaponMenu() == false){
                        String answer = in.nextLine().toLowerCase();
                        if(answer.equals("weapon"))
                            Game.getGame().getPlayer().setInWeaponMenu(true);
                        else if(answer.equals("item"))
                            Game.getGame().getPlayer().setInItemMenu(true);
                        else
                            System.out.println("say weapon or item for the menu you want");
                    }
                
                    Game.getGame().getPlayer().setChoosingMenu(true);
           
                    if(Game.getGame().getPlayer().isInWeaponMenu()){
                    
                        Weapon pWeapon = askWeapon();
                        int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                        Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 


                        int pDamge = pWeapon.getDamage();
                        System.out.println(Game.getGame().getPlayer().getName() + " used" + " " + pWeapon.getName());
                        int eDamage = eWeapon.getDamage();
                        System.out.println(enemy.getName() + " used" + " " + eWeapon.getName());

                        playerEffects.add(eWeapon.getEffect());
                        enemyEffects.add(pWeapon.getEffect());

                        for (int i = 0; i < playerEffects.size(); i++) {
                            if(playerEffects.get(i).getName().equalsIgnoreCase("placeholder"))
                                break;
                            System.out.println("you got the effect " + playerEffects.get(i).getName());
                            int dam = playerEffects.get(i).getDamageChange();
                            int sped = playerEffects.get(i).getSpeedChange();
                            if(playerEffects.get(i).getTurn() != playerEffects.get(i).getTurnCount()){
                                    playerHealth-=dam;
                                    playerSpeed-=sped;
                                    System.out.println("it did " + dam + " Damage");
                                    System.out.println("Your speed was lowered by " + sped);
                                    playerEffects.get(i).setTurnCount(playerEffects.get(i).getTurnCount()+1);
                                }else{
                                    playerEffects.get(i).setTurnCount(0);
                                }
                        }

                        for (int i = 0; i < enemyEffects.size(); i++) {
                            if(enemyEffects.get(i).getName().equalsIgnoreCase("placeholder"))
                                break;
                            System.out.println("you dealt the effect " + enemyEffects.get(i).getName());
                            int dam = enemyEffects.get(i).getDamageChange();
                            int sped = enemyEffects.get(i).getSpeedChange();
                            
                            if(enemyEffects.get(i).getTurn() != enemyEffects.get(i).getTurnCount()){
                                enemyHealth-=dam;
                                enemySpeed-=sped;
                                System.out.println("your effect did " + dam + " Damage to " + enemy.getName());
                                System.out.println("the enimes speed was lowered by" + sped);
                                enemyEffects.get(i).setTurnCount(enemyEffects.get(i).getTurnCount()+1);
                            }else{
                                enemyEffects.get(i).setTurnCount(0);
                            }
                        }
                        

                        if(playerSpeed>enemySpeed){
                            System.out.println("You did " + pDamge + " Damage");
                            enemyHealth -= pDamge;
                            System.out.println(enemy.getName() + " did " + pDamge + " Damage");
                            playerHealth -= eDamage;
                            if(enemyHealth<=0){
                                System.out.println(enemy.getName() + " Died! YOU WIN!!!");
                                Game.getGame().getPlayer().setInFight(false);
                                return true;
                                
                                
                            }else if(playerHealth<= 0){
                                System.out.println(enemy.getName() + " Won! YOU DIED!!!");
                                Game.getGame().getPlayer().gameOver();
                                Game.getGame().getPlayer().setInFight(false);
                                return false;
                            }
                            Game.getGame().getPlayer().setChoosingMenu(true);

                            
                        }else{
                            System.out.println(enemy.getName() + " did " + eDamage + " Damage");
                            playerHealth -= eDamage;
                            System.out.println("You did " + pDamge + " Damage");
                            enemyHealth -= pDamge;
                            if(playerHealth<= 0){
                                System.out.println(enemy.getName() + " Won! YOU DIED!!!");
                                Game.getGame().getPlayer().gameOver();
                                Game.getGame().getPlayer().setInFight(false);
                                return false;
                                
                            }else if(enemyHealth<=0){
                                System.out.println(enemy.getName() + " Died! YOU WIN!!!");
                                Game.getGame().getPlayer().setInFight(false);
                                return true;
                                
                            }
                            
                            Game.getGame().getPlayer().setChoosingMenu(true);
                            System.out.println(Game.getGame().getPlayer().getName() + " has " + playerHealth + " health remaining");
                            System.out.println(enemy.getName() + " has " + enemyHealth + " health remaining");
                        }
                        
                    }else if(Game.getGame().getPlayer().getIsItemMenu() == true){
                        Item item = askItem();
                    }
                Game.getGame().getPlayer().setInWeaponMenu(false);
                Game.getGame().getPlayer().setInItemMenu(false);
                Game.getGame().getPlayer().setChoosingMenu(true);
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
        ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
        if(arr.size() >= 1){
        System.out.println("What weapon do you want to use?");
        for (int i = 0; i < arr.size(); i++) {
            System.out.println("> " + arr.get(i).getName());
        }
        boolean choiceMade = false;
        String answer = "";
        while (!choiceMade) {
            answer = in.nextLine().toLowerCase();
            choiceMade = true;
           
        }
        
        for (int i = 0; i < arr.size(); i++) {
            if(arr.get(i).getName().equalsIgnoreCase(answer)){
                return arr.get(i);
            }
        }
        
        }else{
            return new Weapon(0, "no weapon", false, 0, new Effects("no effect", 0, 0, 0));
        }

        return null;
    }

    

    
}