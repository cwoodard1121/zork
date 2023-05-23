package zork.commands;

import zork.Command;
import zork.Exit;
import zork.Game;

public class Exits extends Command {
    public Exits(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        String ans = "";
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            ans = ans + e.getDirection() + " " + e.getAdjacentRoom().getRoomName() + "\n";
        }
        return ans;    
    }

    
}
