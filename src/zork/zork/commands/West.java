package zork.commands;


import zork.Command;
import zork.Exit;
import zork.Game;

public class West extends Command { 
    public West(String name) {
        super(name);
        addAlias("w"); // Typing w also runs this command
    }

    @Override
    public String runCommand(String... args){ // Command deals with the west movement
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("w")) {
                if (!e.getAdjacentRoom().isLocked() && !e.getIsExitLocked()) { // Checks if 
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
            return "There is no room to the West";
        }
    }

