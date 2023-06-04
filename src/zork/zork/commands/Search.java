package zork.commands;

import zork.Command;
import zork.Game;
import zork.Item;

public class Search extends Command {
    public Search(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        String str = "";
        if(Game.getGame().getPlayer().getCurrentRoom().getGroundItems().size() == 0) return "You searched and found... nothing.";
        for (Item e : Game.getGame().getPlayer().getCurrentRoom().getGroundItems()) {
            str = str + e.getName() + ", ";
        }
        str = str.substring(0, str.length()-2);
        return str;
    }

    
}
