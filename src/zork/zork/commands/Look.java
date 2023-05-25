package zork.commands;

import zork.Command;
import zork.Game;

public class Look extends Command {
    public Look(String name) {
        super(name);
        addAlias("l");
    }

    @Override
    public String runCommand(String... args) {
        return Game.getGame().getPlayer().getCurrentRoom().shortDescription();
    }

    
}
