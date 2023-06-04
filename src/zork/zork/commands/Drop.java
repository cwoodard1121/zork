package zork.commands;

import zork.Command;
import zork.Game;
import zork.Item;
import zork.entites.Player;

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
            String command = "";
            for (int j = 0; j < args.length; j++) {
                command+=args[j] + " ";
            }
            command = command.substring(0, command.length()-1);
            if (command.equalsIgnoreCase(inventory.getItem(i).getName())) {
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
        return "You do not have that in your inventory";
        


    }
}
