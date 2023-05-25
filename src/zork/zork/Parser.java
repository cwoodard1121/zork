package zork;

import java.util.Scanner;
import java.util.Map.Entry;

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
      boolean hasAlias = false;
      String alias = "";
      if(i == 0) {
        if(!CommandConstants.commands.containsKey(word.toLowerCase())) {
          for(Entry<String,Command> entry : CommandConstants.commands.entrySet()) {
            Command g = entry.getValue();
            for(String a : g.getAliases()) {
              if(a.toLowerCase().equals(word.toLowerCase())) {
                alias = g.getName().toLowerCase();
                hasAlias = true;
              }
            }
          }
          if(!hasAlias) {
            throw new CommandNotFoundException(inputLine);
          }
        }
        if(!hasAlias) {
          c = Constants.CommandConstants.commands.get(word.toLowerCase());
        } else {
          c = CommandConstants.commands.get(alias);
        }
        i++;
        continue;
      }
      args[i - 1] = word;
      i++;
    }
    } else {
      boolean hasAlias = false;
      String alias = "";
      if(!CommandConstants.commands.containsKey(words[0].toLowerCase())) {
        for(Entry<String,Command> entry : CommandConstants.commands.entrySet()) {
          Command g = entry.getValue();
          for(String a : g.getAliases()) {
            if(a.toLowerCase().equals(words[0].toLowerCase())) {
              alias = g.getName().toLowerCase();
              hasAlias = true;
            }
          }
        }
        if(!hasAlias) {
          throw new CommandNotFoundException(inputLine);
        }
      }
        if(!hasAlias) {
          c = Constants.CommandConstants.commands.get(words[0].toLowerCase());
        } else {
          c = CommandConstants.commands.get(alias);
        }
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
