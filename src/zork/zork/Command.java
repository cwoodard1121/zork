package zork;

public class Command {
  private String name;

  /**
   * Create a command object. Only need to supply the name and then use override the
   * runCommand method for it to actually do stuff.
   */
  public Command(String name) {
    this.name = name;
  }

  /* Blank constructor for unknown commands. */
  public Command() {
    this.name = "Unknown";
  }

  
  /**
   * 
   * @param args
   * @return Console otuput
   */
  public String runCommand(String... args) {
    return "Invalid command: " + name;
  }

  public String getName() {
    return name;
  }

}
