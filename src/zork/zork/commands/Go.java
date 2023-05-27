package zork.commands;



import zork.Command;
import zork.Exit;
import zork.Game;

public class Go extends Command {
    
    public Go(String name) {
        super(name);
        addAlias("g");
    }

    @Override
    public String runCommand(String... args) {
        if(args.length > 0) {
            String direction = args[0].substring(0, 1);
            for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
                if(e.getDirection().equalsIgnoreCase(direction)) {
                    Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                    try {
                    e.getAdjacentRoom().printAscii();
                    } catch (Exception ignored) {
                        System.out.println("ascii not loaded.");
                    }
                    return e.getAdjacentRoom().getDescription();
                }
            }
            if (direction.equalsIgnoreCase("u"))
                return "There is no room above you";
            else if (direction.equalsIgnoreCase("d"))
                return "There is no room below you";    
            else 
                return "There is no room to the " + args[0];
        }
        return "You need to supply a direction.";
    }



}
