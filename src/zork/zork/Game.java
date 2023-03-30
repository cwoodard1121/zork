package zork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public static boolean isTesting = true;
  public static HashMap<String, Room> roomMap = new HashMap<String, Room>();

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
    this.player = new Player(new Location(0, 0), new Room(), PlayerConstants.DEFAULT_HEALTH, 0, new Inventory(PlayerConstants.MAX_INVENTORY_WEIGHT), 0, new ArrayList<Move>());
    
  }

  public static Game getGame() {
    return game;
  }

  public Player getPlayer() {
    return player;
  }

    
  /**
   * This method should only be used when testing so that we can export rooms using java
   * instead of having to write them in the god awful json file. run this and it will export
   * all the rooms in the roomsMap.
   */
  private void exportRooms() {
    final BufferedWriter roomWriter =  Utils.getWriterFromBin("rooms.json");
    try {
      roomWriter.write(gson.toJson(roomMap));
      roomWriter.close();
    } catch(IOException e) {

    }
  }

  private void initRooms(String fileName) throws Exception {
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
    if(isTesting) return;
    Scanner in = new Scanner(System.in);
    titleCard c = new titleCard();
    Utils.playTitleSound();
    c.printTitle();
    boolean hasStart = false;
    while(!hasStart) {
      String result = in.nextLine().toLowerCase();
      if(result.equals("start")) {
        Constants.SoundConstants.playSounds.replace("mainmenu.wav", true, false);
        hasStart = true;
        try {
          renderer.showCutScene(1500);
        } catch (Exception e) {
          handleException(e);
        }
      } else {
        System.out.println("Please enter a valid command");
    }
   }

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
