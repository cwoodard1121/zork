package zork.commands;

import java.io.IOException;

import zork.Command;
import zork.Game;

public class Look extends Command {
    public Look(String name) {
        super(name);
        addAlias("l");
    }

    @Override
    public String runCommand(String... args) {
        try {
            Game.getGame().getPlayer().getCurrentRoom().printAscii();
        } catch (IOException e) {
            System.err.println("Graphics Couldn't Load");
        }
        return Game.getGame().getPlayer().getCurrentRoom().shortDescription();

    }

    
}
