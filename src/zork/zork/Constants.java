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
        public static final int MAX_INVENTORY_WEIGHT = 100;
    }

    public static final class EntityConstants {
        public static final int MAX_INVENTORY_WEIGHT = 25;
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
     * Registers all commands in the zork.commands package using reflection
     */
    public static void initCommands() {
        try {
            for(Class<?> command : Utils.getClasses("zork.commands")) {
                Utils.registerCommand((Class<? extends Command>) command);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }






}
