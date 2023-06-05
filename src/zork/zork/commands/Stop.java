package zork.commands;

import zork.Command;
import zork.Game;
import zork.Utils.SoundHandler;

public class Stop extends Command{
    public Stop (String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
      SoundHandler.stop();
      return "music stopped";
    }

    
}
