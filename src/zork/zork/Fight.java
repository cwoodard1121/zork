package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import java.util.Scanner;
import zork.Utils.SoundHandler;

public class Fight {
    private Enemy enemy;
    static Scanner in = new Scanner(System.in);
    Graphics text = new Graphics();
    private int playerHealth;
    private int enemyHealth;

    private int playerSpeed;
    private int enemySpeed;
                            
                        
    private ArrayList<Effects> playerEffects;
    private ArrayList<Effects> enemyEffects;

    public Fight(Enemy bad){
        this.enemy = bad;
        
    }
    
   
    /**
     * Main fight method.
     */
    public void fight(){
        Game.getGame().getPlayer().setChoosingMenu(false);
        Game.getGame().getPlayer().setInFight(true);     
        boolean didPlayerWin = false;
        SoundHandler.stop();
        SoundHandler.playSound("would_boss.wav",true);
        didPlayerWin = fightingResults();
        if(didPlayerWin)
            System.out.println("won");
        else
            System.out.println("lost");

        SoundHandler.stopSound("would_boss.wav");
        SoundHandler.startAfterInterruption();
       
        
    }





    /**
     * @return Whether the player wins the fight
     */
    private boolean fightingResults() {

        try {
                        text = new Graphics();
                        playerHealth = Game.getGame().getPlayer().getHealth();
                        enemyHealth = enemy.getHealth();

                        playerSpeed = Game.getGame().getPlayer().getSpeed();
                        enemySpeed = enemy.getSpeed();
                            
                        
                         playerEffects = new ArrayList<>();
                         enemyEffects  = new ArrayList<>();
                while (true){      
                        
                    
                        Game.getGame().getPlayer().setChoosingMenu(true);
            
                        if(enemySpeed>playerSpeed){
                            enemyTurn();
                            playerTurn();
                        }else{
                            playerTurn();
                            enemyTurn();
                        }
                        Game.getGame().getPlayer().setChoosingMenu(true);
                        text.slowTextSpeed(Game.getGame().getPlayer().getName() + " has " + playerHealth + " health remaining", 7);
                        text.slowTextSpeed(enemy.getName() + " has " + enemyHealth + " health remaining", 7);  
                        if(enemyHealth<=0){
                                    text.slowTextSpeed(enemy.getName() + " Died! YOU WIN!!!", 7);
                                    Game.getGame().getPlayer().setInFight(false);
                                    return true;
                        }else if(playerHealth<= 0){
                                    text.slowTextSpeed(enemy.getName() + " Won! YOU DIED!!!", 7);
                                    Game.getGame().getPlayer().gameOver();
                                    Game.getGame().getPlayer().setInFight(false);
                                    return false;
                        }
                        Game.getGame().getPlayer().setChoosingMenu(true);

                        Game.getGame().getPlayer().setInWeaponMenu(false);
                        Game.getGame().getPlayer().setInItemMenu(false);
                        Game.getGame().getPlayer().setChoosingMenu(true);
                }
                
            } catch (Exception e) {
              System.out.println("error occured ");
                return false;
            }
                
        }    
    
   


 
    
    private void enemyTurn() {
        try{

        int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
        Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 
        int eDamage = eWeapon.getDamage();
        text.slowTextSpeed(enemy.getName() + " used" + " " + eWeapon.getName(), 7);
        playerHealth-= eDamage;
        text.slowTextSpeed("they did " + eDamage + " Damage", 7);
        playerEffects.add(eWeapon.getEffect());
        computeEffects(false);

        }catch (Exception e){
            System.out.println("error happened");
        }
    }


    private void playerTurn() {
        try {
            text.slowTextSpeed("Do you want to use a WEAPON or a ITEM (for now u cant go back)", 7);
                        
                        while( Game.getGame().getPlayer().getIsItemMenu() == false && Game.getGame().getPlayer().isInWeaponMenu() == false){
                            String answer = in.nextLine().toLowerCase();
                            if(answer.equals("weapon"))
                                Game.getGame().getPlayer().setInWeaponMenu(true);
                            else if(answer.equals("item"))
                                Game.getGame().getPlayer().setInItemMenu(true);
                            else
                            
                            text.slowTextSpeed("say WEAPON or ITEM for the menu you want", 7);
                        }

            if(Game.getGame().getPlayer().isInWeaponMenu()){
                Weapon pWeapon = askWeapon();
                int pDamge = pWeapon.getDamage();
                text.slowTextSpeed(Game.getGame().getPlayer().getName() + " used" + " " + pWeapon.getName(), 7);
                enemyHealth -= pDamge;
                text.slowTextSpeed("You did " + pDamge + " Damage", 7);
                enemyEffects.add(pWeapon.getEffect());
                computeEffects(true);
            }else{
                Item item = askItem();
                if(item != null){
                    playerHealth += item.getEffect().getHealth();
                    playerSpeed += item.getEffect().getSpeedChange();
                    text.slowTextSpeed("health up by " + item.getEffect().getHealth() + " and speed went up by " + item.getEffect().getSpeedChange(), 7);
                    for (int i = 0; i < Game.getGame().getPlayer().getInventory().getItems().size(); i++) {
                        if(item.getName().equals(Game.getGame().getPlayer().getInventory().getItems().get(i).getName())){
                            Game.getGame().getPlayer().getInventory().getItems().remove(i);
                                }
                            }
                        }else{
                        text.slowTextSpeed("you dont have an item avaliable (should have visited a tims...)", 7);
                            
                    }

            }
        } catch (Exception e) {
        System.out.println("error happened");
        }
    }


