package zork.commands;

import zork.Command;
import zork.Constants;
import zork.Utils;


/*
 * use this for reference on how to use commands & play sounds. nothing else its horrible
 */
@Deprecated
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
