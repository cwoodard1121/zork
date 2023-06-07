package zork.commands;



import java.io.IOException;

import zork.Command;
import zork.Exit;
import zork.Game;

public class Go extends Command {
    
    public Go(String name) {
        super(name);
        addAlias("g");
    }

    @Override
    public String runCommand(String... args) { // Does the same thing as Direction Commands.
        if(args.length > 0) {
            String direction = args[0].substring(0, 1);
            for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
                if(e.getDirection().equalsIgnoreCase(direction)) {
                    try {
                    if (!e.getAdjacentRoom().isLocked() && !e.getIsExitLocked()) {        
                        Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                        e.getAdjacentRoom().printAscii();                    
                        return e.getAdjacentRoom().getDescription();
                    } else {
                        if(e.hasLockedDescription()){
                            return e.getLockedDescription();
                        }
                        return e.getAdjacentRoom().getLockedMessage();
                    }
                    } catch (Exception ignored) {
                        System.out.println("ascii not loaded.");
                    }
                }
            }
            if (direction.equalsIgnoreCase("u"))
                return "There is no room above you";
            else if (direction.equalsIgnoreCase("d")) // To display different messages for each direction
                return "There is no room below you";    
            else 
                return "There is no room to the " + args[0];
        }
        return "You need to supply a direction.";
    }



}
