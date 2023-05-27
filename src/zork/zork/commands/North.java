package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class North extends Command {

    public North(String name) {
        super(name);
        addAlias("n");
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("n")) {
                if (!e.getAdjacentRoom().isLocked()) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
                }
            }
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room to the North";
        }
    }


