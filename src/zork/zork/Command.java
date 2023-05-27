package zork;

import java.util.ArrayList;

public class Command {
  private String name;
  private ArrayList<String> aliases = new ArrayList<>();

  /**
   * Create a command object. Only need to supply the name and then use override the
   * runCommand method for it to actually do stuff.
   */
  public Command(String name) {
    this.name = name;
  }

  public Command addAlias(String name) {
    aliases.add(name);
    return this;
  }

  public ArrayList<String> getAliases() {
    return aliases;
  }

  /* Blank constructor for unknown commands. */
  public Command() {
    this.name = "Unknown";
  }

  
  /**
   * 
   * @param args
   * @return Console otuput
 * @throws InterruptedException
   */
  public String runCommand(String... args) throws InterruptedException  {
    return "Invalid command: " + name;
  }

  public String getName() {
    return name;
  }

}
