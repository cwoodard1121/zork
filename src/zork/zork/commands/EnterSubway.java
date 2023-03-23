package zork.commands;

import zork.Command;
import zork.Constants;
import zork.Utils;

public class EnterSubway extends Command {
    public EnterSubway(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Utils.subwaySound();
        return "You have entered the subway station.";
    }

    
    
}
