package datatypes;

import zork.Command;
import zork.Game;

public class CommandNotFoundException extends Exception {
    String error;

    public CommandNotFoundException(String error) {
        this.error = error;
    }

    @Override
    public void printStackTrace() {
        if(Game.isTesting) {
            System.out.println("Command not found: " + this.error);
        } else {
            System.out.println("Invalid command.");
        }
    }

    
    
}
