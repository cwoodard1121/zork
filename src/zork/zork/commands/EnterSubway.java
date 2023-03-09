package zork.commands;

import zork.Command;
import zork.Constants;

public class EnterSubway extends Command {
    public EnterSubway(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Constants.subwaySound();
        return "You have entered the subway station.";
    }

    
    
}
