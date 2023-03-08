package zork;

import java.util.HashMap;

import zork.commands.Go;

public class CommandWords {
  // a constant array that holds all valid command words
  private static final HashMap<String,Command> commands = new HashMap<>();



  /**
   * PUT COMMAND DEFINITIONS IN THIS STATIC
   * static blocks allow you to quickly define things as soon as the class is accessed
   * allowing things to be initialized right before we need them
   */
  static {
    commands.put("go", new Go("go"));
  }
  /**
   * Constructor - initialise the command words.
   */
  public CommandWords() {
  
    // nothing to do at the moment...
  }

  /**
   * Check whether a given String is a valid command word. Return true if it is,
   * false if it isn't.
   **/
  public boolean isCommand(String aString) {
    //TODO: FIX
    return true;
  }

  /*
   * Print all valid commands to System.out.
   */
  public void showAll() {
    //TODO: FIX
  }
}
