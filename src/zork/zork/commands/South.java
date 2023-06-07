package zork.commands;



import zork.Command;
import zork.Game;
import zork.Exit;

public class South extends Command {

    public South(String name) {
        super(name);
        addAlias("s"); // s also allows you to use this command with s
    }

    @Override
    public String runCommand(String... args){ // Command that deals with south movement
        for(Exit e : Game.getGame().getPlayer().getCurrentRoom().getExits()) {
            try{
            if(e.getDirection().equalsIgnoreCase("s")) { // Checks if the room is locked
                if (!e.getAdjacentRoom().isLocked() && !e.getIsExitLocked()) {
                    Game.getGame().getPlayer().changeRoom(e.getAdjacentRoom());
                    e.getAdjacentRoom().printAscii();
                    return e.getAdjacentRoom().getDescription();
                } else {
                    if(e.hasLockedDescription()){
                        return e.getLockedDescription();
                    }
                    return e.getAdjacentRoom().getLockedMessage();
                }
            }
            } catch (Exception exception) {
                exception.printStackTrace();
            } 
        }
            return "There is no room to the South";
    }

}
