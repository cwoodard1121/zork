package zork.commands;

import zork.Command;
import zork.Game;

public class Stop extends Command{
    public Stop (String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
      Game.getGame().finished = true;
      return "Exitting game...";  
    }

    
}
