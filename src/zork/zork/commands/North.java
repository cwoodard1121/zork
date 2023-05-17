package zork.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import zork.Command;
import zork.Game;
import zork.Exit;

public class North extends Command {

    public North(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) throws FileNotFoundException, IOException {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            if(e.getDirection().equalsIgnoreCase("n")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }
        }
        return "There is no room to the North";


    }
}
