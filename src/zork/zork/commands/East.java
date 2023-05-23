package zork.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import zork.Command;
import zork.Game;
import zork.Exit;

public class East extends Command {

    public East(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args){
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("e")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }
            return "There is no room to the East";
        } catch (Exception exception) {} 
            }
            return "ya done goofed";
        }
    }

