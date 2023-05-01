package zork.commands;

import zork.Command;
import zork.Game;
import zork.Item;
import zork.entites.Player;
import zork.Exit;

public class Drop extends Command {

    public Drop(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Game game = Game.getGame();
        for(int i = 0; i < game.getPlayer().getInventory().getItemCount(); i++) {
            zork.Inventory inventory = game.getPlayer().getInventory();
            Player player = game.getPlayer();
            if (args[0].equalsIgnoreCase(inventory.getItem(i).getName())) {
                int weight = inventory.getCurrentWeight();
                weight = weight - inventory.getItem(i).getWeight();
                player.getInventory().setCurrentWeight(weight);
                Item item = inventory.getItem(i);
                String getItemName = inventory.getItem(i).getName();
                game.getPlayer().getInventory().removeItem(Game.getGame().getPlayer().getInventory().getItem(i));
                player.getCurrentRoom().addItemGround(item);

                return "You dropped your " + getItemName + " on the ground";
            }
        }
        return "You do not have a " + args[0];
        


    }
}
