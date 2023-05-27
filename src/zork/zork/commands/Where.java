package zork.commands;

import zork.Command;
import zork.Game;

public class Where extends Command {
    public Where(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return Game.getGame().getPlayer().getCurrentRoom().getDisplayName();
    }

    
    
}
