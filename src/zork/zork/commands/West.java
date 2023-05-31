package zork.commands;


import zork.Command;
import zork.Exit;
import zork.Game;

public class West extends Command {
    public West(String name) {
        super(name);
        addAlias("w");
    }

    @Override
    public String runCommand(String... args){
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("w")) {
                if (!e.getAdjacentRoom().isLocked()) {
                    Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                    e.getAdjacentRoom().printAscii();
                    return e.getAdjacentRoom().getDescription();
                } else {
                    return e.getAdjacentRoom().getLockedMessage();
                }
            }
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room to the West";
        }
    }

