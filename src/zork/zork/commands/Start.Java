package zork.commands;

import zork.Command;
import zork.Game;

public class Start extends Command {
    public Start(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return "Starting Game...";
    }

    
}