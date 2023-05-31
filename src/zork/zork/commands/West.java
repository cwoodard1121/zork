package zork.commands;


import zork.Command;
import zork.Exit;
import zork.Game;

public class West extends Command {
    public West(String name) {
        super(name);
        addAlias("w");
    }

    @Override
    public String runCommand(String... args) {
        for (Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try {
            if (e.getDirection().equalsIgnoreCase("w")) {
                Game.getGame().getPlayer().setCurrentRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }  
        } catch (Exception exception) {
            return "no ascii art";
        } 
            }
            return "There is no room to the West";
        }
    }

