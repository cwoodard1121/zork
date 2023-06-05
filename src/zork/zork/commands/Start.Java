package zork.commands;

import zork.Command;
import zork.Utils;

public class Start extends Command {
    public Start(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Utils.SoundHandler.startAfterInterruption();
        Utils.SoundHandler.skip();
        return "";
    }

    
}