package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.items.Weapon;
import java.util.Scanner;
import zork.Utils.SoundHandler;

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
        SoundHandler.stop();
        SoundHandler.playSound("would_boss.wav",true);
        didPlayerWin = fightingResults();
        if(didPlayerWin)
            System.out.println("won");
        else
            System.out.println("lost");

        SoundHandler.stopSound("would_boss.wav");
        SoundHandler.startAfterInterruption();
                        
                    
                    //game over thing will have all the menu things set to default and your location back to a spawnpoint
                    //win expect player serperatly gets stuff from enemys dead body
                    //example, you beat them in a fight so you search their dead body so you can pick up the sutff that wont overload your inventory
                
            
            
       
        
    }





    private boolean fightingResults() {

        try {
            
       
                        Graphics text = new Graphics();
                        int playerHealth = Game.getGame().getPlayer().getHealth();
                        int enemyHealth = enemy.getHealth();

                        int playerSpeed = Game.getGame().getPlayer().getSpeed();
                        int enemySpeed = enemy.getSpeed();
                            
                        
                        ArrayList<Effects> playerEffects = new ArrayList<>();
                        ArrayList<Effects> enemyEffects  = new ArrayList<>();
                while (true){      
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
                    
                        Game.getGame().getPlayer().setChoosingMenu(true);
            
                        if(Game.getGame().getPlayer().isInWeaponMenu()){
                        
                            Weapon pWeapon = askWeapon();
                            int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                            Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 


                            int pDamge = pWeapon.getDamage();
                            text.slowTextSpeed(Game.getGame().getPlayer().getName() + " used" + " " + pWeapon.getName(), 7);
                            int eDamage = eWeapon.getDamage();
                            text.slowTextSpeed(enemy.getName() + " used" + " " + eWeapon.getName(), 7);

                            playerEffects.add(eWeapon.getEffect());
                            enemyEffects.add(pWeapon.getEffect());
                            
                            for (int i = 0; i < playerEffects.size(); i++) {
                                if(playerEffects.get(i).getName().equalsIgnoreCase("placeholder")){
                                    playerEffects.remove(i);
                                    continue;
                                }else{
                                    int doseRepeat = 0;
                                    int location = 0;
                                    for(int y = 0; y<playerEffects.size(); y++){
                                        if(playerEffects.get(y).getName().equals(playerEffects.get(i).getName())){
                                            doseRepeat++;
                                            location = y;
                                        }
                                    }
                                    if(doseRepeat>=2){
                                        playerEffects.remove(location);
                                       
                                    }
                                }
                                
                                int dam = playerEffects.get(i).getDamageChange();
                                int sped = playerEffects.get(i).getSpeedChange();
                                if(playerEffects.get(i).getTurn() != playerEffects.get(i).getTurnCount()){
                                        text.slowTextSpeed("you got the effect " + playerEffects.get(i).getName(), 7);
                                        playerHealth-=dam;
                                        playerSpeed-=sped;
                                        text.slowTextSpeed("it did " + dam + " Damage", 7);
                                        text.slowTextSpeed("Your speed was lowered by " + sped, 7);
                                        playerEffects.get(i).setTurnCount(playerEffects.get(i).getTurnCount()+1);
                                }else{
                                    text.slowTextSpeed("the effect " + playerEffects.get(i).getName() + " has stoped", 7);
                                    playerEffects.get(i).setTurn(0);
                                    playerEffects.remove(i);
                                    i = 0;
                                       
                                }
                            }

                            //effect thing above works when it is complette copy it everywhere else

                            for (int i = 0; i < enemyEffects.size(); i++) {
                                if(enemyEffects.get(i).getName().equalsIgnoreCase("placeholder")){
                                    enemyEffects.remove(i);
                                    continue;
                                }else{
                                    int doseRepeat = 0;
                                    int location = 0;
                                    for(int y = 0; y<enemyEffects.size(); y++){
                                        if(enemyEffects.get(y).getName().equals(enemyEffects.get(i).getName())){
                                            doseRepeat++;
                                            location = y;
                                        }
                                    }
                                    if(doseRepeat>=2){
                                        enemyEffects.remove(location);
                                       
                                    }
                                }
                                
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
                                    i = 0;   
                                }
                            }
                            if(playerSpeed>enemySpeed){
                                text.slowTextSpeed("You did " + pDamge + " Damage", 7);
                                enemyHealth -= pDamge;
                                text.slowTextSpeed(enemy.getName() + " did " + eDamage + " Damage", 7);
                                playerHealth -= eDamage;
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

                                
                            }else{
                                text.slowTextSpeed(enemy.getName() + " did " + eDamage + " Damage", 7);
                                playerHealth -= eDamage;
                                text.slowTextSpeed("You did " + pDamge + " Damage", 7);
                                enemyHealth -= pDamge;
                                if(playerHealth<= 0){
                                    text.slowTextSpeed(enemy.getName() + " Won! YOU DIED!!!", 7);
                                    Game.getGame().getPlayer().gameOver();
                                    Game.getGame().getPlayer().setInFight(false);
                                    return false;
                                    
                                }else if(enemyHealth<=0){
                                    text.slowTextSpeed(enemy.getName() + " Died! YOU WIN!!!", 7);
                                    Game.getGame().getPlayer().setInFight(false);
                                    return true;
                                    
                                }
                                
                                Game.getGame().getPlayer().setChoosingMenu(true);
                                text.slowTextSpeed(Game.getGame().getPlayer().getName() + " has " + playerHealth + " health remaining", 7);
                                text.slowTextSpeed(enemy.getName() + " has " + enemyHealth + " health remaining", 7);
                            }
                            
                        }else if(Game.getGame().getPlayer().getIsItemMenu() == true){
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

                                int ran2 = (int)(Math.random()*enemy.getInventory().getWeapons().size());
                                Weapon eWeapon = enemy.getInventory().getWeapons().get(ran2); 
                                int eDamage = eWeapon.getDamage();


                                text.slowTextSpeed(enemy.getName() + " used" + " " + eWeapon.getName(), 7);
                                playerEffects.add(eWeapon.getEffect());
                                for (int i = 0; i < playerEffects.size(); i++) {
                                    if(playerEffects.get(i).getName().equalsIgnoreCase("placeholder")){
                                        playerEffects.remove(i);
                                        continue;
                                    }else{
                                        int doseRepeat = 0;
                                        int location = 0;
                                        for(int y = 0; y<playerEffects.size(); y++){
                                            if(playerEffects.get(y).getName().equals(playerEffects.get(i).getName())){
                                                doseRepeat++;
                                                location = y;
                                            }
                                        }
                                        if(doseRepeat>=2){
                                            playerEffects.remove(location);
                                        
                                        }
                                    }
                                    
                                    int dam = playerEffects.get(i).getDamageChange();
                                    int sped = playerEffects.get(i).getSpeedChange();
                                    if(playerEffects.get(i).getTurn() != playerEffects.get(i).getTurnCount()){
                                            text.slowTextSpeed("you got the effect " + playerEffects.get(i).getName(), 7);
                                            playerHealth-=dam;
                                            playerSpeed-=sped;
                                            text.slowTextSpeed("it did " + dam + " Damage", 7);
                                            text.slowTextSpeed("Your speed was lowered by " + sped, 7);
                                            playerEffects.get(i).setTurnCount(playerEffects.get(i).getTurnCount()+1);
                                    }else{
                                        text.slowTextSpeed("the effect " + playerEffects.get(i).getName() + " has stoped", 7);
                                        playerEffects.get(i).setTurn(0);
                                        playerEffects.remove(i);
                                        
                                    }
                                }


                                text.slowTextSpeed(enemy.getName() + " did " + eDamage + " Damage", 7);
                                playerHealth -= eDamage;
                                if(playerHealth<= 0){
                                    text.slowTextSpeed(enemy.getName() + " Won! YOU DIED!!!", 7);
                                    Game.getGame().getPlayer().gameOver();
                                    Game.getGame().getPlayer().setInFight(false);
                                    return false;
                                }
                                
                                text.slowTextSpeed(Game.getGame().getPlayer().getName() + " has " + playerHealth + " health remaining", 7);
                                text.slowTextSpeed(enemy.getName() + " has " + enemyHealth + " health remaining", 7);
                            }else{
                                text.slowTextSpeed("you dont have an item avaliable (should have visited a tims...)", 7);
                            }

                        }

                    Game.getGame().getPlayer().setInWeaponMenu(false);
                    Game.getGame().getPlayer().setInItemMenu(false);
                    Game.getGame().getPlayer().setChoosingMenu(true);
                }
            } catch (Exception e) {
              System.out.println("error occured ");
                return false;
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
            return new Item(0, null, false, null, null);
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
            return new Weapon(0, "no weapon", false, 0, new Effects("no effect", 0, 0, 0, 0));
        }

        } catch (Exception e) {
            // TODO: handle exception
            return new Weapon(0, null, false, 0, null);
        }

      
    }

    

    
}