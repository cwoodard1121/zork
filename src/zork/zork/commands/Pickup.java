package zork.commands;

import java.util.ArrayList;
import zork.Command;
import zork.Game;
import zork.Item;
import zork.Constants.PlayerConstants;
import zork.entites.Player;

public class Pickup extends Command {

    public Pickup(String name) {
        super(name);
        addAlias("pick");
    }
    @Override
    public String runCommand(String... args) {
        Player player = Game.getGame().getPlayer();
        ArrayList<Item> groundItems = player.getCurrentRoom().getGroundItems();
        if(groundItems.size() == 0) return "There are no items in the room.";
        boolean hasItem = false;
        String itemList = "";
        boolean isSpecifiedItem = args.length > 0;
        for (int i = 0; i < groundItems.size(); i++) {
            Item item = groundItems.get(i);
            hasItem = true;
            String command = "";
            for (int j = 0; j < args.length; j++) {
                command+=args[j] + " ";
            }
            if (isSpecifiedItem) {
                        if (command.contains(item.getName().toLowerCase())) {
                            if(player.getInventory().getCurrentWeight() + item.getWeight()>PlayerConstants.MAX_INVENTORY_WEIGHT){
                                return "you are carrying too much stuff to pick that up, drop stuff somewhere";
                            }else{
                                player.getInventory().addItem(item);
                                player.getCurrentRoom().removeFromGround(item);
                                itemList = item.getName();
                            }
                            
                        }
                    
                } else {
                    if(player.getInventory().getCurrentWeight() + item.getWeight()>PlayerConstants.MAX_INVENTORY_WEIGHT){
                        return "you are carrying too much stuff to pick that up, drop stuff somewhere";
                    }else{
                        player.getInventory().addItem(item);
                        player.getCurrentRoom().removeFromGround(item);
                        itemList = item.getName() + ", ";
                    }
                }
        }  
        if (isSpecifiedItem && hasItem && itemList.length() >= 1) 
            return "You have picked up " + itemList;
        else if (isSpecifiedItem && hasItem)
            return "There are no items of the name " + itemList; 
        else if (hasItem) 
            return "You have picked up " + itemList;
        else
            return "There are no items in this room";
    }
}
