package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class Down extends Command {

    public Down(String name) {
        super(name);
        addAlias("d"); // Allows d to also run this command
    }

    @Override
    public String runCommand(String... args){ // Command allows for down direction
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("d")) {
                if (!e.getAdjacentRoom().isLocked() && !e.getIsExitLocked()) { // Changes room, if the room or exit is locked doesnt change room.
                    Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                    e.getAdjacentRoom().printAscii();
                    return e.getAdjacentRoom().getDescription();
                } else {
                    if(e.hasLockedDescription()){
                        return e.getLockedDescription();
                    }
                    return e.getAdjacentRoom().getLockedMessage();
                }
            }
        } catch (Exception exception) {
            return "no ascii art";
        } 
            }
            return "There is no room below you";
        }
    }

