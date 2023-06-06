package zork.commands;

import java.util.ArrayList;
import zork.Command;
import zork.Game;
import zork.Item;
import zork.entites.Player;
import zork.items.Weapon;
public class Use extends Command {
    public Use(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        String command = "";
        for (int i = 0; i < args.length; i++) {
            command+=args[i] + " ";
        }
        command = command.substring(0, command.length()-1);
        Player p = Game.getGame().getPlayer();
        ArrayList<Item> arr = p.getInventory().getItems();
        ArrayList<Item> arr2 = p.getInventory().getItemsWithEffects();
        int location = 0;
        for(int i = 0; i<arr.size(); i++){
            if(arr.get(i).getName().equalsIgnoreCase(command))
                location = i;
        }
        
            for(int i = 0; i<arr2.size(); i++){
                try {
                    
                
                    if(command.contains(arr2.get(i).getName().toLowerCase())){
                        if(p.getHealth() + arr2.get(i).getEffect().getHealth()>Game.getGame().getPlayer().getMaxHealth()){
                            p.setHealth(Game.getGame().getPlayer().getMaxHealth());
                        }else{
                            p.setHealth(p.getHealth() + arr2.get(i).getEffect().getHealth());
                        }
                        Game.getGame().getPlayer().getInventory().getItems().remove(location);
                        return "";
                    }

                } catch (Exception e) {
                    System.out.println("You cant use that item right now");
                }
            }
        
        return "";
    }

    
}