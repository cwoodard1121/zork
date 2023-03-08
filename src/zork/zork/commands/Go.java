package zork.commands;

import zork.Command;

public class Go extends Command {
    
    public Go(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        return "Can't go anywhere yet lol";
    }



}
