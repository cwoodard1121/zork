package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class North extends Command {

    public North(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if(e.getDirection().equalsIgnoreCase("n")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                return e.getAdjacentRoom().getDescription();
            }
        }
        return "There is no room to the North";


    }
}
