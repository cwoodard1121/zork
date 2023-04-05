package zork.commands;

import zork.Command;
import zork.Exit;
import zork.Game;

public class Go extends Command {
    
    public Go(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        if(args.length > 0) {
            String direction = args[0];
            for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
                if(e.getDirection().equalsIgnoreCase(direction)) {
                    Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                    return e.getAdjacentRoom().getDescription();
                }
            }
            return "There is no room to the" + direction;
        }
        return "You need to supply a direction.";
    }



}
