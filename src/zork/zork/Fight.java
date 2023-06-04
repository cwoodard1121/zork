package zork;

import java.util.ArrayList;

import zork.entites.Enemy;
import zork.entites.Player;
import zork.items.Weapon;
import java.util.Scanner;
import zork.Utils.SoundHandler;

public class Fight {
    private Enemy enemy;
    static Scanner in = new Scanner(System.in);
    Graphics text = new Graphics();
    private int playerHealth;
    private int enemyHealth;
    private double enemyTotalHealth;
    private double playerTotalHealth = Game.getGame().getPlayer().getMaxHealth();

    private int playerSpeed;
    private int enemySpeed;
    private boolean isTotalHealth = false;                   
                        
    private ArrayList<Effect> playerEffect;
    private ArrayList<Effect> enemyEffect;

    public Fight(Enemy bad){
        this.enemy = bad;
       
    }
    
   
    /**
     * Main fight method.
     */
    public boolean fight(){
        Player player = Game.getGame().getPlayer();
        player.setChoosingMenu(false);
        player.setInFight(true);     
        boolean didPlayerWin = false;
        SoundHandler.stop();
        SoundHandler.playSound("would_boss.wav",true);
        didPlayerWin = fightingResults();
        if(didPlayerWin){
            expGain();
            System.out.println("You got " + enemy.getMoney() + " from " + enemy.getName());
            Game.getGame().getPlayer().setMoney(Game.getGame().getPlayer().getMoney() + enemy.getMoney());

            
        }else{
            System.out.println("lost");
        }

        SoundHandler.stopSound("would_boss.wav");
        SoundHandler.startAfterInterruption();
        Game.getGame().getPlayer().setInWeaponMenu(false);
        Game.getGame().getPlayer().setInItemMenu(false);
        return didPlayerWin;
       
        
    }


