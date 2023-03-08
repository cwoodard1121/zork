package zork;

import java.util.HashMap;

public class Constants {
    public static final class PlayerConstants {
        public static final int DEFAULT_HEALTH = 100;
    }

    public static final class CommandConstants {
        public static final HashMap<String,Command> commands = new HashMap<>();


        /**
         * This static block registers every command, do this after you make one.
         */
        static {
            commands.put("go", new Go("go"));
        }
    }
}
