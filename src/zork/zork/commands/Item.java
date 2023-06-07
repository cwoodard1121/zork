package zork.commands;

import zork.Command;
import zork.Game;

public class Item extends Command {
    public Item(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) { // Command to go into the ITEM menu in battle
        if(Game.getGame().getPlayer().isInFight() == true){
        Game.getGame().getPlayer().setItemMenu(true);
        Game.getGame().getPlayer().setChoosingMenu(false);
        return "going into the menu";
        }else{
            return "not in a fight";
        }


    }
}