    private void computeEffects(boolean isForEnemy) {
        try {
        if(isForEnemy){
            for (int i = 0; i < enemyEffects.size(); i++) {
                if(enemyEffects.get(i) == null){
                    enemyEffects.remove(i);
                    i = 0;
                    continue;
                }else{
                    enemyEffects = removeRepeats(enemyEffects);
                    int dam = enemyEffects.get(i).getDamageChange();
                    int sped = enemyEffects.get(i).getSpeedChange();
                    if(enemyEffects.get(i).getTurn() != enemyEffects.get(i).getTurnCount()){
                        text.slowTextSpeed("you dealt the effect " + enemyEffects.get(i).getName(), 7);
                        enemyHealth-=dam;
                        enemySpeed-=sped;
                        text.slowTextSpeed("it did " + dam + " Damage", 7);
                        text.slowTextSpeed("Their speed was lowered by " + sped, 7);
                        enemyEffects.get(i).setTurnCount(enemyEffects.get(i).getTurnCount()+1);
                    }else{
                        text.slowTextSpeed("the effect " + enemyEffects.get(i).getName() + " has stoped", 7);
                        enemyEffects.get(i).setTurn(0);
                        enemyEffects.remove(i);
                    }

                }
            }
        }else{
            for (int i = 0; i < playerEffects.size(); i++) {
                if(playerEffects.get(i) == null){
                    playerEffects.remove(i);
                    i = 0;
                    continue;
                }else{
                    playerEffects = removeRepeats(playerEffects);
                    int dam = playerEffects.get(i).getDamageChange();
                    int sped = playerEffects.get(i).getSpeedChange();
                    if(playerEffects.get(i).getTurn() != playerEffects.get(i).getTurnCount()){
                        text.slowTextSpeed("you have the effect " + playerEffects.get(i).getName(), 7);
                        playerHealth-=dam;
                        playerSpeed-=sped;
                        text.slowTextSpeed("it did " + dam + " Damage", 7);
                        text.slowTextSpeed("your speed was lowered by " + sped, 7);
                        playerEffects.get(i).setTurnCount(playerEffects.get(i).getTurnCount()+1);
                    }else{
                        text.slowTextSpeed("the effect " + playerEffects.get(i).getName() + " has stoped", 7);
                        playerEffects.get(i).setTurn(0);
                        playerEffects.remove(i);
                    }

                }
            }
        }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    private ArrayList<Effects> removeRepeats(ArrayList<Effects> Effects) {
        for (int i = 0; i < Effects.size(); i++) {
            
                int doseRepeat = 0;
                int location = 0;
                for(int y = 0; y<Effects.size(); y++){
                    if(Effects.get(y).getName().equals(Effects.get(i).getName())){
                        doseRepeat++;
                        location = y;
                    }
                }
                if(doseRepeat>=2){
                    Effects.remove(location);
                   
                }
        
        }

        return Effects;

    }


    private Item askItem() {
        try{
        Graphics text = new Graphics();
        Game.getGame().getPlayer().setInItemMenu(true);
        ArrayList<Item> arr = Game.getGame().getPlayer().getInventory().getItemsWithEffects();

        while(true){
            if(Game.getGame().getPlayer().getInventory().getItemsWithEffects().size()>=1){
                text.slowTextSpeed("What item do you want to use?", 7);
                for (int i = 0; i < arr.size(); i++) {
                    text.slowTextSpeed("> " + arr.get(i).getName(), 7);
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

                text.slowTextSpeed("that is not an item you have", 7); 
            }else{
                return null;
            }
        }
        } catch (Exception e) {
            // TODO: handle exception
            return new Item(0, null, false, null, false);
        }

        
    }


    private Weapon askWeapon() {
        try {
            
        Graphics text = Game.getGame().getRenderer();
        ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
        if(arr.size() >= 1){

        while(true){
            text.slowTextSpeed("What weapon do you want to use?", 7);
            for (int i = 0; i < arr.size(); i++) {
                text.slowTextSpeed("> " + arr.get(i).getName(), 7);
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

            text.slowTextSpeed("that is not an weapon you have", 7); 
        }
        
        }else{
            return new Weapon(0, "no weapon", false, 0, new Effect("no effect", 0, 0, 0, 0));
        }

        } catch (Exception e) {
            // TODO: handle exception
            return new Weapon(0, null, false, 0, null);
        }

      
    }

    

    
}