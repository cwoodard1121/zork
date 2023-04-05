package zork.commands;

import zork.Command;
import zork.Constants;
import zork.Exit;
import zork.Game;
import zork.Utils;

public class Where extends Command {
    public Where(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return Game.getGame().getPlayer().getCurrentRoom().getRoomName();
    }

    
    
}
