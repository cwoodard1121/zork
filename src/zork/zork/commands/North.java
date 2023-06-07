package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class North extends Command {

    public North(String name) {
        super(name);
        addAlias("n"); // adds N to work with this command aswell
    }

    @Override
    public String runCommand(String... args){ // Command that deals with north movement
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("n")) {
                if (!e.getAdjacentRoom().isLocked() && !e.getIsExitLocked()) { // Checks if its locked before letting you in
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
            return "There is no room to the North";
        }
    }

