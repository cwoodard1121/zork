package zork.commands;


import zork.Command;
import zork.Game;
import zork.Exit;

public class East extends Command {

    public East(String name) {
        super(name);
        addAlias("e"); // Allows typing e to also run this command
    }

    @Override
    public String runCommand(String... args){
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) { // Handles changing directions to the east
            try{
            if(e.getDirection().equalsIgnoreCase("e")) {
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
            return "There is no room to the East";
        }
    }

