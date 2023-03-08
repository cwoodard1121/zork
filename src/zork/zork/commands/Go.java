package zork.commands;

import zork.Command;
import zork.Game;

public class Go extends Command {
    
    public Go(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        if(args.length > 0) {
            String direction = args[0];
            //FUTURE ADD ROOMAT Game.getGame().getPlayer().changeRoom(Game.getGame().roomAt(direction));
            return "Went to " + direction;
        }
        return "You need to supply a direction.";
    }



}
