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
                            
                        
    private ArrayList<Effect> playerEffect;
    private ArrayList<Effect> enemyEffect;

    public Fight(Enemy bad){
        this.enemy = bad;
        
    }
    
   
    /**
     * Main fight method.
     */
    public boolean fight(){
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
        return didPlayerWin;
       
        
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
                            
                        
                         playerEffect = new ArrayList<>();
                         enemyEffect  = new ArrayList<>();
                while (true){      
                        
                    
                       
            
                        if(enemySpeed>playerSpeed){
                            enemyTurn();
                            playerTurn();
                        }else{
                            playerTurn();
                            enemyTurn();
                        }
                        
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
                        
                        Game.getGame().getPlayer().setInWeaponMenu(false);
                        Game.getGame().getPlayer().setInItemMenu(false);
                        
                }
                
            } catch (Exception e) {
                e.printStackTrace();
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
        playerEffect.add(eWeapon.getEffect());
        computeEffects(true);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void computeEffects(boolean isEnemy) {
        try {
            
        
        if(isEnemy){
            playerEffect = makeItUseable(playerEffect);
            for(int i = 0; i<playerEffect.size(); i++){
                int dam = playerEffect.get(i).getDamageChange();
                int sped = playerEffect.get(i).getSpeedChange();

                if(playerEffect.get(i).getTurnCount() != playerEffect.get(i).getTurn()){
                    text.slowTextSpeed("you got the effect " + playerEffect.get(i).getName(), 7);
                    playerHealth-= dam;
                    playerSpeed -= sped;
                    text.slowTextSpeed("it lowered your speed by " + sped + " it did " + dam + " damage", 7);
                    playerEffect.get(i).setTurnCount(playerEffect.get(i).getTurnCount()+1);
                }else{
                    text.slowTextSpeed("effect is over " + playerEffect.get(i).getName(), 7);
                    playerEffect.get(i).setTurnCount(0);
                    playerEffect.remove(i);
                }

            }

        }else{
            enemyEffect = makeItUseable(enemyEffect);
            for(int i = 0; i<enemyEffect.size(); i++){
                int dam = enemyEffect.get(i).getDamageChange();
                int sped = enemyEffect.get(i).getSpeedChange();

                if(enemyEffect.get(i).getTurnCount() != enemyEffect.get(i).getTurn()){
                    text.slowTextSpeed("the enemy got the effect " + enemyEffect.get(i).getName(), 7);
                    enemyHealth-= dam;
                    enemySpeed -= sped;
                    text.slowTextSpeed("it lowered their speed by " + sped + " and it did " + dam + " damage", 7);
                    playerEffect.get(i).setTurnCount(playerEffect.get(i).getTurnCount()+1);
                }else{
                    text.slowTextSpeed("effect is over " + enemyEffect.get(i).getName(), 7);
                    playerEffect.get(i).setTurnCount(0);
                    enemyEffect.remove(i);
                }

            }
        }
        } catch (Exception e) {
            System.out.println("error slow text speed");
        }
    }


    private ArrayList<Effect> makeItUseable(ArrayList<Effect> effects) {
        for (int j = 0; j < effects.size(); j++) {
            if(effects.get(j) == null){
                effects.remove(j);
                continue;
            }
        }

        for(int i = 0; i<effects.size()-1; i++){

            for (int x = i+1; x < effects.size(); x++) {
                if(effects.get(i).getName().equals(effects.get(x).getName())){
                    effects.remove(x);
                }
            }
        }

        
        return effects;
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
                enemyEffect.add(pWeapon.getEffect());
                computeEffects(false);
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
            e.printStackTrace();
        }
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