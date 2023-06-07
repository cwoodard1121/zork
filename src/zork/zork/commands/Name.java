package zork.commands;

import zork.Command;
import zork.Game;

public class Name extends Command {
    public Name(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) { // Returns the player's name
        return "Your name is" + Game.getGame().getPlayer().getName();
    }

    
}

