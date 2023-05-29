package zork.commands;

import java.util.ArrayList;
import zork.Command;
import zork.Game;
import zork.Item;
import zork.entites.Player;

public class Pickup extends Command {

    public Pickup(String name) {
        super(name);
        addAlias("Pick");
    }

    @Override
    public String runCommand(String... args) {
        Player player = Game.getGame().getPlayer();
        ArrayList<Item> groundItems = player.getCurrentRoom().getGroundItems();
        boolean hasItem = false;
        String itemList = "";
        boolean isSpecifiedItem = args.length > 0;
        for (Item item : groundItems) {
            hasItem = true;
            if (isSpecifiedItem) {
                for (int i = 0; i < args.length; i++) {
                    if (item.getName().equalsIgnoreCase(args[i])) {
                        player.getInventory().addItem(item);
                        player.getCurrentRoom().removeFromGround(item);
                        itemList = item.getName() + ", ";
                    }
                }
            } else {
                player.getInventory().addItem(item);
                player.getCurrentRoom().removeFromGround(item);
                itemList = item.getName() + ", ";
            }
        }  
        if (isSpecifiedItem && hasItem && itemList.length() >= 1) 
            return "You have picked up " + itemList;
        else if (isSpecifiedItem && hasItem)
            return "There are no items of the name " + itemList; 
        else if (hasItem) 
            return "You have picked up" + itemList;
        else
            return "There are no items in this room";
    }
}
