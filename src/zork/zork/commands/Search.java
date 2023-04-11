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
        for (Item e : Game.getGame().getPlayer().getRoom().getGroundItems()) {
            str = str + e.getName();
        }
        return str;
    }

    
}
