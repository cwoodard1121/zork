package zork.commands;

import zork.Command;
import zork.Utils;

public class Skip extends Command {

    public Skip(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) throws InterruptedException { // Skips the song currently playing
        Utils.SoundHandler.skip();
        return "Skipped!";
    }
    
}
