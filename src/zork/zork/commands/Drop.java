package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class Drop extends Command {

    public Drop(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for(int i = 0; i < Game.getGame().getPlayer().getInventory().getItemCount(); i++) {
            if (args[0].equalsIgnoreCase(Game.getGame().getPlayer().getInventory().getItem(i).getName())) {
                int weight = Game.getGame().getPlayer().getInventory().getCurrentWeight();
                weight = weight - Game.getGame().getPlayer().getInventory().getItem(i).getWeight();
                Game.getGame().getPlayer().getInventory().setCurrentWeight(weight);
                String getItemName = Game.getGame().getPlayer().getInventory().getItem(i).getName();
                Game.getGame().getPlayer().getInventory().removeItem(Game.getGame().getPlayer().getInventory().getItem(i));
                return "You dropped your " + getItemName + " on the ground";
            }
        }
        return "You do not have a " + args[0];
        


    }
}
