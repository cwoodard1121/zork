package zork;

import java.util.Scanner;

public class Parser {
  private CommandWords commands; // holds all valid command words
  private Scanner in;
  private String[] args;

  public Parser() {
    commands = new CommandWords();
    in = new Scanner(System.in);
  }

  public Command getCommand() throws java.io.IOException {
    String inputLine = "";
    String[] words;
    Command c = new Command();

    System.out.print("> "); // print prompt

    inputLine = in.nextLine();

    words = inputLine.split(" ");

    int i = 1;
    for(String word : words) {
      if(i == 1) {
        c = new Command(word);
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
  public void showCommands() {
    commands.showAll();
  }

  public String[] getParams() {
    return args;
  }
}
