package zork.commands;

import zork.Command;
import zork.Game;

public class Search extends Command {
    public Search(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return Game.getGame().getPlayer().getRoom().finish later;
    }

    
}
