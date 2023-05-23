package zork.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import zork.Command;
import zork.Constants;
import zork.Exit;
import zork.Game;
import zork.Utils;

public class West extends Command {
    public West(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        try {
        for (Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if (e.getDirection().equalsIgnoreCase("w")) {
                Game.getGame().getPlayer().setCurrentRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }
            
        }
        return "There is no room to the West";
    } catch (Exception exception) {} 

    return "ya done goofed";
    }
}

