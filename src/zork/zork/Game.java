package zork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import datatypes.CommandNotFoundException;
import datatypes.Location;
import zork.Constants.PlayerConstants;
import zork.entites.Player;

public class Game {

  private final Graphics renderer = new Graphics(this);
  private final Gson gson = new Gson();
  public static Game game = new Game();
  public static boolean finished = false;
  public static boolean shouldCreateRooms = true;
  public static boolean isTesting = true;
  public static HashMap<String, Room> roomMap; 

  private final Player player;
  private final Parser parser;
  

  /**
   * Create the game and initialise its internal map.
   */
  public Game() { 
    Constants.initCommands();
    try {
      // initRooms("src\\zork\\data\\rooms.json");
      // currentRoom = roomMap.get("Bedroom");

    } catch (Exception e) {
      e.printStackTrace();
    }
    this.parser = new Parser();
    this.player = new Player(new Location(0, 0), new Room(), PlayerConstants.DEFAULT_HEALTH, new Inventory(PlayerConstants.MAX_INVENTORY_WEIGHT), 0, new ArrayList<Move>(), "caga");
    
  }

  public static Game getGame() {
    return game;
  }

  public Player getPlayer() {
    return player;
  }

  @Deprecated
  /**
   * This method should only be used when testing so that we can export rooms using java
   * instead of having to write them in the god awful json file. run this and it will export
   * all the rooms in the roomsMap.
   */
  public void exportRooms() {
    System.out.println(roomMap.size());
    final BufferedWriter roomWriter =  Utils.getWriterFromBin("rooms.json");
    try {
      roomWriter.write(gson.toJson(roomMap));
      roomWriter.close();
    } catch(IOException e) {

    }
  }

  /**
   * This method will not be here at the final product its just to create the initital rooms
   */
  public void createRooms() { 
    roomMap = new HashMap<String,Room>();
    // VARIABLE NAME STANDARDS !!IMPORTANT!!, for rooms, Make a name using java naming convention
    // for Exits use the room name + exit + Direction of exit example Room yorkMillsTerminal has as exit that goes to it in the north so we call it "yorkMillsBusTerminalExitNorth"
    if(shouldCreateRooms) {
      // Create a room object and use the description as the constructor parameter.
      final Room yorkMillsBusTerminal = new Room("The bus","yorkmillsbusterminal"); roomMap.put(yorkMillsBusTerminal.getRoomName(),yorkMillsBusTerminal);
      // Subway area in york mills
      final Room yorkMillsSubwayHallway = new Room("A Hallway is ahead leading to the Subway","yorkmillssubwayhallway"); roomMap.put(yorkMillsSubwayHallway.getRoomName(), yorkMillsSubwayHallway);
      // Exit which goes down into the subwaynigni
      final Room yorkMillsSubway = new Room("Subways are going by, North to Finch, South to Vaughn", "yorkmillssubway"); roomMap.put(yorkMillsSubway.getRoomName(), yorkMillsSubway);
      final Exit yorkMillsSubwayHallwayExitDown = new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown);
      // exit which goes back to the bus terminal
      final Exit yorkMillsBusTerminalExitUp = new Exit("U", yorkMillsBusTerminal); yorkMillsSubwayHallway.addExit(yorkMillsBusTerminalExitUp);
      final Exit yorkMillsBusSubwayHallwayExitNorth = new Exit("N", yorkMillsSubwayHallway); yorkMillsSubway.addExit(yorkMillsBusSubwayHallwayExitNorth);
      final Exit yorkMillsSubwayExitSouth = new Exit("S", yorkMillsSubway); yorkMillsSubwayHallway.addExit(yorkMillsSubwayExitSouth);
     // Attempt at understanding this _______________________  Direction from TO here        From here                             Exit Name Where its going TO +
      System.out.println(roomMap == null);
      
    }
  }


  private void initRooms(String fileName) throws Exception {
    roomMap = new HashMap<String, Room>();
    BufferedReader reader = Utils.getReaderFromBin(fileName);
    String line = null;
    StringBuilder b = new StringBuilder();
    while((line = reader.readLine()) != null) {
      b.append(line);
    }
    // this is so much simpler and it should actually work.
    String jsonString = b.toString();
    roomMap = (HashMap<String, Room>) gson.fromJson(jsonString, Map.class);
  }

  /**
   * Main play routine. Loops until end of play.
   * @throws InterruptedException
   */
  public void play() throws InterruptedException {
    
    printWelcome();
    try {
      createRooms();
      if(roomMap.size() == 0) {
        initRooms("rooms.json");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.player.setCurrentRoom(roomMap.get("yorkmillsbusterminal"));
    while (!finished) {
      Command command;
      try {
        command = parser.getCommand();
        String[] params = parser.getParams();
        processCommand(command,params);
      } catch (NullPointerException e) {
        e.printStackTrace();
      } catch (CommandNotFoundException e) {
        e.printStackTrace();
      }

    } 
  }

  /**
   * Print out the opening message for the player.
   */

  private void handleException(Exception e) {
    // TODO: FINISH
  }

  private void printWelcome() throws InterruptedException {
    Scanner in = new Scanner(System.in);
    titleCard c = new titleCard();
    Utils.playTitleSound();
    c.printTitle();
    boolean hasStart = false;
    while(!hasStart) {
      String result = in.nextLine().toLowerCase();
      if(result.equals("start")) {
        Utils.stopSound("mainmenu.wav");
        hasStart = true;
        if(!isTesting) {
        try {
          renderer.showCutScene(1500, "\\bin\\zork\\data\\cutscene.txt");
        } catch (Exception e) {
          handleException(e);
        }
      }
        boolean hasChosenName = false;
        System.out.print("Please enter your name: ");
        while(!hasChosenName) {
          result = in.nextLine().toLowerCase();
          if(result.length() > 16) {
            System.out.print("Please enter a name between 1-16 Characters");
          } else if (result.equalsIgnoreCase("cameron")) { 
            System.out.print("Sorry that name is already taken, Please enter another name:");
          } else if (result.equalsIgnoreCase("cagasuge")) { 
            System.out.println("Im not mad... Im just dissapointed     ");
          } else {
            hasChosenName = true;
            Game.getGame().getPlayer().setName(result);
          }
        }
        


      } else {
        System.out.println("Please enter a valid command");
    }
   }
  }

  /**
   * Given a command, process (that is: execute) the command.
   */
  private void processCommand(Command command, String[] args) {
    System.out.println(command.runCommand(args));
  }

  // implementations of user commands:

  /**
   * Print out some help information. Here we print some stupid, cryptic message
   * and a list of the command words.
   */
  private void printHelp() {
    System.out.println("You are lost. You are alone. You wander");
    System.out.println("around at Monash Uni, Peninsula Campus.");
    System.out.println();
    System.out.println("Your command words are:");
  }

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */
}
