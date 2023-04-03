package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class East extends Command {

    public East(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if(e.getDirection().equalsIgnoreCase("east")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                return e.getAdjacentRoom().getDescription();
            }
        }
        return "There is no room to the East";
        


    }
}

