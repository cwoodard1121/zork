package zork.commands;

import zork.Command;
import zork.Game;
import zork.Exit;

public class Up extends Command {

    public Up(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if(e.getDirection().equalsIgnoreCase("u")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                return e.getAdjacentRoom().getDescription();
            }
        }
        return "There is no room above you";


    }
}