    private void expGain() {
        try {
            Player player = Game.getGame().getPlayer();
            boolean leveledUp = false;
            Game.getGame().getPlayer().addExp(enemy.getExp());
            text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Gained " + enemy.getExp() + " EXP", 20); Thread.sleep(1000);
            while (!leveledUp) {
                if (player.getExp() > 100) {
                    player.addExp(-100);
                    player.addLevel(1);
                    int previousLevel = player.getLevel()-1;
                    text.slowTextSpeed(player.getName() + " LEVELED UP " + previousLevel + " -> " + player.getLevel() + "", 20);
                    Thread.sleep(100);
                    int[] statIncrease = {player.getStrength() + (int)((Math.random()*4)+2), player.getMaxHealth() + (int)((Math.random()*5) + 6), player.getSpeed() + (int)((Math.random()*2)+1)};
                    text.slowTextSpeed(" > Strength: " + player.getStrength() + " -> " + statIncrease[0] + "\n > Health: " + player.getMaxHealth() + " -> " + statIncrease[1] + "\n > Speed: " + player.getSpeed() + " -> " + statIncrease[2], 20);
                    Thread.sleep(100);
                    player.setStrength(statIncrease[0]); player.setMaxHealth(statIncrease[1]); player.setSpeed(statIncrease[2]);
                } 
                if(player.getExp() <= 100) {
                    leveledUp = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                        
                        // text.slowTextSpeed(Game.getGame().getPlayer().getName() + " has " + playerHealth + " health remaining", 7); 
                        // text.slowTextSpeed(enemy.getName() + " has " + enemyHealth + " health remaining", 7);  Don't need anymore, we have gui
                        if(enemyHealth<=0){
                                    text.slowTextSpeed(enemy.getName() + " Died! YOU WIN!!!", 7);
                                    Game.getGame().getPlayer().setInFight(false);
                                    isTotalHealth = false;
                                    return true;
                        }else if(playerHealth<= 0){
                                    text.slowTextSpeed(enemy.getName() + " Won! YOU DIED!!!", 7);
                                    Game.getGame().getPlayer().gameOver();
                                    Game.getGame().getPlayer().setInFight(false);
                                    isTotalHealth = false;
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
        Thread.sleep(1000);
        eDamage = (int)((Math.random() * 0.5 + 0.8) * (eDamage)); // Create a damage range of 0.8x and 1.2x damage
        if ((int)(Math.random()*16 + 1) == 16) {
            eDamage = eDamage * 2;
            System.out.println("CRITICAL HIT!!!");
        }
        playerHealth-= eDamage;
        text.slowTextSpeed("It delt " + eDamage + " Damage", 7);
        Thread.sleep(1000);
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
                    text.slowTextSpeed(Game.getGame().getPlayer().getName() + " got the effect " + playerEffect.get(i).getName(), 7);
                    Thread.sleep(1000);
                    playerHealth-= dam;
                    playerSpeed -= sped;
                    text.slowTextSpeed("Speed decreased by " + sped + " and delt " + dam + " damage", 7);
                    Thread.sleep(1000);
                    playerEffect.get(i).setTurnCount(playerEffect.get(i).getTurnCount()+1);
                }else{
                    text.slowTextSpeed("effect is over " + playerEffect.get(i).getName(), 7);
                    Thread.sleep(1000);
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
                    text.slowTextSpeed(enemy.getName() + " got the effect " + enemyEffect.get(i).getName(), 7);
                    Thread.sleep(1000);
                    enemyHealth-= dam;
                    enemySpeed -= sped;
                    text.slowTextSpeed("Speed decreased by " + sped + " and delt " + dam + " damage", 7);
                    Thread.sleep(1000);
                    playerEffect.get(i).setTurnCount(playerEffect.get(i).getTurnCount()+1);
                }else{
                    text.slowTextSpeed("effect is over " + enemyEffect.get(i).getName(), 7);
                    Thread.sleep(1000);
                    playerEffect.get(i).setTurnCount(0);
                    enemyEffect.remove(i);
                }

            }
        }
        } catch (Exception e) {
            
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
        if(!isTotalHealth) {
            enemyTotalHealth = enemyHealth;
            isTotalHealth = true;
        }
        try {
            displayFightGraphics();
            text.slowTextSpeed("Do you want to use a WEAPON or a ITEM", 7);
                        
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
                Thread.sleep(1000);
                double strengthMod = (Game.getGame().getPlayer().getStrength() / 100.0) + 1;
                pDamge = (int)((Math.random() * 0.5 + 0.8) * ((pDamge) * strengthMod)); // Create a damage range of 0.8x and 1.2x damage
                if ((int)(Math.random()*16 + 1) == 16) {
                    pDamge = pDamge * 2;
                    System.out.println("CRITICAL HIT!!!");
                }
                enemyHealth -= pDamge;
                text.slowTextSpeed("It delt " + pDamge + " Damage", 7);
                Thread.sleep(1000);
                enemyEffect.add(pWeapon.getEffect());
                computeEffects(false);
            }else{
                Item item = askItem();
                if(item != null){
                    playerHealth += item.getEffect().getHealth();
                    playerSpeed += item.getEffect().getSpeedChange();
                    text.slowTextSpeed("health up by " + item.getEffect().getHealth() + " and speed went up by " + item.getEffect().getSpeedChange(), 7);
                    Thread.sleep(1000);
                    for (int i = 0; i < Game.getGame().getPlayer().getInventory().getItems().size(); i++) {
                        if(item.getName().equals(Game.getGame().getPlayer().getInventory().getItems().get(i).getName())){
                            Game.getGame().getPlayer().getInventory().getItems().remove(i);
                                }
                            }
                        }else{
                        text.slowTextSpeed("you dont have an item avaliable (should have visited a tims...)", 7);
                        Thread.sleep(1000);
                            
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
    private void displayFightGraphics() {
        String spaceBetweenLine = "";
        System.out.println("_______________________________________________________");
        System.out.println("|                                                     |");
        int NameLength = enemy.getName().length();
        int HealthPercent = (int)((enemyHealth / enemyTotalHealth) * 20);
        String HealthBar = "";
        for (int i = 1; i < 21 ; i++) {
            if(HealthPercent == i) {
                HealthBar += "|";
            } else {
                HealthBar += "-";
            }
        }
        for (int i = 0; i < 51 - NameLength - HealthBar.length(); i++) { // 51 is to total length I just counted :/
            spaceBetweenLine += " ";
        }
        
        System.out.println("| " + enemy.getName() + " " + HealthBar + spaceBetweenLine + "|");

        int enemyTotalHealthInt = (int)(enemyTotalHealth);
        String spaceBetweenHealth = "";
        String enemyHealthDisplay = enemyHealth + "/" + enemyTotalHealthInt;
        for (int i = 0; i < 39 - spaceBetweenLine.length() - enemyHealthDisplay.length(); i++) { // 51 - ENEMY HEALTH:  string
            spaceBetweenHealth += " ";
        }
        System.out.println("|                                                     |");
        System.out.println("|" + spaceBetweenHealth + "Enemy Health: " + enemyHealthDisplay + spaceBetweenLine + "|");
        System.out.println("|                                                     |");
        System.out.println("|                                                     |");
        NameLength = Game.getGame().getPlayer().getName().length();
        spaceBetweenLine = "";
        HealthPercent = (int)((playerHealth / playerTotalHealth)*20);
        int playerTotalHealthInt = (int)(playerTotalHealth);
        HealthBar = "";
        for (int i = 1; i < 21 ; i++) {
            if(HealthPercent == i) {
                HealthBar += "|";
            } else {
                HealthBar += "-";
            }
        }
        for (int i = 0; i < 51 - NameLength - HealthBar.length(); i++) { // 51 is to total length again
            spaceBetweenLine += " ";
        }
        spaceBetweenHealth = "";
        String playerHealthDisplay = playerHealth + "/" + playerTotalHealthInt;
        for (int i = 0; i < 38 - spaceBetweenLine.length() - playerHealthDisplay.length(); i++) { // 51 again - PLAYER HEALTH:  string
            spaceBetweenHealth += " ";
        }
        System.out.println("|" + spaceBetweenLine + "Player Health: " + playerHealthDisplay + spaceBetweenHealth + "|");
        System.out.println("|                                                     |");
        System.out.println("|" + spaceBetweenLine + HealthBar + " " + Game.getGame().getPlayer().getName() + " |");                                                     
        System.out.println("|_____________________________________________________|");
    }    
}