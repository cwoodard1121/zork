package zork;

import java.util.Scanner;

public class Parser {
  private Scanner in;
  private String[] args;

  public Parser() {
    in = new Scanner(System.in);
  }

  public Command getCommand() throws java.io.IOException {
    String[] words;
    String inputLine = "";
    Command c = new Command();

    System.out.print("> "); // print prompt

    inputLine = in.nextLine();

    words = inputLine.split(" ");

    int i = 1;
    for(String word : words) {
      if(i == 1) {
        c = Constants.CommandConstants.commands.get(word);
        i++;
        continue;
      }
      args[i] = word;
      i++;
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
