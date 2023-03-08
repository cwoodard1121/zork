package zork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import datatypes.Location;
import zork.Constants.PlayerConstants;
import zork.entites.Player;

public class Game {

  private final Graphics renderer = new Graphics(this);
  public static Game game = new Game();
  public static boolean finished = false;
  public static HashMap<String, Room> roomMap = new HashMap<String, Room>();

  private final Player player;
  private final Parser parser;

  private Room currentRoom;
  

  /**
   * Create the game and initialise its internal map.
   */
  public Game() {
    try {
      // initRooms("src\\zork\\data\\rooms.json");
      // currentRoom = roomMap.get("Bedroom");

    } catch (Exception e) {
      e.printStackTrace();
    }
    this.parser = new Parser();
    this.player = new Player(new Location(0, 0), new Room(), PlayerConstants.DEFAULT_HEALTH, 0, new ArrayList<Item>(), 0, new ArrayList<Moves>());
    
  }

  public static Game getGame() {
    return game;
  }

  public Player getPlayer() {
    return player;
  }

    

  private void initRooms(String fileName) throws Exception {
    File f = new File(new File("").getAbsolutePath().concat("\\bin\\zork\\data\\" + fileName));
    BufferedReader reader = new BufferedReader(new FileReader(f));
    String line = null;
    StringBuilder b = new StringBuilder();
    while((line = reader.readLine()) != null) {
      b.append(line);
    }
    String jsonString = b.toString();
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);

    JSONArray jsonRooms = (JSONArray) json.get("rooms");

    for (Object roomObj : jsonRooms) {
      Room room = new Room();
      String roomName = (String) ((JSONObject) roomObj).get("name");
      String roomId = (String) ((JSONObject) roomObj).get("id");
      String roomDescription = (String) ((JSONObject) roomObj).get("description");
      room.setDescription(roomDescription);
      room.setRoomName(roomName);

      JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
      ArrayList<Exit> exits = new ArrayList<Exit>();
      for (Object exitObj : jsonExits) {
        String direction = (String) ((JSONObject) exitObj).get("direction");
        String adjacentRoom = (String) ((JSONObject) exitObj).get("adjacentRoom");
        String keyId = (String) ((JSONObject) exitObj).get("keyId");
        Boolean isLocked = (Boolean) ((JSONObject) exitObj).get("isLocked");
        Boolean isOpen = (Boolean) ((JSONObject) exitObj).get("isOpen");
        Exit exit = new Exit(direction, adjacentRoom, isLocked, keyId, isOpen);
        exits.add(exit);
      }
      room.setExits(exits);
      roomMap.put(roomId, room);
    }
  }

  /**
   * Main play routine. Loops until end of play.
   */
  public void play() {
    
    printWelcome();

    while (!finished) {
      Command command;
      try {
        command = parser.getCommand();
        String[] params = parser.getParams();
        processCommand(command,params);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    System.out.println("Thank you for playing.  Good bye.");
  }

  /**
   * Print out the opening message for the player.
   */

  private void handleException(Exception e) {
    // TODO: FINISH
  }

  private void printWelcome() {
    titleCard c = new titleCard();
    c.printTitle();
    try {
      renderer.showCutScene(1500);
    } catch (Exception e) {
      handleException(e);
    }

    System.out.println();
    System.out.println("Zork is a new, incredibly boring adventure game.");
    System.out.println("Type 'help' if you need help.");
    System.out.println();


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
