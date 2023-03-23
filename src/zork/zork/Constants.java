package zork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import sun.audio.*;
import zork.commands.EnterSubway;
import zork.commands.Go;
import zork.commands.Stop;

public class Constants {
    public static final class PlayerConstants {
        public static final int DEFAULT_HEALTH = 100;
    }

    public static final class SoundConstants {
        public static final HashMap<String,Boolean> playSounds = new HashMap<>();
    }

    public static final class RenderConstants {
        public static final int BORDER_LENGTH = 20;
        public static final int BORDER_HEIGHT = 20;
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
        CommandConstants.commands.put("entersubway", new EnterSubway("entersubway"));
    }






}
