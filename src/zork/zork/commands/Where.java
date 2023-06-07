package zork.commands;

import zork.Command;
import zork.Game;

public class Where extends Command {
    public Where(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) { // Says the name of the room
        return Game.getGame().getPlayer().getCurrentRoom().getDisplayName();
    }

    
    
}
