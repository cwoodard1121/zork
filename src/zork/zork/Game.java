package zork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;

import datatypes.CommandNotFoundException;
import datatypes.Location;
import zork.Constants.PlayerConstants;
import zork.entites.Player;

public class Game {

  private final Graphics renderer = new Graphics(this);
  public static AtomicBoolean bool = new AtomicBoolean();
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
    this.player = new Player(new Location(0, 0), new Room(), PlayerConstants.DEFAULT_HEALTH, new Inventory(PlayerConstants.MAX_INVENTORY_WEIGHT), 0,"placeholder (lmao reference)");
    
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

      // SHEPPARD YONGE
      final Room sheppardYongeLine1 = new Room("Going south will lead you to York Mills, North to Finch is under maintainence.","sheppardyongeline1");
      final Room sheppardYongeLine4 = new Room("Going east will lead you to Bayview. Going west will lead you into a tunnel.","sheppardyongeline4");
      final Room sheppardYongeLine4StreetHallway = new Room("The escalator is stopped. The door to the street is nearby.","sheppardyongeline4streethighway");

      //YORK MILLS AREA ROOMS
      final Room yorkMillsBusTerminal = new Room("The bus","yorkmillsbusterminal"); roomMap.put(yorkMillsBusTerminal.getRoomName(),yorkMillsBusTerminal);
      final Room facultyRoom = new Room("A staff room with a few tables", "facultyroom"); roomMap.put(facultyRoom.getRoomName(), facultyRoom);
      final Room gatewayNewsstands = new Room("*Implement shopkeeper* Hello, would you like to purchase anything?", "gatewaynewsstands"); roomMap.put(gatewayNewsstands.getRoomName(), gatewayNewsstands);
      final Room yorkMillsSubwayHallway = new Room("A Hallway is ahead leading to the Subway, Chuck Page plays some guitar for passersby.","yorkmillssubwayhallway"); roomMap.put(yorkMillsSubwayHallway.getRoomName(), yorkMillsSubwayHallway);
      final Room yorkMillsSubway = new Room("Please come back later, unscheduled maintenance has just been scheduled, shuttlebuses are available.", "yorkmillssubway"); 
      yorkMillsSubway.addItemGround(new Item(1,  "transfer", false, false));
      final Room eglintonShuttleBus = new Room("Going south will lead you to Elginton Station via the shuttle bus", "eglintonshuttlebus"); roomMap.put(eglintonShuttleBus.getRoomName(), eglintonShuttleBus); roomMap.put(yorkMillsSubway.getRoomName(), yorkMillsSubway);
      //EGLINTON AREA ROOMS
      final Room yorkMillsShuttleBus = new Room("Going north will lead you to York Mills Station", "yorkmillsshuttlebus"); roomMap.put(yorkMillsShuttleBus.getRoomName(), yorkMillsShuttleBus);
      final Room eglintonBusStop = new Room("You face the completely halted traffic of Yonge and Eglinton", "eglintonbusstop"); roomMap.put(eglintonBusStop.getRoomName(), eglintonBusStop);
      final Room eglintonStation = new Room("you have entered Eglinton Station. It smells of cinnabons.", "elgintonstation"); roomMap.put(eglintonStation.getRoomName(), eglintonStation);
      final Room eglintonStreet = new Room("You are on the sidewalk on Eglinton Street, you can feel the subway rumble below you.", "eglintonstreet"); roomMap.put(eglintonStreet.getRoomName(), eglintonStreet);
      final Room yongeEglintonMall = new Room("You stand in the lobby of the Yonge and Eglinton Mall.", "yongeeglintonmall"); roomMap.put(yongeEglintonMall.getRoomName(), yongeEglintonMall);

      //BAYVIEW GLEN INDEPENDENT SCHOOL ROOMS

