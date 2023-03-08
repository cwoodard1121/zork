package zork.commands;

import zork.Command;
import zork.Game;

public class Look extends Command {
    public Look(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return "in this room we have:" + Game.getGame().getPlayer().getRoom().getDescription();
    }

    
}
