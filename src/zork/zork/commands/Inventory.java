package zork.commands;


import zork.Command;
import zork.Game;
import zork.Item;

public class Inventory extends Command {
    public Inventory(String name) {
        super(name);
        addAlias("i");
    }
    @Override
    public String runCommand(String... args) {
        String ans = "";
        for (Item item : Game.getGame().getPlayer().getInventory().getItems()) {
            ans += item.getName() + ", ";
        }
        return ans;
    }    
}
