package zork.commands;

import zork.Command;
import zork.Game;

public class ExportRooms extends Command {
    public ExportRooms(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Game.getGame().exportRooms();
        return "Exported rooms.";
    }
}
