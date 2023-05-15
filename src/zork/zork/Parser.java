package zork;

import java.util.Scanner;

import datatypes.CommandNotFoundException;
import zork.Constants.CommandConstants;

public class Parser {
  private Scanner in;
  private String[] args;

  public Parser() {
    in = new Scanner(System.in);
  }

  public Command getCommand() throws CommandNotFoundException {
    String[] words;
    String inputLine = "";
    Command c = new Command();

    System.out.print("> "); // print prompt

    inputLine = in.nextLine();

    words = inputLine.split(" ");
    args = new String[words.length - 1];
    if(words.length > 1) {
    int i = 0;
    for(String word : words) {
      if(i == 0) {
        if(!CommandConstants.commands.containsKey(word.toLowerCase())) {
          throw new CommandNotFoundException(inputLine);
        }
        c = Constants.CommandConstants.commands.get(word.toLowerCase());
        i++;
        continue;
      }
      args[i - 1] = word;
      i++;
    }
    } else {
      if(!CommandConstants.commands.containsKey(words[0].toLowerCase())) {
        throw new CommandNotFoundException(inputLine);
      }
      c = Constants.CommandConstants.commands.get(words[0].toLowerCase());
    }
    return c;
  }

  /**
   * Print out a list of valid command words.
   */

  public String[] getParams() {
    return args;
  }
}
