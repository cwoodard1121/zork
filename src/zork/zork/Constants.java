package zork;

import java.util.HashMap;

import zork.commands.Go;
import zork.commands.Stop;

public class Constants {
    public static final class PlayerConstants {
        public static final int DEFAULT_HEALTH = 100;
    }

    public static final class CommandConstants {
        public static final HashMap<String,Command> commands = new HashMap<>();
    }
        /**
         * This static block registers every command, do this after you make one.
         */
    public static void initCommands() {
        CommandConstants.commands.put("go", new Go("go"));
        CommandConstants.commands.put("stop", new Stop("stop"));
    }
}
