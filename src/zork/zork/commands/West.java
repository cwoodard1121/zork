package zork.commands;

import zork.Command;
import zork.Constants;
import zork.Exit;
import zork.Game;
import zork.Utils;

public class West extends Command {
    public West(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for (Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if (e.getDirection().equalsIgnoreCase("w")) {
                Game.getGame().getPlayer().setCurrentRoom(e.getAdjacentRoom());
                return e.getAdjacentRoom().getDescription();
            }
            
        }
        return "There is no room to the West";
    }

    
    
}

