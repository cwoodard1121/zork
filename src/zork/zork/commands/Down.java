package zork.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import zork.Command;
import zork.Game;
import zork.Exit;

public class Down extends Command {

    public Down(String name) {
        super(name);
        addAlias("d");
    }

    @Override
    public String runCommand(String... args) {
        
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("d")) {
                if (!e.getAdjacentRoom().isLocked()) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
                }
                return e.getAdjacentRoom().getDisplayName() + " is Locked or Unavalible at this time";
            }
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room below you";
        }
    }

