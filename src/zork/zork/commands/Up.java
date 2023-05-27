package zork.commands;


import zork.Command;
import zork.Game;
import zork.Exit;

public class Up extends Command {

    public Up(String name) {
        super(name);
        addAlias("u");
    }

    @Override
    public String runCommand(String... args) {
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("u")) {
                Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                e.getAdjacentRoom().printAscii();
                return e.getAdjacentRoom().getDescription();
            }
        } catch (Exception exception) {
            return "ya done goofed";
        } 
            }
            return "There is no room above you";
        }
    }