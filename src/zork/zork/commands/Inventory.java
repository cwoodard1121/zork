package zork.commands;


import zork.Command;
import zork.Game;
import zork.Item;
import zork.Constants.PlayerConstants;
import zork.entites.Player;

public class Inventory extends Command {
    public Inventory(String name) {
        super(name);
        addAlias("i");
    }
    @Override
    public String runCommand(String... args) {
        
        for (Item item : Game.getGame().getPlayer().getInventory().getItems()) {
            System.out.println("> " + item.getName());
           
        }
        System.out.println("Health: " + Game.getGame().getPlayer().getHealth() +"/" + Game.getGame().getPlayer().getMaxHealth());
        System.out.println("Money: " + Game.getGame().getPlayer().getMoney());
        System.out.println("Your Current Weight: " + Game.getGame().getPlayer().getInventory().getCurrentWeight() + "/" + PlayerConstants.MAX_INVENTORY_WEIGHT);
   
        return "";
    }    
}
