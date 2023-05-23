package zork.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import zork.Command;
import zork.Game;
import zork.Exit;

public class Down extends Command {

    public Down(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("d")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room below you";
        }
    }

