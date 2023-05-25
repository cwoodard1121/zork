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

      yorkMillsSubway.addItemGround(new Item(1,  "transfer",false, null, false));
      final Room eglintonShuttleBus = new Room("Going south will lead you to Eglinton Station via the shuttle bus", "eglintonshuttlebus"); roomMap.put(eglintonShuttleBus.getRoomName(), eglintonShuttleBus); roomMap.put(yorkMillsSubway.getRoomName(), yorkMillsSubway);
      yorkMillsSubway.addItemGround(new Item(1,  "transfer", false, null, false));
      //EGLINTON AREA ROOMS
      final Room yorkMillsShuttleBus = new Room("Going north will lead you to York Mills Station", "yorkmillsshuttlebus"); roomMap.put(yorkMillsShuttleBus.getRoomName(), yorkMillsShuttleBus);
      final Room eglintonBusStop = new Room("You face the completely halted traffic of Yonge and Eglinton", "eglintonbusstop"); roomMap.put(eglintonBusStop.getRoomName(), eglintonBusStop);
      final Room eglintonStation = new Room("you have entered Eglinton Station. It smells of cinnabons.", "eglintonstation"); roomMap.put(eglintonStation.getRoomName(), eglintonStation);
      final Room eglintonStreet = new Room("You are on the sidewalk on Eglinton Street, you can feel the subway rumble below you.", "eglintonstreet"); roomMap.put(eglintonStreet.getRoomName(), eglintonStreet);
      final Room yongeEglintonMall = new Room("You stand in the lobby of the Yonge and Eglinton Mall.", "yongeeglintonmall"); roomMap.put(yongeEglintonMall.getRoomName(), yongeEglintonMall);
      final Room circleK = new Room ("*Dialogue about prime to be implemented, homeless fight and u get a prime* a dingy convenience store with a sleeping cashier", "circlek"); roomMap.put(circleK.getRoomName(), circleK);
      final Room foodCourt = new Room ("*Dialogue with OP shopkeeper, the prime here is super expensive, and shopkeeper is super strong* You can hear the subway rumbling in the background", "foodcourt"); roomMap.put(foodCourt.getRoomName(), foodCourt);
      final Room eglintonSubway = new Room ("South leads to St. Clair station, North leads to York Mills Station", "eglintonsubway",true); eglintonSubway.setLocked(true); roomMap.put(eglintonSubway.getRoomName(), eglintonSubway);

      //BAYVIEW GLEN INDEPENDENT SCHOOL ROOMS
 
      final Room bayviewGlenLobby = new Room ("Placeholder Description for bayviewGlenLobby", "bayviewglenlobby"); roomMap.put(bayviewGlenLobby.getRoomName(), bayviewGlenLobby); // north exit outside for later looking south when walking in
      final Room bayviewGlenOutsideLobby = new Room ("Placeholder Description for bayviewGlenOutsideLobby", "bayviewglenoutsidelobby"); roomMap.put(bayviewGlenOutsideLobby.getRoomName(), bayviewGlenOutsideLobby);
      final Room bayviewGlenHallwayCafeteria = new Room ("Placeholder Description for bayviewGlenHallwayCafeteria", "bayviewglenhallwaycafeteria"); roomMap.put(bayviewGlenHallwayCafeteria.getRoomName(), bayviewGlenHallwayCafeteria); // to the east from lobby
      final Room bayviewGlenHallwayPrepGym = new Room("Placeholder Description for bayviewGlenHallwayPrepGym", "bayviewglenhallwayprepgym"); roomMap.put(bayviewGlenHallwayPrepGym.getRoomName(), bayviewGlenHallwayPrepGym);
      final Room bayviewGlenHallwayTheatreFront = new Room("Placeholder Description for bayviewGlenHallwayTheatreFront", "bayviewglenhallwaytheatrefront"); roomMap.put(bayviewGlenHallwayTheatreFront.getRoomName(), bayviewGlenHallwayTheatreFront);
      final Room bayviewGlenOutsideHallwayTheatreFront = new Room ("Placeholder Description for bayviewGlenOutsideHallwayTheatreFront", "bayviewglenoutsidehallwaytheatrefront"); roomMap.put(bayviewGlenOutsideHallwayTheatreFront.getRoomName(), bayviewGlenOutsideHallwayTheatreFront);
      final Room bayviewGlenCafeteriaFoodArea = new Room("Placeholder Description for bayviewGlenCafeteriaFoodArea", "bayviewglencafeteriafoodarea"); roomMap.put(bayviewGlenCafeteriaFoodArea.getRoomName(), bayviewGlenCafeteriaFoodArea);
      final Room bayviewGlenOutsideCafeteria = new Room ("Placeholder Description for bayviewGlenOutsideCafeteria", "bayviewglenoutsidecafeteria"); roomMap.put(bayviewGlenOutsideCafeteria.getRoomName(), bayviewGlenOutsideCafeteria);
      final Room bayviewGlenKitchen = new Room("Placeholder Description for BayviewGlenKitchen", "bayviewglenkitchen"); roomMap.put(bayviewGlenKitchen.getRoomName(), bayviewGlenKitchen);
      final Room bayviewGlenCafeteriaDiningArea = new Room("Placeholder Description for bayviewGlenCAfeteriaDiningArea", "bayviewglencafeteriadiningarea"); roomMap.put(bayviewGlenCafeteriaDiningArea.getRoomName(), bayviewGlenCafeteriaDiningArea);
      final Room bayviewGlenPrepGym = new Room("Placeholder Description for bayviewGlenPrepGym", "bayviewglenprepgym"); roomMap.put(bayviewGlenPrepGym.getRoomName(), bayviewGlenPrepGym);
      final Room bayviewGlenWeightRoom = new Room("Placeholder Description for bayviewGlenWeightRoom", "bayviewglenweightroom"); roomMap.put(bayviewGlenWeightRoom.getRoomName(), bayviewGlenWeightRoom);
      final Room bayviewGlenOutsidePrepGymNorth = new Room("Placeholder Description for bayviewGlenOutsidePrepGymNorth", "bayviewglenoutsideprepgymnorth"); roomMap.put(bayviewGlenOutsidePrepGymNorth.getRoomName(), bayviewGlenOutsidePrepGymNorth);
      final Room bayviewGlenOutsidePrepGymEast = new Room("Placeholder Description for bayviewGlenOutsidePrepGymEast", "bayviewglenoutsideprepgymeast"); roomMap.put(bayviewGlenOutsidePrepGymEast.getRoomName(), bayviewGlenOutsidePrepGymEast);
      final Room bayviewGlenLearningCommons = new Room("Placeholder Description for bayviewGlenLearningCommons", "bayviewglenlearningcommons"); roomMap.put(bayviewGlenLearningCommons.getRoomName(), bayviewGlenLearningCommons);
      final Room bayviewGlenLearningCommonsSubArea = new Room ("Placeholder Description for bayviewGlenLearningCommonsSubArea", "bayviewglenlearningcommonssubarea"); roomMap.put(bayviewGlenLearningCommonsSubArea.getRoomName(), bayviewGlenLearningCommonsSubArea);
      final Room bayviewGlenTheatre = new Room("Placeholder Description for bayviewGlenTheatre", "bayviewglentheatre"); roomMap.put(bayviewGlenTheatre.getRoomName(), bayviewGlenTheatre);
      final Room bayviewGlenHallwayTheatreBack = new Room ("Placeholder Description for bayviewGlenHallwayTheatreBack", "bayviewglenhallwaytheatreback"); roomMap.put(bayviewGlenHallwayTheatreBack.getRoomName(), bayviewGlenHallwayTheatreBack);
      final Room bayviewGlenHallway2ndFloorToUpperSchool = new Room("Placeholder Description for bayviewGlenHallway2ndFloorToUpperSchool", "bayviewglenhallway2ndfloortoupperschool"); roomMap.put(bayviewGlenHallway2ndFloorToUpperSchool.getRoomName(), bayviewGlenHallway2ndFloorToUpperSchool);
      final Room bayviewGlenG11CommonArea = new Room("Placeholder Description for bayviewGlenG11CommonArea", "bayviewgleng11commonarea"); roomMap.put(bayviewGlenG11CommonArea.getRoomName(), bayviewGlenG11CommonArea);
      final Room bayviewGlenGradHallway = new Room("Placeholder Description for bayviewGlenGradHallway", "bayviewglengradhallway"); roomMap.put(bayviewGlenGradHallway.getRoomName(), bayviewGlenGradHallway);
      final Room bayviewGlenUpperMusicHallway = new Room ("Placeholder Description for bayviewGlenUpperMusicHallway", "bayviewglenuppermusichallway"); roomMap.put(bayviewGlenUpperMusicHallway.getRoomName(), bayviewGlenUpperMusicHallway);
      final Room bayviewGlenOutsideHallwayTheatreBack = new Room ("Placeholder Description for bayviewGlenOutsideHallwayTheatreBack", "bayviewglenoutsidehallwaytheatreback"); roomMap.put(bayviewGlenOutsideHallwayTheatreBack.getRoomName(), bayviewGlenOutsideHallwayTheatreBack);
      final Room bayviewGlenDramaRoom = new Room ("Placeholder Description for bayviewGlenDramaRoom", "bayviewglendramaroom"); roomMap.put(bayviewGlenDramaRoom.getRoomName(), bayviewGlenDramaRoom);
      final Room bayviewGlenDeck = new Room ("Placeholder Description for bayviewGlenDeck", "bayviewglendeck"); roomMap.put(bayviewGlenDeck.getRoomName(), bayviewGlenDeck);
      final Room bayviewGlenYorkMills = new Room ("Placeholder Description for bayviewGlenYorkMills", "bayviewglenyorkmills"); roomMap.put(bayviewGlenYorkMills.getRoomName(), bayviewGlenYorkMills);
      final Room bayviewGlen3rdFloorLobby = new Room ("Placeholder Description for bayviewGlen3rdFloorLobby", "bayviewglen3rdfloorlobby"); roomMap.put(bayviewGlen3rdFloorLobby.getRoomName(), bayviewGlen3rdFloorLobby);
      final Room bayviewGlen3rdFloorWestPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen3rdFloorWestPrepStairwayHallway", "bayviewglen3rdfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorWestPrepStairwayHallway);
      final Room bayviewGlen3rdFloorEastPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen3rdFloorEast'PrepStairwayHallway", "bayviewglen3rdflooreastprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorEastPrepStairwayHallway);
      final Room bayviewGlenG9CommonArea = new Room ("Placeholder Description for bayviewGlenG9CommonArea", "bayviewgleng9commonarea"); roomMap.put(bayviewGlenG9CommonArea.getRoomName(), bayviewGlenG9CommonArea);
      final Room bayviewGlenOutsideStaircase = new Room ("Placeholder Description for bayviewGlenOutsideStaircase", "bayviewglenoutsidestaircase"); roomMap.put(bayviewGlenOutsideStaircase.getRoomName(), bayviewGlenOutsideStaircase);
      final Room bayviewGlenPrepMusicRoomHallway = new Room ("Placeholder Description for bayviewGlenPrepMusicRoomHallway", "bayviewglenprepmusicroomhallway"); roomMap.put(bayviewGlenPrepMusicRoomHallway.getRoomName(), bayviewGlenPrepMusicRoomHallway);
      final Room bayviewGlenMusicRoom = new Room ("Placeholder Description for bayviewGlenMusicRoom", "bayviewglenmusicroom"); roomMap.put(bayviewGlenMusicRoom.getRoomName(), bayviewGlenMusicRoom);
      final Room bayviewGlen2ndFloorUpperHallway = new Room ("Placeholder Description for bayviewGlen2ndFloorUpperHallway", "bayviewglen2ndfloorupperhallway"); roomMap.put(bayviewGlen2ndFloorUpperHallway.getRoomName(), bayviewGlen2ndFloorUpperHallway);

      //BVG EXITS
      final Exit bayviewGlenOutsideLobbyExitNorth = new Exit("N",bayviewGlenOutsideLobby); bayviewGlenLobby.addExit(bayviewGlenOutsideLobbyExitNorth); 
      final Exit bayviewGlenHallwayCafeteriaExitEast = new Exit("E",bayviewGlenHallwayCafeteria); bayviewGlenLobby.addExit(bayviewGlenHallwayCafeteriaExitEast);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitSouth = new Exit("S",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenLobby.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitSouth);
      final Exit bayviewGlenHallwayTheatreFrontExitWest = new Exit("W",bayviewGlenHallwayTheatreFront); bayviewGlenLobby.addExit(bayviewGlenHallwayTheatreFrontExitWest);
      final Exit bayviewGlen3rdFloorLobbyExitUp = new Exit("U",bayviewGlen3rdFloorLobby); bayviewGlenLobby.addExit(bayviewGlen3rdFloorLobbyExitUp);

      final Exit bayviewGlenLobbyExitSouth = new Exit("S",bayviewGlenLobby); bayviewGlenOutsideLobby.addExit(bayviewGlenLobbyExitSouth);
      final Exit bayviewGlenOutsideCafeteriaExitEast = new Exit("E",bayviewGlenOutsideCafeteria); bayviewGlenLobby.addExit(bayviewGlenOutsideCafeteriaExitEast);
      final Exit bayviewGlenOutsideHallwayTheatreFrontExitWest = new Exit("W",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenLobby.addExit(bayviewGlenOutsideHallwayTheatreFrontExitWest);

      final Exit bayviewGlenLearningCommonsExitSouth = new Exit("S",bayviewGlenLearningCommons); bayviewGlenHallwayCafeteria.addExit(bayviewGlenLearningCommonsExitSouth);
      final Exit bayviewGlenCafeteriaFoodAreaExitNorth = new Exit("N",bayviewGlenCafeteriaFoodArea); bayviewGlenHallwayCafeteria.addExit(bayviewGlenCafeteriaFoodAreaExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitEast = new Exit("E",bayviewGlenHallwayPrepGym); bayviewGlenHallwayCafeteria.addExit(bayviewGlenHallwayPrepGymExitEast);
      final Exit bayviewGlenLobbyExitWest = new Exit("W",bayviewGlenLobby); bayviewGlenHallwayCafeteria.addExit(bayviewGlenLobbyExitWest);

      final Exit bayviewGlenPrepGymExitNorth = new Exit("N",bayviewGlenPrepGym); bayviewGlenHallwayPrepGym.addExit(bayviewGlenPrepGymExitNorth);
      final Exit bayviewGlenOutsideHallwayPrepGymExitEast = new Exit("E",bayviewGlenOutsidePrepGymEast); bayviewGlenHallwayPrepGym.addExit(bayviewGlenOutsideHallwayPrepGymExitEast);
      final Exit bayviewGlenWeightRoomExitSouth = new Exit("S",bayviewGlenWeightRoom); bayviewGlenHallwayPrepGym.addExit(bayviewGlenWeightRoomExitSouth);
      final Exit bayviewGlenHallwayCafeteriaExitWest = new Exit("W",bayviewGlenHallwayCafeteria); bayviewGlenHallwayPrepGym.addExit(bayviewGlenHallwayCafeteriaExitWest);
      final Exit bayviewGlen3rdFloorEastPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen3rdFloorEastPrepStairwayHallway); bayviewGlenHallwayPrepGym.addExit(bayviewGlen3rdFloorEastPrepStairwayHallwayExitUp);

      final Exit bayviewGlenOutsideHallwayTheatreFrontExitNorth = new Exit("N",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenOutsideHallwayTheatreFrontExitNorth);
      final Exit bayviewGlenTheatreExitSouth = new Exit("S",bayviewGlenTheatre); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenTheatreExitSouth);
      final Exit bayviewGlenLobbyExitEast = new Exit("E",bayviewGlenLobby); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenLobbyExitEast);
      final Exit bayviewGlenOutsideHallwayTheatreFrontWestExitWest = new Exit("W",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenOutsideHallwayTheatreFrontWestExitWest);
      final Exit bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen3rdFloorWestPrepStairwayHallway); bayviewGlenHallwayTheatreFront.addExit(bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp);
      
      final Exit bayviewGlenHallwayTheatreFrontExitSouth = new Exit("S",bayviewGlenHallwayTheatreFront); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenHallwayTheatreFrontExitSouth);
      
      final Exit bayviewGlenHallwayTheatreFrontExitEast = new Exit("E",bayviewGlenHallwayTheatreFront); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenHallwayTheatreFrontExitEast);

      final Exit bayviewGlenCafeteriaDiningAreaExitNorth = new Exit("N",bayviewGlenCafeteriaDiningArea); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenCafeteriaDiningAreaExitNorth);
      final Exit bayviewGlenHallwayCafeteriaExitSouth = new Exit("S",bayviewGlenHallwayCafeteria); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenHallwayCafeteriaExitSouth);
      final Exit bayviewGlenKitchenExitEast = new Exit("E",bayviewGlenKitchen); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenKitchenExitEast);
    
      final Exit bayviewGlenCafeteriaFoodAreaExitWest = new Exit("W",bayviewGlenCafeteriaFoodArea); bayviewGlenKitchen.addExit(bayviewGlenCafeteriaFoodAreaExitWest);

      final Exit bayviewGlenHallwayCafeteriaExitSouthTwo = new Exit("S",bayviewGlenHallwayCafeteria); bayviewGlenCafeteriaDiningArea.addExit(bayviewGlenHallwayCafeteriaExitSouthTwo);
      final Exit bayviewGlenOutsideCafeteriaExitNorth = new Exit("N",bayviewGlenOutsideCafeteria); bayviewGlenCafeteriaDiningArea.addExit(bayviewGlenOutsideCafeteriaExitNorth);
      
      final Exit bayviewGlenOutsideLobbyExitWest = new Exit("W",bayviewGlenOutsideLobby); bayviewGlenOutsideCafeteria.addExit(bayviewGlenOutsideLobbyExitWest);
      final Exit bayviewGlenOutsidePrepGymExitEast = new Exit("E",bayviewGlenOutsidePrepGymNorth); bayviewGlenOutsideCafeteria.addExit(bayviewGlenOutsidePrepGymExitEast);   
      final Exit bayviewGlenCafeteriaDiningAreaExitSouth = new Exit("S",bayviewGlenCafeteriaDiningArea); bayviewGlenOutsideCafeteria.addExit(bayviewGlenCafeteriaDiningAreaExitSouth);
      
      final Exit bayviewGlenOutsidePrepGymExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenPrepGym.addExit(bayviewGlenOutsidePrepGymExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitSouth = new Exit("S",bayviewGlenHallwayPrepGym); bayviewGlenPrepGym.addExit(bayviewGlenHallwayPrepGymExitSouth);

      final Exit bayviewGlenHallwayPrepGymExitNorth = new Exit("N",bayviewGlenHallwayPrepGym); bayviewGlenWeightRoom.addExit(bayviewGlenHallwayPrepGymExitNorth);

      final Exit bayviewGlenOutsideCafeteriaExitWest = new Exit("W",bayviewGlenOutsideCafeteria); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsideCafeteriaExitWest);
      final Exit bayviewGlenOutsidePrepGymEastExitEast = new Exit("E",bayviewGlenOutsidePrepGymEast); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsidePrepGymEastExitEast);
      final Exit bayviewGlenPrepGymExitSouth = new Exit("S",bayviewGlenPrepGym); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenPrepGymExitSouth);

      final Exit bayviewGlenOutsidePrepGymNorthExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenOutsidePrepGymEast.addExit(bayviewGlenOutsidePrepGymNorthExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitWest = new Exit("W",bayviewGlenHallwayPrepGym); bayviewGlenOutsidePrepGymEast.addExit(bayviewGlenHallwayPrepGymExitWest); 

      final Exit bayviewGlenHallwayCafeteriaExitNorth = new Exit("N",bayviewGlenHallwayCafeteria); bayviewGlenLearningCommons.addExit(bayviewGlenHallwayCafeteriaExitNorth);
      final Exit bayviewGlenLearningCommonsSubAreaExitWest = new Exit("W",bayviewGlenLearningCommonsSubArea); bayviewGlenLearningCommons.addExit(bayviewGlenLearningCommonsSubAreaExitWest);

      final Exit bayviewGlenLearningCommonsExitEast = new Exit("E", bayviewGlenLearningCommons); bayviewGlenLearningCommonsSubArea.addExit(bayviewGlenLearningCommonsExitEast);

      final Exit bayviewGlenHallwayTheatreFrontExitNorth = new Exit("N",bayviewGlenHallwayTheatreFront); bayviewGlenTheatre.addExit(bayviewGlenHallwayTheatreFrontExitNorth);
      final Exit bayviewGlenHallwayTheatreBackExitSouth = new Exit("S",bayviewGlenHallwayTheatreBack); bayviewGlenTheatre.addExit(bayviewGlenHallwayTheatreBackExitSouth);

      final Exit bayviewGlenTheatreExitNorth = new Exit("N",bayviewGlenTheatre); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenTheatreExitNorth);
      final Exit bayviewGlenDramaRoomExitSouth = new Exit("S",bayviewGlenDramaRoom); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenDramaRoomExitSouth);
      final Exit bayviewGlenG11CommonAreaExitEast = new Exit("E",bayviewGlenG11CommonArea); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenG11CommonAreaExitEast);
      final Exit bayviewGlenOutsideHallwayTheatreBackExitWest = new Exit("W",bayviewGlenOutsideHallwayTheatreBack); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenOutsideHallwayTheatreBackExitWest);

      final Exit bayviewGlenTheatreHallwayBackExitNorth = new Exit("N",bayviewGlenHallwayTheatreBack); bayviewGlenDramaRoom.addExit(bayviewGlenTheatreHallwayBackExitNorth);

      final Exit bayviewGlenOustideHallwayTheatreFrontExitNorth = new Exit("N",bayviewGlenOutsideHallwayTheatreBack); bayviewGlenOutsideHallwayTheatreBack.addExit(bayviewGlenOustideHallwayTheatreFrontExitNorth);
      final Exit bayviewGlenDeckExitSouth = new Exit("S",bayviewGlenDeck); bayviewGlenOutsideHallwayTheatreBack.addExit(bayviewGlenDeckExitSouth);
      final Exit bayviewGlenHallwayTheatreBackExitEast = new Exit("E",bayviewGlenHallwayTheatreBack); bayviewGlenOutsideHallwayTheatreBack.addExit(bayviewGlenHallwayTheatreBackExitEast);
      
      final Exit bayviewGlenLobbyExitNorth = new Exit("N",bayviewGlenLobby); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenLobbyExitNorth);
      final Exit bayviewGlenG11CommonAreaExitSouth = new Exit("S",bayviewGlenG11CommonArea); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenG11CommonAreaExitSouth);
      
      final Exit bayviewGlenOutsideHallwayTheatreBackExitNorth = new Exit("N",bayviewGlenOutsideHallwayTheatreBack); bayviewGlenDeck.addExit(bayviewGlenOutsideHallwayTheatreBackExitNorth);
      final Exit bayviewGlenYorkMillsExitSouth = new Exit("S",bayviewGlenYorkMills); bayviewGlenDeck.addExit(bayviewGlenYorkMillsExitSouth);
      final Exit bayviewGlenG11CommonAreaExitUp = new Exit("U",bayviewGlenG11CommonArea); bayviewGlenDeck.addExit(bayviewGlenG11CommonAreaExitUp);
      final Exit bayviewGlenOutsideStaircaseExitEast = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenDeck.addExit(bayviewGlenOutsideStaircaseExitEast); 

      final Exit bayviewGlenG9CommonAreaExitUp = new Exit("U",bayviewGlenG9CommonArea); bayviewGlenG11CommonArea.addExit(bayviewGlenG9CommonAreaExitUp);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitNorth = new Exit("N",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenG11CommonArea.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitNorth);
      final Exit bayviewGlenDeckExitDown = new Exit("D",bayviewGlenDeck); bayviewGlenG11CommonArea.addExit(bayviewGlenDeckExitDown);
      final Exit bayviewGlenHallwayTheatreBackExitWest = new Exit("W",bayviewGlenHallwayTheatreBack); bayviewGlenG11CommonArea.addExit(bayviewGlenHallwayTheatreBackExitWest);
      final Exit bayviewGlenPrepMusicRoomHallwayExitEast = new Exit("E",bayviewGlenPrepMusicRoomHallway); bayviewGlenG11CommonArea.addExit(bayviewGlenPrepMusicRoomHallwayExitEast);  

      final Exit bayviewGlenUpperMusicHallwayExitEast = new Exit("E",bayviewGlenUpperMusicHallway); bayviewGlenGradHallway.addExit(bayviewGlenUpperMusicHallwayExitEast);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitWest = new Exit("W",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenGradHallway.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitWest);

      final Exit bayviewGlenMusicRoomExitWest = new Exit("W",bayviewGlenMusicRoom); bayviewGlenUpperMusicHallway.addExit(bayviewGlenMusicRoomExitWest);
      final Exit bayviewGlenGradHallwayExitNorth = new Exit("N",bayviewGlenGradHallway); bayviewGlenUpperMusicHallway.addExit(bayviewGlenGradHallwayExitNorth);
      final Exit bayviewGlen2ndFloorUpperHallwayExitSouth = new Exit("S",bayviewGlen2ndFloorUpperHallway); bayviewGlenUpperMusicHallway.addExit(bayviewGlen2ndFloorUpperHallwayExitSouth);

      final Exit bayviewGlenUpperMusicHallwayExitEastTwo = new Exit("E",bayviewGlenUpperMusicHallway); bayviewGlenMusicRoom.addExit(bayviewGlenUpperMusicHallwayExitEastTwo);
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
      final Exit eglintonStreetExitEast = new Exit("E", eglintonStreet); eglintonBusStop.addExit(eglintonStreetExitEast);
      final Exit eglintonBusStopExitWest = new Exit("W", eglintonBusStop); eglintonStreet.addExit(eglintonBusStopExitWest);
      final Exit eglintonStationExitSouth = new Exit("S", eglintonStation); eglintonBusStop.addExit(eglintonStationExitSouth);
      final Exit yongeEglintonMallExitNorth = new Exit("N", yongeEglintonMall); eglintonStreet.addExit(yongeEglintonMallExitNorth);
      final Exit eglintonBusStopExitNorth = new Exit("N", eglintonBusStop); eglintonStation.addExit(eglintonBusStopExitNorth);
      final Exit eglintonStreetExitSouth = new Exit("S", eglintonStreet); yongeEglintonMall.addExit(eglintonStreetExitSouth);
      final Exit yongeEglintonMallExitWest = new Exit("W",yongeEglintonMall); circleK.addExit(yongeEglintonMallExitWest);
      final Exit circleKExitEast = new Exit("E", circleK); yongeEglintonMall.addExit(circleKExitEast);
      final Exit eglintonBusStopExitEast = new Exit("E",eglintonBusStop); foodCourt.addExit(eglintonBusStopExitEast);
      final Exit foodCourtExitWest = new Exit("W", foodCourt); eglintonBusStop.addExit(foodCourtExitWest);
      final Exit eglintonSubwayExitWest = new Exit("W", eglintonSubway); eglintonStation.addExit(eglintonSubwayExitWest);
      final Exit eglintonStationExitEast = new Exit("E", eglintonStation); eglintonSubway.addExit(eglintonStationExitEast);
      // final Exit  = new Exit("", ); .addExit();
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
    this.player.setCurrentRoom(roomMap.get("bayviewglenlobby"));
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
