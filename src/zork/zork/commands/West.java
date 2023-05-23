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
        for (Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try {
            if (e.getDirection().equalsIgnoreCase("w")) {
                Game.getGame().getPlayer().setCurrentRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }  
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room to the South";
        }
    }