      final Room bayviewGlenLobby = new Room ("Placeholder Description for bayviewGlenLobby", "bayviewglenlobby"); roomMap.put(bayviewGlenLobby.getRoomName(), bayviewGlenLobby); // north exit outside for later looking south when walking in
      final Room bayviewGlenHallwayCafeteria = new Room ("Placeholder Description for bayviewGlenHallwayCafeteria", "bayviewglenhallwaycafeteria"); roomMap.put(bayviewGlenHallwayCafeteria.getRoomName(), bayviewGlenHallwayCafeteria); // to the east from lobby
      final Room bayviewGlenHallwayPrepGym = new Room("Placeholder Description for bayviewGlenHallwayPrepGym", "bayviewglenhallwayprepgym");
      final Room bayviewGlenHallwayTheatreFront = new Room("Placeholder Description for bayviewGlenHallwayTheatreFront", "bayviewglenhallwaytheatrefront");
      final Room bayviewGlenCafeteriaFoodArea = new Room("Placeholder Description for bayviewGlenCafeteriaFoodArea", "bayviewglencafeteriafoodarea");
      final Room bayviewGlenKitchen = new Room("Placeholder Description for BayviewGlenKitchen", "bayviewglenkitchen");
      final Room bayviewGlenCafeteriaDiningArea = new Room("Placeholder Description for bayviewGlenCAfeteriaDiningArea", "bayviewglencafeteriadiningarea");
      final Room bayviewGlenPrepGym = new Room("Placeholder Description for bayviewGlenPrepGym", "bayviewglenprepgym");
      final Room bayviewGlenWeightRoom = new Room("Placeholder Description for bayviewGlenWeightRoom", "bayviewglenweightroom");
      final Room bayviewGlenOutsideHallwayPrepGym = new Room("Placeholder Description for bayviewGlenOutsideHallwayPrepGym", "bayviewglenoutsidehallwayprepgym");
      final Room bayviewGlenLearningCommons = new Room("Placeholder Description for bayviewGlenLearningCommons", "bayviewglenlearningcommons");
      final Room bayviewGlenHallwayExitFromHallwayTheatreFront = new Room("placeholder Description for bayviewGlenHallwayExitFromHallwayTheatreFront", "bayviewglenhallwayexitfromhallwaytheatrefront");
      final Room bayviewGlenTheatre = new Room("Placeholder Description for bayviewGlenTheatre", "bayviewglentheatre");
      final Room bayviewGlenHallway2ndFloorToUpperSchool = new Room("Placeholder Description for bayviewGlenHallway2ndFloorToUpperSchool", "bayviewglenhallway2ndfloortoupperschool");
      final Room bayviewGlenG11CommonArea = new Room("Placeholder Description for bayviewGlenG11CommonArea", "bayviewgleng11commonarea");
      final Room bayviewGlenGradHallway = new Room("Placeholder Description for bayviewGlenGradHallway", "bayviewglengradhallway");
      


      //YORK MILLS AREA EXITS
      final Exit yorkMillsSubwayHallwayExitDown = new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown);
      final Exit yorkMillsBusSubwayHallwayExitNorth = new Exit("N", yorkMillsSubwayHallway); yorkMillsSubway.addExit(yorkMillsBusSubwayHallwayExitNorth);
      final Exit yorkMillsBusTerminalExitUp = new Exit("U", yorkMillsBusTerminal); yorkMillsSubwayHallway.addExit(yorkMillsBusTerminalExitUp);
      final Exit gatewayNewsstandsExitEast = new Exit("E", gatewayNewsstands); yorkMillsSubwayHallway.addExit(gatewayNewsstandsExitEast);
      final Exit yorkMillsSubwayExitSouth = new Exit("S", yorkMillsSubway); yorkMillsSubwayHallway.addExit(yorkMillsSubwayExitSouth);
      final Exit yorkMillsSubwayHallwayExitWest = new Exit("W", yorkMillsSubwayHallway); gatewayNewsstands.addExit(yorkMillsSubwayHallwayExitWest);
      final Exit facultyRoomExitWest = new Exit("W", facultyRoom); yorkMillsSubwayHallway.addExit(facultyRoomExitWest);
      final Exit yorkMillsSubwayHallwayExitEast = new Exit("E", yorkMillsSubwayHallway); facultyRoom.addExit(yorkMillsSubwayHallwayExitEast);
      final Exit eglintonShuttleBusExitEast = new Exit("E", eglintonShuttleBus); yorkMillsBusTerminal.addExit(eglintonShuttleBusExitEast);
      final Exit yorkMillsBusTerminalExitWest = new Exit("W", yorkMillsBusTerminal); eglintonShuttleBus.addExit(yorkMillsBusTerminalExitWest);
      final Exit eglintonBusStopExitSouth = new Exit("S", eglintonBusStop); eglintonShuttleBus.addExit(eglintonBusStopExitSouth);
      
      //EGLINTON AREA EXITS
      final Exit yorkMillsShuttleBusExitNorth = new Exit("N", yorkMillsShuttleBus); eglintonBusStop.addExit(yorkMillsShuttleBusExitNorth);
      final Exit eglintonBusStopExitSouthFromYorkMillsBus = new Exit("S", eglintonBusStop); yorkMillsShuttleBus.addExit(eglintonBusStopExitSouthFromYorkMillsBus);
      try {
        yorkMillsBusTerminal.printAscii();
      } catch (IOException e) {
        e.printStackTrace();
      }

      
     //ROOM LEGEND
     //final Room (room name) = new Room("description", room name all lowercase); roomMap.put(room name.getRoomName(), room name);
     // final Room  =  new Room("", ""); roomMap.put( .getRoomName(), );

     // EXIT LEGEND
     // final exit (destinationRoomExit(direction current room exits to) = new Exit(direction, destinationRoom); currentRoom.addExit(destinationRoomExit(direction current room exit to))
     // final exit  = new Exit("", );  .addExit( );

     

      
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
   * @throws IOException
   * @throws FileNotFoundException
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
   * @throws IOException
   * @throws FileNotFoundException
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
