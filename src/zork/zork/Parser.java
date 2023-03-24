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

    if(words.length > 1) {
    int i = 1;
    for(String word : words) {
      if(i == 1) {
        if(!CommandConstants.commands.containsKey(word)) {
          throw new CommandNotFoundException(inputLine);
        }
        c = Constants.CommandConstants.commands.get(word);
        i++;
        continue;
      }
      args[i] = word;
      i++;
    }
    } else {
      if(!CommandConstants.commands.containsKey(words[0])) {
        throw new CommandNotFoundException(inputLine);
      }
      c = Constants.CommandConstants.commands.get(words[0]);
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
