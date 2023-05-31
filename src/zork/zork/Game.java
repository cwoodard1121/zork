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
import zork.Utils.SoundHandler;
import zork.entites.Enemy;
import zork.entites.Player;
import zork.items.TestItem;
import zork.items.Coupon;
import zork.items.Weapon;
import java.lang.Runnable;

public class Game {

  private final Graphics renderer = new Graphics();
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

  /** the game */
  public static Game getGame() {
    return game;
  }

  /** the renderer */
  public Graphics getRenderer() {
    return renderer;
  }


  /** the player */
  public Player getPlayer() {
    return player;
  }

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
      final Room sheppardYongeLine4StreetHallway = new Room("The escalator is stopped. The door to the street is nearby.","sheppardyongeline4streethallway");
      final Room sheppardYongeLine1HallwayBeforeStreet = new Room("stuff","sheppardyongeline1hallwaybeforestreet");


      //YORK MILLS AREA ROOMS
      final Room yorkMillsBusTerminal = new Room("The bus","yorkmillsbusterminal"); roomMap.put(yorkMillsBusTerminal.getRoomName(),yorkMillsBusTerminal);
      final Room facultyRoom = new Room("A staff room with a few tables", "facultyroom"); roomMap.put(facultyRoom.getRoomName(), facultyRoom);
      final Room gatewayNewsstands = new Room("*Implement shopkeeper* Hello, would you like to purchase anything?", "gatewaynewsstands"); roomMap.put(gatewayNewsstands.getRoomName(), gatewayNewsstands);
      final Room yorkMillsSubwayHallway = new Room("A Hallway is ahead leading to the Subway, Chuck Page plays some guitar for passersby.","yorkmillssubwayhallway"); roomMap.put(yorkMillsSubwayHallway.getRoomName(), yorkMillsSubwayHallway);
      final Room yorkMillsSubway = new Room("Please come back later, unscheduled maintenance has just been scheduled, shuttlebuses are available.", "yorkmillssubway","york mills subway",true,"Placeholder Locked Message");
      


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
      final Room eglintonSubway = new Room ("South leads to St. Clair station, North leads to York Mills Station", "eglintonsubway",true, "Placeholder Locked Message"); eglintonSubway.setLocked(true); roomMap.put(eglintonSubway.getRoomName(), eglintonSubway);

      //ST. CLAIR AREA ROOMS
      final Room stClairSubway = new Room("North leads to Eglinton, South leads to Summerhill", "stclairsubway"); roomMap.put(stClairSubway.getRoomName(), stClairSubway);
      final Room ___it_keeps = new Room("Please leave it alone. It does not like to be disturbed.", "itkeeps"); roomMap.put(___it_keeps.getRoomName(), ___it_keeps);
      final Room pleaseLEAVE = new Room("LEAVE THIS PLaCE**. nOW!!","leave"); roomMap.put(pleaseLEAVE.getRoomName(), pleaseLEAVE);
      final Room theEnd = new Room("...Stay for a while", "theend"); roomMap.put(theEnd.getRoomName(), theEnd);
      final Room stClairStation = new Room("you stand in the main lobby of St. Clair Station.", "stclairstation"); roomMap.put(stClairStation.getRoomName(), stClairStation);
      final Room mcDonalds = new Room("You walk into the cramped subway station McDonald's", "mcdonalds"); roomMap.put(mcDonalds.getRoomName(), mcDonalds);
      final Room stClairAvenue = new Room("You are outside of the station and are facing the slighlty less halted traffic.", "stclairavenue"); roomMap.put(stClairAvenue.getRoomName(), stClairAvenue);
      final Room stClairAndYonge = new Room("You are on the southeast corner of the St. Clair and Yonge intersection.", "stclairandyonge"); roomMap.put(stClairAndYonge.getRoomName(), stClairAndYonge);
      final Room bucaRestaurant = new Room("You are on the north side of St. CLair Avenue West, just outside the restaurant 'Buca'.", "bucarestaurant"); roomMap.put(bucaRestaurant.getRoomName(), bucaRestaurant);
      final Room genericCorporateBuilding = new Room("You stand outside of a towering corporate building, fancy looking people with briefcases walk by.", "genericCorporateBuilding"); 
      final Room wetCement = new Room("You just stepped into wet cement! You'd better get out of there before you get stuck.", "wetcement!"); roomMap.put(wetCement.getRoomName(), wetCement);
      final Room corporateLobby = new Room("You feel very out of place among the other Patrick-Bateman like businessmen in the corporate lobby.", "corporatelobby"); roomMap.put(corporateLobby.getRoomName(), corporateLobby);
      final Room corporateCafe = new Room("You step into a bleak cafe", "corporatecafe"); roomMap.put(corporateCafe.getRoomName(), corporateCafe);
      final Room corporateElevator = new Room("You are on the first floor of the elevator, it is severely cramped and smells like leather briefcases. Would you like to go up?", "corporateelevator"); roomMap.put(corporateElevator.getRoomName(), corporateElevator);
      final Room elevatorSecondFloor = new Room("You arrive on the second floor of the office.", "elevatorsecondfloor"); roomMap.put(elevatorSecondFloor.getRoomName(), elevatorSecondFloor);
      final Room officeRoom = new Room("You enter an office space with people working away. ", "officeroom"); roomMap.put(officeRoom.getRoomName(), officeRoom);
      final Room storageSpace = new Room("You are now in an old decrepid storage area. Spider webs cover the walls, and there is an open window ", "storagespace"); roomMap.put(storageSpace.getRoomName(), storageSpace);
      final Room catwalk = new Room("You stand on a thin plank of wood connecting the building you were just in, and the building beside it.", "catwalk"); roomMap.put(catwalk.getRoomName(), catwalk);



      //SUMMER HILL DEAD END ROOM

      final Room summerhillSubway = new Room("Please stay on the train, police investigation underway", "summerhillSubway"); roomMap.put(summerhillSubway.getRoomName(), summerhillSubway);











      //ELLESMERE AREA ROOMS

      final Room ellesmereStationUnderground = new Room("Tap presto to enter subway.", "ellesmerestationunderground", "Ellesmere Station Entrance");
      final Room ellesmereSubwayNorthbound = new Room("Northbound to McCowan","ellesmeresubwaynorthbound","Ellesmere Station Track Northbound");
      final Room ellesmereSubwaySouthbound = new Room("Southbound to Kennedy","ellesmeresubwaysouthbound","Ellesmere Station Track Southbound");
      






      //BAYVIEW GLEN INDEPENDENT SCHOOL ROOMS
 
      final boolean[] hasEnteredLobby = new boolean[]{false};
      final Room bayviewGlenLobby = new Room ("Placeholder Description for bayviewGlenLobby", "bayviewglenlobby"); roomMap.put(bayviewGlenLobby.getRoomName(), bayviewGlenLobby);  // north exit outside for later looking south when walking in
      bayviewGlenLobby.addItemGround(new Item(0, null, isTesting, null, finished));
      
      final Room bayviewGlenOutsideLobby = new Room ("Placeholder Description for bayviewGlenOutsideLobby", "bayviewglenoutsidelobby"); roomMap.put(bayviewGlenOutsideLobby.getRoomName(), bayviewGlenOutsideLobby);
      final Room bayviewGlenHallwayCafeteria = new Room ("Placeholder Description for bayviewGlenHallwayCafeteria", "bayviewglenhallwaycafeteria"); roomMap.put(bayviewGlenHallwayCafeteria.getRoomName(), bayviewGlenHallwayCafeteria); // to the east from lobby
      final Room bayviewGlenHallwayPrepGym = new Room("Placeholder Description for bayviewGlenHallwayPrepGym", "bayviewglenhallwayprepgym", true, "Since you know the gym is probably locked maybe the hallway by the gym isn't. you were dead wrong"); roomMap.put(bayviewGlenHallwayPrepGym.getRoomName(), bayviewGlenHallwayPrepGym);
      final Room bayviewGlenHallwayTheatreFront = new Room("Placeholder Description for bayviewGlenHallwayTheatreFront", "bayviewglenhallwaytheatrefront", true, "the door is locked, you try to barge in but its locked so that doesnt make any sense."); roomMap.put(bayviewGlenHallwayTheatreFront.getRoomName(), bayviewGlenHallwayTheatreFront);
      final Room bayviewGlenOutsideHallwayTheatreFront = new Room ("Placeholder Description for bayviewGlenOutsideHallwayTheatreFront", "bayviewglenoutsidehallwaytheatrefront"); roomMap.put(bayviewGlenOutsideHallwayTheatreFront.getRoomName(), bayviewGlenOutsideHallwayTheatreFront);
      final Room bayviewGlenCafeteriaFoodArea = new Room("Placeholder Description for bayviewGlenCafeteriaFoodArea", "bayviewglencafeteriafoodarea"); roomMap.put(bayviewGlenCafeteriaFoodArea.getRoomName(), bayviewGlenCafeteriaFoodArea);
      final Room bayviewGlenOutsideCafeteria = new Room ("Placeholder Description for bayviewGlenOutsideCafeteria", "bayviewglenoutsidecafeteria"); roomMap.put(bayviewGlenOutsideCafeteria.getRoomName(), bayviewGlenOutsideCafeteria);
      final Room bayviewGlenKitchen = new Room("Placeholder Description for BayviewGlenKitchen", "bayviewglenkitchen"); roomMap.put(bayviewGlenKitchen.getRoomName(), bayviewGlenKitchen);
      final Room bayviewGlenCafeteriaDiningArea = new Room("Placeholder Description for bayviewGlenCafeteriaDiningArea", "bayviewglencafeteriadiningarea", true, "The cafeteria is closed, you hear your stomach grumbling but then you realize its cafeteria food and your urges subside"); roomMap.put(bayviewGlenCafeteriaDiningArea.getRoomName(), bayviewGlenCafeteriaDiningArea);
      final Room bayviewGlenPrepGym = new Room("Placeholder Description for bayviewGlenPrepGym", "bayviewglenprepgym", true, "The gym is closed and you are unable to get in"); roomMap.put(bayviewGlenPrepGym.getRoomName(), bayviewGlenPrepGym);
      final Room bayviewGlenWeightRoom = new Room("Placeholder Description for bayviewGlenWeightRoom", "bayviewglenweightroom"); roomMap.put(bayviewGlenWeightRoom.getRoomName(), bayviewGlenWeightRoom);
      final Room bayviewGlenOutsidePrepGymNorth = new Room("Placeholder Description for bayviewGlenOutsidePrepGymNorth", "bayviewglenoutsideprepgymnorth"); roomMap.put(bayviewGlenOutsidePrepGymNorth.getRoomName(), bayviewGlenOutsidePrepGymNorth);
      final Room bayviewGlenLearningCommons = new Room("Placeholder Description for bayviewGlenLearningCommons", "bayviewglenlearningcommons"); roomMap.put(bayviewGlenLearningCommons.getRoomName(), bayviewGlenLearningCommons);
      final Room bayviewGlenLearningCommonsSubArea = new Room ("Placeholder Description for bayviewGlenLearningCommonsSubArea", "bayviewglenlearningcommonssubarea"); roomMap.put(bayviewGlenLearningCommonsSubArea.getRoomName(), bayviewGlenLearningCommonsSubArea);
      final Room bayviewGlenTheatre = new Room("Placeholder Description for bayviewGlenTheatre", "bayviewglentheatre"); roomMap.put(bayviewGlenTheatre.getRoomName(), bayviewGlenTheatre);
      final Room bayviewGlenHallwayTheatreBack = new Room ("Placeholder Description for bayviewGlenHallwayTheatreBack", "bayviewglenhallwaytheatreback"); roomMap.put(bayviewGlenHallwayTheatreBack.getRoomName(), bayviewGlenHallwayTheatreBack);
      final Room bayviewGlenHallway2ndFloorToUpperSchool = new Room("Placeholder Description for bayviewGlenHallway2ndFloorToUpperSchool", "bayviewglenhallway2ndfloortoupperschool"); roomMap.put(bayviewGlenHallway2ndFloorToUpperSchool.getRoomName(), bayviewGlenHallway2ndFloorToUpperSchool);
      final Room bayviewGlenG11CommonArea = new Room("Placeholder Description for bayviewGlenG11CommonArea", "bayviewgleng11commonarea", true, "You go up the stairs and try to open the door, but as usual its locked an nobody is there to let you in."); roomMap.put(bayviewGlenG11CommonArea.getRoomName(), bayviewGlenG11CommonArea);
      final Room bayviewGlenGradHallway = new Room("Placeholder Description for bayviewGlenGradHallway", "bayviewglengradhallway"); roomMap.put(bayviewGlenGradHallway.getRoomName(), bayviewGlenGradHallway);
      final Room bayviewGlenUpperMusicHallway = new Room ("Placeholder Description for bayviewGlenUpperMusicHallway", "bayviewglenuppermusichallway"); roomMap.put(bayviewGlenUpperMusicHallway.getRoomName(), bayviewGlenUpperMusicHallway);
      final Room bayviewGlenDramaRoom = new Room ("Placeholder Description for bayviewGlenDramaRoom", "bayviewglendramaroom"); roomMap.put(bayviewGlenDramaRoom.getRoomName(), bayviewGlenDramaRoom);
      final Room bayviewGlenDeck = new Room ("Placeholder Description for bayviewGlenDeck", "bayviewglendeck"); roomMap.put(bayviewGlenDeck.getRoomName(), bayviewGlenDeck);
      final Room bayviewGlenYorkMills = new Room ("Placeholder Description for bayviewGlenYorkMills", "bayviewglenyorkmills"); roomMap.put(bayviewGlenYorkMills.getRoomName(), bayviewGlenYorkMills);
      final Room bayviewGlen3rdFloorLobby = new Room ("Placeholder Description for bayviewGlen3rdFloorLobby", "bayviewglen3rdfloorlobby"); roomMap.put(bayviewGlen3rdFloorLobby.getRoomName(), bayviewGlen3rdFloorLobby);
      final Room bayviewGlen3rdFloorWestPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen3rdFloorWestPrepStairwayHallway", "bayviewglen3rdfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorWestPrepStairwayHallway);
      final Room bayviewGlen3rdFloorEastPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen3rdFloorEast'PrepStairwayHallway", "bayviewglen3rdflooreastprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorEastPrepStairwayHallway);
      final Room bayviewGlenG9CommonArea = new Room ("Placeholder Description for bayviewGlenG9CommonArea", "bayviewgleng9commonarea"); roomMap.put(bayviewGlenG9CommonArea.getRoomName(), bayviewGlenG9CommonArea);
      Inventory meatBall = new Inventory(5);
      meatBall.addItem(new Weapon(3, "Fork", false, 10, null));
      final Enemy cyrus_meatball = new Enemy(null, bayviewGlenG9CommonArea, Constants.PlayerConstants.DEFAULT_HEALTH * 2,meatBall , 0, "Meatball");
      bayviewGlenG9CommonArea.addEnemies(cyrus_meatball);
      bayviewGlenG9CommonArea.setRunnable(new Runnable() {

        @Override
        public void run() {
          System.out.println("Meatball is in the way of cyrus's locker. ");
          Fight f = new Fight(cyrus_meatball);
          boolean won = f.fight();
          if(won) {
            Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(cyrus_meatball);
          }

        }

      });
      final Room bayviewGlenOutsideStaircase = new Room ("Placeholder Description for bayviewGlenOutsideStaircase", "bayviewglenoutsidestaircase"); roomMap.put(bayviewGlenOutsideStaircase.getRoomName(), bayviewGlenOutsideStaircase);
      final Room bayviewGlenOutsideWest = new Room ("Placeholder Description for bayviewGlenOutsideWest", "bayviewglenoutsidewest"); roomMap.put(bayviewGlenOutsideWest.getRoomName(), bayviewGlenOutsideWest);
      final Room bayviewGlenMusicRoom = new Room ("Placeholder Description for bayviewGlenMusicRoom", "bayviewglenmusicroom"); roomMap.put(bayviewGlenMusicRoom.getRoomName(), bayviewGlenMusicRoom);
      final Room bayviewGlen2ndFloorUpperHallway = new Room ("Placeholder Description for bayviewGlen2ndFloorUpperHallway", "bayviewglen2ndfloorupperhallway"); roomMap.put(bayviewGlen2ndFloorUpperHallway.getRoomName(), bayviewGlen2ndFloorUpperHallway);
      final Room bayviewGlenHallway3rdFloorByElevator = new Room ("Placeholder Description for bayviewGlenHallway3rdFloorByElevator", "bayviewglenhallway3rdfloorbyelevator"); roomMap.put(bayviewGlenHallway3rdFloorByElevator.getRoomName(), bayviewGlenHallway3rdFloorByElevator);
      final Room bayviewGlenMathWing = new Room ("Placeholder Description for bayviewGlenMathWing", "bayviewglenmathwing"); roomMap.put(bayviewGlenMathWing.getRoomName(), bayviewGlenMathWing);
      final Room bayviewGlen3rdFloorUpperSchoolHallway = new Room ("Placeholder Description for bayviewGlen3rdFloorUpperSchoolHallway", "bayviewglen3rdfloorupperschoolhallway"); roomMap.put(bayviewGlen3rdFloorUpperSchoolHallway.getRoomName(), bayviewGlen3rdFloorUpperSchoolHallway);
      final Room bayviewGlenG10CommonArea = new Room ("Placeholder Description for bayviewGlenG10CommonArea", "bayviewgleng10commonarea"); roomMap.put(bayviewGlenG10CommonArea.getRoomName(), bayviewGlenG10CommonArea);
      final Room bayviewGlen3rdFloorBridge = new Room ("Placeholder Description for bayviewGlen3rdFloorBridge", "bayviewglen3rdfloorbridge"); roomMap.put(bayviewGlen3rdFloorBridge.getRoomName(), bayviewGlen3rdFloorBridge);
      final Room bayviewGlen4thFloorEastPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen4thFloorEastPrepStairwayHallway", "bayviewglen4thflooreastprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorEastPrepStairwayHallway);
      final Room bayviewGlen4thFloorWestPrepStairwayHallway = new Room ("Placeholder Description for bayviewGlen4thFloorWestPrepStairwayHallway", "bayviewglen4thfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorWestPrepStairwayHallway);
      final Room bayviewGlen4thFloorLobby = new Room ("Placeholder Description for bayviewGlen4thFloorLobby", "bayviewglen4thfloorlobby"); roomMap.put(bayviewGlen4thFloorLobby.getRoomName(), bayviewGlen4thFloorLobby);
      final Room bayviewGlenPrepStaffRoom = new Room ("Placeholder Description for bayviewGlenPrepStaffRoom", "bayviewglenprepstaffroom"); roomMap.put(bayviewGlenPrepStaffRoom.getRoomName(), bayviewGlenPrepStaffRoom);
      final Room bayviewGlen1stFloorBelowG11CommonArea = new Room ("Placeholder Description for bayviewGlen1stFloorBelowG11CommonArea", "bayviewglen1stfloorbelowg11commonarea"); roomMap.put(bayviewGlen1stFloorBelowG11CommonArea.getRoomName(), bayviewGlen1stFloorBelowG11CommonArea);
      final Room bayviewGlenG12CommonArea = new Room ("Placeholder Description for bayviewGlenG12CommonArea", "bayviewgleng12commonarea"); roomMap.put(bayviewGlenG12CommonArea.getRoomName(), bayviewGlenG12CommonArea);
      final Room bayviewGlenArtRoom = new Room ("Placeholder Description for bayviewGlenArtRoom", "bayviewglenartroom"); roomMap.put(bayviewGlenArtRoom.getRoomName(), bayviewGlenArtRoom);
      final Room bayviewGlenHallwayOutsideUpperGym = new Room ("Placeholder Description for bayviewGlenHallwayOutsideUpperGym", "bayviewglenhallwayoutsideuppergym"); roomMap.put(bayviewGlenHallwayOutsideUpperGym.getRoomName(), bayviewGlenHallwayOutsideUpperGym);
      final Room bayviewGlenUpperGym = new Room ("Placeholder Description for bayviewGlenUpperGym", "bayviewglenuppergym", true, "The gym is closed, and you can't get in."); roomMap.put(bayviewGlenUpperGym.getRoomName(), bayviewGlenUpperGym);

      bayviewGlenLobby.setRunnable(new Runnable(){

        @Override
        public void run() {
          if (!hasEnteredLobby[0]) {
            hasEnteredLobby[0] = true;
            try {
              renderer.showCutScene(1500, "\\bin\\zork\\data\\bayviewglencyruscall.txt", 35);
            } catch (Exception e) {
              handleException(e);
              bayviewGlenHallwayTheatreFront.setLocked(false);
              bayviewGlenUpperGym.setLocked(false);
              bayviewGlenG11CommonArea.setLocked(false);
              bayviewGlenHallwayPrepGym.setLocked(false);
              bayviewGlenPrepGym.setLocked(false);
              bayviewGlenCafeteriaDiningArea.setLocked(false);

            }
          }
        }
      });


      //BVG EXITS
      final Exit bayviewGlenOutsideLobbyExitNorth = new Exit("N",bayviewGlenOutsideLobby); bayviewGlenLobby.addExit(bayviewGlenOutsideLobbyExitNorth); 
      final Exit bayviewGlenHallwayCafeteriaExitEast = new Exit("E",bayviewGlenHallwayCafeteria); bayviewGlenLobby.addExit(bayviewGlenHallwayCafeteriaExitEast);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitSouth = new Exit("S",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenLobby.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitSouth);
      final Exit bayviewGlenHallwayTheatreFrontExitWest = new Exit("W",bayviewGlenHallwayTheatreFront); bayviewGlenLobby.addExit(bayviewGlenHallwayTheatreFrontExitWest);
      final Exit bayviewGlen3rdFloorLobbyExitUp = new Exit("U",bayviewGlen3rdFloorLobby); bayviewGlenLobby.addExit(bayviewGlen3rdFloorLobbyExitUp);

      final Exit bayviewGlenLobbyExitSouth = new Exit("S",bayviewGlenLobby); bayviewGlenOutsideLobby.addExit(bayviewGlenLobbyExitSouth);
      final Exit bayviewGlenOutsideCafeteriaExitEast = new Exit("E",bayviewGlenOutsideCafeteria); bayviewGlenOutsideLobby.addExit(bayviewGlenOutsideCafeteriaExitEast);
      final Exit bayviewGlenOutsideHallwayTheatreFrontExitWest = new Exit("W",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenOutsideLobby.addExit(bayviewGlenOutsideHallwayTheatreFrontExitWest);

      final Exit bayviewGlenLearningCommonsExitSouth = new Exit("S",bayviewGlenLearningCommons); bayviewGlenHallwayCafeteria.addExit(bayviewGlenLearningCommonsExitSouth);
      final Exit bayviewGlenCafeteriaFoodAreaExitNorth = new Exit("N",bayviewGlenCafeteriaFoodArea); bayviewGlenHallwayCafeteria.addExit(bayviewGlenCafeteriaFoodAreaExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitEast = new Exit("E",bayviewGlenHallwayPrepGym); bayviewGlenHallwayCafeteria.addExit(bayviewGlenHallwayPrepGymExitEast);
      final Exit bayviewGlenLobbyExitWest = new Exit("W",bayviewGlenLobby); bayviewGlenHallwayCafeteria.addExit(bayviewGlenLobbyExitWest);

      final Exit bayviewGlenPrepGymExitNorth = new Exit("N",bayviewGlenPrepGym); bayviewGlenHallwayPrepGym.addExit(bayviewGlenPrepGymExitNorth);
      final Exit bayviewGlenOutsideStaircaseExitEast = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenHallwayPrepGym.addExit(bayviewGlenOutsideStaircaseExitEast);
      final Exit bayviewGlenWeightRoomExitSouth = new Exit("S",bayviewGlenWeightRoom); bayviewGlenHallwayPrepGym.addExit(bayviewGlenWeightRoomExitSouth);
      final Exit bayviewGlenHallwayCafeteriaExitWest = new Exit("W",bayviewGlenHallwayCafeteria); bayviewGlenHallwayPrepGym.addExit(bayviewGlenHallwayCafeteriaExitWest);
      final Exit bayviewGlen3rdFloorEastPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen3rdFloorEastPrepStairwayHallway); bayviewGlenHallwayPrepGym.addExit(bayviewGlen3rdFloorEastPrepStairwayHallwayExitUp);

      final Exit bayviewGlenOutsideHallwayTheatreFrontExitNorth = new Exit("N",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenOutsideHallwayTheatreFrontExitNorth);
      final Exit bayviewGlenTheatreExitSouth = new Exit("S",bayviewGlenTheatre); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenTheatreExitSouth);
      final Exit bayviewGlenLobbyExitEast = new Exit("E",bayviewGlenLobby); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenLobbyExitEast);
      final Exit bayviewGlenOutsideWestExitWest = new Exit("W",bayviewGlenOutsideWest); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenOutsideWestExitWest);
      final Exit bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen3rdFloorWestPrepStairwayHallway); bayviewGlenHallwayTheatreFront.addExit(bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp);
      
      final Exit bayviewGlenHallwayTheatreFrontExitSouth = new Exit("S",bayviewGlenHallwayTheatreFront); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenHallwayTheatreFrontExitSouth);
      final Exit bayviewGlenOutsideLobbyExitEast = new Exit("E",bayviewGlenOutsideLobby); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenOutsideLobbyExitEast);
      final Exit bayviewGlenOutsideWestExitWestTwo = new Exit("W",bayviewGlenOutsideWest); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenOutsideWestExitWestTwo);

      final Exit bayviewGlenCafeteriaDiningAreaExitNorth = new Exit("N",bayviewGlenCafeteriaDiningArea); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenCafeteriaDiningAreaExitNorth);
      final Exit bayviewGlenHallwayCafeteriaExitSouth = new Exit("S",bayviewGlenHallwayCafeteria); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenHallwayCafeteriaExitSouth);
      final Exit bayviewGlenKitchenExitEast = new Exit("E",bayviewGlenKitchen); bayviewGlenCafeteriaFoodArea.addExit(bayviewGlenKitchenExitEast);
    
      final Exit bayviewGlenCafeteriaFoodAreaExitWest = new Exit("W",bayviewGlenCafeteriaFoodArea); bayviewGlenKitchen.addExit(bayviewGlenCafeteriaFoodAreaExitWest);

      final Exit bayviewGlenCafeteriaFoodAreaExitSouth = new Exit("S",bayviewGlenCafeteriaFoodArea); bayviewGlenCafeteriaDiningArea.addExit(bayviewGlenCafeteriaFoodAreaExitSouth);
      final Exit bayviewGlenOutsideCafeteriaExitNorth = new Exit("N",bayviewGlenOutsideCafeteria); bayviewGlenCafeteriaDiningArea.addExit(bayviewGlenOutsideCafeteriaExitNorth);
      
      final Exit bayviewGlenOutsideLobbyExitWest = new Exit("W",bayviewGlenOutsideLobby); bayviewGlenOutsideCafeteria.addExit(bayviewGlenOutsideLobbyExitWest);
      final Exit bayviewGlenOutsidePrepGymExitEast = new Exit("E",bayviewGlenOutsidePrepGymNorth); bayviewGlenOutsideCafeteria.addExit(bayviewGlenOutsidePrepGymExitEast);   
      final Exit bayviewGlenCafeteriaDiningAreaExitSouth = new Exit("S",bayviewGlenCafeteriaDiningArea); bayviewGlenOutsideCafeteria.addExit(bayviewGlenCafeteriaDiningAreaExitSouth);
      
      final Exit bayviewGlenOutsidePrepGymExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenPrepGym.addExit(bayviewGlenOutsidePrepGymExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitSouth = new Exit("S",bayviewGlenHallwayPrepGym); bayviewGlenPrepGym.addExit(bayviewGlenHallwayPrepGymExitSouth);

      final Exit bayviewGlenHallwayPrepGymExitNorth = new Exit("N",bayviewGlenHallwayPrepGym); bayviewGlenWeightRoom.addExit(bayviewGlenHallwayPrepGymExitNorth);

      final Exit bayviewGlenOutsideCafeteriaExitWest = new Exit("W",bayviewGlenOutsideCafeteria); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsideCafeteriaExitWest);
      final Exit bayviewGlenOutsideStaircaseExitEastTwo = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsideStaircaseExitEastTwo);
      final Exit bayviewGlenPrepGymExitSouth = new Exit("S",bayviewGlenPrepGym); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenPrepGymExitSouth);

      final Exit bayviewGlenOutsidePrepGymNorthExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenOutsideStaircase.addExit(bayviewGlenOutsidePrepGymNorthExitNorth);
      final Exit bayviewGlenHallwayPrepGymExitWest = new Exit("W",bayviewGlenHallwayPrepGym); bayviewGlenOutsideStaircase.addExit(bayviewGlenHallwayPrepGymExitWest); 
      final Exit bayviewGlenDeckExitWest = new Exit("S",bayviewGlenDeck); bayviewGlenOutsideStaircase.addExit(bayviewGlenDeckExitWest); 

      final Exit bayviewGlenHallwayCafeteriaExitNorth = new Exit("N",bayviewGlenHallwayCafeteria); bayviewGlenLearningCommons.addExit(bayviewGlenHallwayCafeteriaExitNorth);
      final Exit bayviewGlenLearningCommonsSubAreaExitWest = new Exit("W",bayviewGlenLearningCommonsSubArea); bayviewGlenLearningCommons.addExit(bayviewGlenLearningCommonsSubAreaExitWest);

      final Exit bayviewGlenLearningCommonsExitEast = new Exit("E", bayviewGlenLearningCommons); bayviewGlenLearningCommonsSubArea.addExit(bayviewGlenLearningCommonsExitEast);

      final Exit bayviewGlenHallwayTheatreFrontExitNorth = new Exit("N",bayviewGlenHallwayTheatreFront); bayviewGlenTheatre.addExit(bayviewGlenHallwayTheatreFrontExitNorth);
      final Exit bayviewGlenHallwayTheatreBackExitSouth = new Exit("S",bayviewGlenHallwayTheatreBack); bayviewGlenTheatre.addExit(bayviewGlenHallwayTheatreBackExitSouth);

      final Exit bayviewGlenTheatreExitNorth = new Exit("N",bayviewGlenTheatre); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenTheatreExitNorth);
      final Exit bayviewGlenDramaRoomExitSouth = new Exit("S",bayviewGlenDramaRoom); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenDramaRoomExitSouth);
      final Exit bayviewGlenG11CommonAreaExitEast = new Exit("E",bayviewGlenG11CommonArea); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenG11CommonAreaExitEast);
      final Exit bayviewGlenOutsideWestExitWestFour = new Exit("W",bayviewGlenOutsideWest); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenOutsideWestExitWestFour);
      final Exit bayviewGlenMathWingExitUp = new Exit("U",bayviewGlenMathWing); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenMathWingExitUp);
      final Exit bayviewGlenHallwayOutsideUpperGymExitDown = new Exit("D",bayviewGlenHallwayOutsideUpperGym); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenHallwayOutsideUpperGymExitDown);

      final Exit bayviewGlenTheatreHallwayBackExitNorth = new Exit("N",bayviewGlenHallwayTheatreBack); bayviewGlenDramaRoom.addExit(bayviewGlenTheatreHallwayBackExitNorth);

      final Exit bayviewGlenUpperMusicHallwayExitSouth = new Exit("S",bayviewGlenUpperMusicHallway); bayviewGlenMusicRoom.addExit(bayviewGlenUpperMusicHallwayExitSouth);
      
      final Exit bayviewGlenLobbyExitNorth = new Exit("N",bayviewGlenLobby); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenLobbyExitNorth);
      final Exit bayviewGlenG11CommonAreaExitSouth = new Exit("S",bayviewGlenG11CommonArea); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenG11CommonAreaExitSouth);
      final Exit bayviewGlenGradHallwayExitEast = new Exit("E",bayviewGlenGradHallway); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenGradHallwayExitEast);

      final Exit bayviewGlenYorkMillsExitSouth = new Exit("S",bayviewGlenYorkMills); bayviewGlenDeck.addExit(bayviewGlenYorkMillsExitSouth);
      final Exit bayviewGlenG11CommonAreaExitUp = new Exit("U",bayviewGlenG11CommonArea); bayviewGlenDeck.addExit(bayviewGlenG11CommonAreaExitUp);
      final Exit bayviewGlenOutsideStaircaseExitEastThree = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenDeck.addExit(bayviewGlenOutsideStaircaseExitEastThree); 
      final Exit bayviewGlenOutsideWestExitWestThree = new Exit("W",bayviewGlenOutsideWest); bayviewGlenDeck.addExit(bayviewGlenOutsideWestExitWestThree);
      final Exit bayviewGlenUpperGymExitNorth = new Exit("N",bayviewGlenUpperGym); bayviewGlenDeck.addExit(bayviewGlenUpperGymExitNorth);

      final Exit bayviewGlenG9CommonAreaExitUp = new Exit("U",bayviewGlenG9CommonArea); bayviewGlenG11CommonArea.addExit(bayviewGlenG9CommonAreaExitUp);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitNorth = new Exit("N",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenG11CommonArea.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitNorth);
      final Exit bayviewGlenDeckExitSouth = new Exit("S",bayviewGlenDeck); bayviewGlenG11CommonArea.addExit(bayviewGlenDeckExitSouth);
      final Exit bayviewGlenHallwayTheatreBackExitWest = new Exit("W",bayviewGlenHallwayTheatreBack); bayviewGlenG11CommonArea.addExit(bayviewGlenHallwayTheatreBackExitWest);
      final Exit bayviewGlen2ndFloorUpperHallwayExitEast = new Exit("E",bayviewGlen2ndFloorUpperHallway); bayviewGlenG11CommonArea.addExit(bayviewGlen2ndFloorUpperHallwayExitEast);
      final Exit bayviewGlen1stFloorBelowG11CommonAreaExitDown = new Exit("D",bayviewGlen1stFloorBelowG11CommonArea); bayviewGlenG11CommonArea.addExit(bayviewGlen1stFloorBelowG11CommonAreaExitDown);

      final Exit bayviewGlenG12CommonAreaExitDown = new Exit("D",bayviewGlenG12CommonArea); bayviewGlen2ndFloorUpperHallway.addExit(bayviewGlenG12CommonAreaExitDown);

      final Exit bayviewGlenUpperMusicHallwayExitEast = new Exit("E",bayviewGlenUpperMusicHallway); bayviewGlenGradHallway.addExit(bayviewGlenUpperMusicHallwayExitEast);
      final Exit bayviewGlenHallway2ndFloorToUpperSchoolExitWest = new Exit("W",bayviewGlenHallway2ndFloorToUpperSchool); bayviewGlenGradHallway.addExit(bayviewGlenHallway2ndFloorToUpperSchoolExitWest);

      final Exit bayviewGlenMusicRoomExitNorth = new Exit("N",bayviewGlenMusicRoom); bayviewGlenUpperMusicHallway.addExit(bayviewGlenMusicRoomExitNorth);
      final Exit bayviewGlenGradHallwayExitWest = new Exit("W",bayviewGlenGradHallway); bayviewGlenUpperMusicHallway.addExit(bayviewGlenGradHallwayExitWest);
      final Exit bayviewGlen2ndFloorUpperHallwayExitSouth = new Exit("S",bayviewGlen2ndFloorUpperHallway); bayviewGlenUpperMusicHallway.addExit(bayviewGlen2ndFloorUpperHallwayExitSouth);

      final Exit bayviewGlenUpperMusicHallwayExitEastTwo = new Exit("E",bayviewGlenUpperMusicHallway); bayviewGlenMusicRoom.addExit(bayviewGlenUpperMusicHallwayExitEastTwo);

      final Exit bayviewGlenUpperMusicHallwayExitNorth = new Exit("N",bayviewGlenUpperMusicHallway); bayviewGlen2ndFloorUpperHallway.addExit(bayviewGlenUpperMusicHallwayExitNorth);
      final Exit bayviewGlenHallway3rdFloorByElevatorExitUp = new Exit("U", bayviewGlenHallway3rdFloorByElevator); bayviewGlen2ndFloorUpperHallway.addExit(bayviewGlenHallway3rdFloorByElevatorExitUp);
      final Exit bayviewGlenG11CommonAreaExitWest = new Exit("W",bayviewGlenG11CommonArea); bayviewGlen2ndFloorUpperHallway.addExit(bayviewGlenG11CommonAreaExitWest);
      
      final Exit bayviewGlenDeckExitSouthTwo = new Exit("S",bayviewGlenDeck); bayviewGlenOutsideWest.addExit(bayviewGlenDeckExitSouthTwo);
      final Exit bayviewGlenOutsideHallwayTheatreFrontExitNorthTwo = new Exit("N",bayviewGlenOutsideHallwayTheatreFront); bayviewGlenOutsideWest.addExit(bayviewGlenOutsideHallwayTheatreFrontExitNorthTwo);
      final Exit bayviewGlenHallwayTheatreFrontExitEast = new Exit("E",bayviewGlenHallwayTheatreFront); bayviewGlenOutsideWest.addExit(bayviewGlenHallwayTheatreFrontExitEast);
      
      final Exit bayviewGlenG11CommonAreaExitDown = new Exit("D",bayviewGlenG11CommonArea); bayviewGlenG9CommonArea.addExit(bayviewGlenG11CommonAreaExitDown);
      final Exit bayviewGlen3rdFloorBridgeExitNorth = new Exit("N",bayviewGlen3rdFloorBridge); bayviewGlenG9CommonArea.addExit(bayviewGlen3rdFloorBridgeExitNorth);
      final Exit bayviewGlen3rdFloorUpperSchoolHallwayExitSouth = new Exit("S",bayviewGlen3rdFloorUpperSchoolHallway); bayviewGlenG9CommonArea.addExit(bayviewGlen3rdFloorUpperSchoolHallwayExitSouth);
      final Exit bayviewGlenMathWingExitWest = new Exit("W",bayviewGlenMathWing); bayviewGlenG9CommonArea.addExit(bayviewGlenMathWingExitWest);

      final Exit bayviewGlenHallwayTheatreBackExitDown = new Exit("D",bayviewGlenHallwayTheatreBack); bayviewGlenMathWing.addExit(bayviewGlenHallwayTheatreBackExitDown);
      final Exit bayviewGlenG9CommonAreaExitEast = new Exit("E",bayviewGlenG9CommonArea); bayviewGlenMathWing.addExit(bayviewGlenG9CommonAreaExitEast);

      final Exit bayviewGlenHallway3rdFloorByElevatorExitSouth = new Exit("S",bayviewGlenHallway3rdFloorByElevator); bayviewGlen3rdFloorUpperSchoolHallway.addExit(bayviewGlenHallway3rdFloorByElevatorExitSouth);
      final Exit bayviewGleng10CommonAreaExitEast = new Exit("E",bayviewGlenG10CommonArea); bayviewGlen3rdFloorUpperSchoolHallway.addExit(bayviewGleng10CommonAreaExitEast);
      final Exit bayviewGlenG9CommonAreaExitNorth = new Exit("N",bayviewGlenG9CommonArea); bayviewGlen3rdFloorUpperSchoolHallway.addExit(bayviewGlenG9CommonAreaExitNorth);

      final Exit bayviewGlen3rdFloorUpperSchoolHallwayExitWest = new Exit("W",bayviewGlen3rdFloorUpperSchoolHallway); bayviewGlenG10CommonArea.addExit(bayviewGlen3rdFloorUpperSchoolHallwayExitWest);
      final Exit bayviewGlenHallway3rdFloorByElevatorExitSouthtwo = new Exit("S",bayviewGlenHallway3rdFloorByElevator); bayviewGlenG10CommonArea.addExit(bayviewGlenHallway3rdFloorByElevatorExitSouthtwo);

      final Exit bayviewGlen2ndFloorUpperHallwayExitDown = new Exit("D",bayviewGlen2ndFloorUpperHallway); bayviewGlenHallway3rdFloorByElevator.addExit(bayviewGlen2ndFloorUpperHallwayExitDown);
      final Exit bayviewGlen3rdFloorUpperSchoolHallwayExitNorth = new Exit("N",bayviewGlen3rdFloorUpperSchoolHallway); bayviewGlenHallway3rdFloorByElevator.addExit(bayviewGlen3rdFloorUpperSchoolHallwayExitNorth);  
      final Exit bayviewGlenG10CommonAreaExitEast = new Exit("E",bayviewGlenG10CommonArea); bayviewGlenHallway3rdFloorByElevator.addExit(bayviewGlenG10CommonAreaExitEast);

      final Exit bayviewGlenG9CommonAreaExitSouth = new Exit("S",bayviewGlenG9CommonArea); bayviewGlen3rdFloorBridge.addExit(bayviewGlenG9CommonAreaExitSouth);
      final Exit bayviewGlen3rdFloorLobbyExitNorth = new Exit("N",bayviewGlen3rdFloorLobby); bayviewGlen3rdFloorBridge.addExit(bayviewGlen3rdFloorLobbyExitNorth);

      final Exit bayviewGlen4thFloorWestPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen4thFloorWestPrepStairwayHallway); bayviewGlen3rdFloorWestPrepStairwayHallway.addExit(bayviewGlen4thFloorWestPrepStairwayHallwayExitUp);
      final Exit bayviewGlenHallwayTheatreFrontExitDown = new Exit("D",bayviewGlenHallwayTheatreFront); bayviewGlen3rdFloorWestPrepStairwayHallway.addExit(bayviewGlenHallwayTheatreFrontExitDown);
      final Exit bayviewGlen3rdFloorLobbyExitEast = new Exit("E",bayviewGlen3rdFloorLobby); bayviewGlen3rdFloorWestPrepStairwayHallway.addExit(bayviewGlen3rdFloorLobbyExitEast);

      final Exit bayviewGlenHallwayPrepGymExitDown = new Exit("D",bayviewGlenHallwayPrepGym); bayviewGlen3rdFloorEastPrepStairwayHallway.addExit(bayviewGlenHallwayPrepGymExitDown);
      final Exit bayviewGlen4thFloorEastPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen4thFloorEastPrepStairwayHallway); bayviewGlen3rdFloorEastPrepStairwayHallway.addExit(bayviewGlen4thFloorEastPrepStairwayHallwayExitUp);
      final Exit bayviewGlen3rdFloorLobbyExitWest = new Exit("W",bayviewGlen3rdFloorLobby); bayviewGlen3rdFloorEastPrepStairwayHallway.addExit(bayviewGlen3rdFloorLobbyExitWest);
      final Exit bayviewGlenPrepStaffRoomExitSouth = new Exit("S",bayviewGlenPrepStaffRoom); bayviewGlen3rdFloorEastPrepStairwayHallway.addExit(bayviewGlenPrepStaffRoomExitSouth);
      
      final Exit bayviewGlen3rdFloorEastPrepStairwayHallwayExitDown = new Exit("D",bayviewGlen3rdFloorEastPrepStairwayHallway); bayviewGlen4thFloorEastPrepStairwayHallway.addExit(bayviewGlen3rdFloorEastPrepStairwayHallwayExitDown);
      final Exit bayviewGlen4thFloorLobbyExitWest = new Exit("W",bayviewGlen4thFloorLobby); bayviewGlen4thFloorEastPrepStairwayHallway.addExit(bayviewGlen4thFloorLobbyExitWest);

      final Exit bayviewGlen3rdFloorWestPrepStairwayHallwayExitDown = new Exit("D",bayviewGlen3rdFloorWestPrepStairwayHallway); bayviewGlen4thFloorWestPrepStairwayHallway.addExit(bayviewGlen3rdFloorWestPrepStairwayHallwayExitDown);
      final Exit bayviewGlen4thFloorLobbyExitEast = new Exit("E",bayviewGlen4thFloorLobby); bayviewGlen4thFloorWestPrepStairwayHallway.addExit(bayviewGlen4thFloorLobbyExitEast);

      final Exit bayviewGlenLobbyExitDown = new Exit("D",bayviewGlenLobby); bayviewGlen3rdFloorLobby.addExit(bayviewGlenLobbyExitDown);
      final Exit bayviewGlen4thFloorLobbyExitUp = new Exit("U",bayviewGlen4thFloorLobby); bayviewGlen3rdFloorLobby.addExit(bayviewGlen4thFloorLobbyExitUp);
      final Exit bayviewGlen3rdFloorEastPrepStairwayHallwayExitEast = new Exit("E",bayviewGlen3rdFloorEastPrepStairwayHallway); bayviewGlen3rdFloorLobby.addExit(bayviewGlen3rdFloorEastPrepStairwayHallwayExitEast);  
      final Exit bayviewGlen3rdFloorWestPrepStairwayHallwayExitWest = new Exit("W",bayviewGlen3rdFloorWestPrepStairwayHallway); bayviewGlen3rdFloorLobby.addExit(bayviewGlen3rdFloorWestPrepStairwayHallwayExitWest);  
      final Exit bayviewGlen3rdFloorBridgeExitSouth = new Exit("S",bayviewGlen3rdFloorBridge); bayviewGlen3rdFloorLobby.addExit(bayviewGlen3rdFloorBridgeExitSouth);

      final Exit bayviewGlen4thFloorEastPrepStairwayHallwayExitEast = new Exit("E",bayviewGlen4thFloorEastPrepStairwayHallway); bayviewGlen4thFloorLobby.addExit(bayviewGlen4thFloorEastPrepStairwayHallwayExitEast);
      final Exit bayviewGlen4thFloorWestPrepStairwayHallwayExitWest = new Exit("W",bayviewGlen4thFloorWestPrepStairwayHallway); bayviewGlen4thFloorLobby.addExit(bayviewGlen4thFloorWestPrepStairwayHallwayExitWest); 
      final Exit bayviewGlen3rdFloorLobbyExitDown = new Exit("D",bayviewGlen3rdFloorLobby); bayviewGlen4thFloorLobby.addExit(bayviewGlen3rdFloorLobbyExitDown);

      final Exit bayviewGlen3rdFloorEastPrepStairwayHallwayExitNorth = new Exit("N",bayviewGlen3rdFloorEastPrepStairwayHallway); bayviewGlenPrepStaffRoom.addExit(bayviewGlen3rdFloorEastPrepStairwayHallwayExitNorth);

      final Exit bayviewGlenG11CommonAreaExitUpTwo = new Exit("U",bayviewGlenG11CommonArea); bayviewGlen1stFloorBelowG11CommonArea.addExit(bayviewGlenG11CommonAreaExitUpTwo);
      final Exit bayviewGlenHallwayOutsideUpperGymExitWest = new Exit("W",bayviewGlenHallwayOutsideUpperGym); bayviewGlen1stFloorBelowG11CommonArea.addExit(bayviewGlenHallwayOutsideUpperGymExitWest);
      final Exit bayviewGlenG12CommonAreaExitEast = new Exit("E",bayviewGlenG12CommonArea); bayviewGlen1stFloorBelowG11CommonArea.addExit(bayviewGlenG12CommonAreaExitEast);

      final Exit bayviewGlen1stFloorBelowG11CommonAreaExitWest = new Exit("W",bayviewGlen1stFloorBelowG11CommonArea); bayviewGlenG12CommonArea.addExit(bayviewGlen1stFloorBelowG11CommonAreaExitWest);
      final Exit bayviewGlen2ndFloorUpperHallwayExitUp = new Exit("U",bayviewGlen2ndFloorUpperHallway); bayviewGlenG12CommonArea.addExit(bayviewGlen2ndFloorUpperHallwayExitUp);
      final Exit bayviewGlenOutsideStaircaseExitEastFour = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenG12CommonArea.addExit(bayviewGlenOutsideStaircaseExitEastFour);
      final Exit bayviewGlenArtRoomExitDown = new Exit("D",bayviewGlenArtRoom); bayviewGlenG12CommonArea.addExit(bayviewGlenArtRoomExitDown);

      final Exit bayviewGlenG12CommonAreaExitUp = new Exit("U",bayviewGlenG12CommonArea); bayviewGlenArtRoom.addExit(bayviewGlenG12CommonAreaExitUp);

      final Exit bayviewGlenUpperGymExitSouth = new Exit("S",bayviewGlenUpperGym); bayviewGlenHallwayOutsideUpperGym.addExit(bayviewGlenUpperGymExitSouth);
      final Exit bayviewGlen1stFloorBelowG11CommonAreaExitEast = new Exit("E",bayviewGlen1stFloorBelowG11CommonArea); bayviewGlenHallwayOutsideUpperGym.addExit(bayviewGlen1stFloorBelowG11CommonAreaExitEast);       
      final Exit bayviewGlenHallwayTheatreBackExitUp = new Exit("U",bayviewGlenHallwayTheatreBack); bayviewGlenHallwayOutsideUpperGym.addExit(bayviewGlenHallwayTheatreBackExitUp);

      final Exit bayviewGlenDeckExitSouthThree = new Exit("S",bayviewGlenDeck); bayviewGlenUpperGym.addExit(bayviewGlenDeckExitSouthThree);
      final Exit bayviewGlenHallwayOutsideUpperGymExitNorth = new Exit("N",bayviewGlenHallwayOutsideUpperGym); bayviewGlenUpperGym.addExit(bayviewGlenHallwayOutsideUpperGymExitNorth);

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


      // SHEPPARD YONGE EXITS

      final Exit sheppardYongeLine1ExitNorth = new Exit("N", sheppardYongeLine4StreetHallway);
      final Exit sheppardYongeLine1ExitSouth = new Exit("S", sheppardYongeLine1HallwayBeforeStreet);
      final Exit sheppardYongeLine4ExitExit = new Exit("S", sheppardYongeLine1);
      


      //ELLESMERE AREA EXITS
      
      
      
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
      final Exit stClairSubwayExitSouth = new Exit("S", stClairSubway); eglintonSubway.addExit(stClairSubwayExitSouth);

      //ST. CLAIR AREA EXITS
      final Exit eglintonSubwayExitNorth = new Exit("N", eglintonSubway); stClairSubway.addExit(eglintonSubwayExitNorth);
      final Exit itKeepsExitDown = new Exit("D", ___it_keeps); stClairSubway.addExit(itKeepsExitDown);
      final Exit stClairSubwayExitUp = new Exit("U", stClairSubway); ___it_keeps.addExit(stClairSubwayExitUp);
      final Exit pleaseLEAVEExitDown = new Exit("D", pleaseLEAVE); ___it_keeps.addExit(pleaseLEAVEExitDown);
      final Exit theEndExitDown = new Exit("D", theEnd); pleaseLEAVE.addExit(theEndExitDown);
      final Exit stClairStationExitUp = new Exit("U", stClairStation); stClairSubway.addExit(stClairStationExitUp);
      final Exit stClairSubwayExitDown = new Exit("D", stClairSubway);stClairStation.addExit(stClairSubwayExitDown);
      final Exit mcDonaldsExitWest = new Exit("W", mcDonalds); stClairStation.addExit(mcDonaldsExitWest);
      final Exit stClairStationExitEast = new Exit("E", stClairStation); mcDonalds.addExit(stClairStationExitEast);
      final Exit stClairAvenueExitNorth = new Exit("N", stClairAvenue); stClairStation.addExit(stClairAvenueExitNorth);
      final Exit stClairStationExitSouth = new Exit("S", stClairStation); stClairAvenue.addExit(stClairStationExitSouth);
      final Exit stClairAndYongeExitWest = new Exit("W", stClairAndYonge); stClairAvenue.addExit(stClairAndYongeExitWest);
      final Exit stClairAvenueExitEast = new Exit("E", stClairAvenue); stClairAndYonge.addExit(stClairAvenueExitEast);
      final Exit bucaExitWest = new Exit("W", bucaRestaurant); stClairAndYonge.addExit(bucaExitWest);
      final Exit stClairAndYongeExitEast = new Exit("E", stClairAndYonge); bucaRestaurant.addExit(stClairAndYongeExitEast);
      final Exit genericCorporateBuildingExitWest = new Exit("W", genericCorporateBuilding); bucaRestaurant.addExit(genericCorporateBuildingExitWest);
      final Exit bucaRestaurantExitEast = new Exit("E", bucaRestaurant); genericCorporateBuilding.addExit(bucaRestaurantExitEast);
      final Exit wetCementExitWest = new Exit("W", wetCement); genericCorporateBuilding.addExit(wetCementExitWest);
      final Exit genericCorporateBuildingExitEast = new Exit("E", genericCorporateBuilding); wetCement.addExit(genericCorporateBuildingExitEast);
      final Exit corporateLobbyExitNorth = new Exit("N", corporateLobby); genericCorporateBuilding.addExit(corporateLobbyExitNorth);
      final Exit genericCoporateBuildingExitSouth = new Exit("S", genericCorporateBuilding); corporateLobby.addExit(genericCoporateBuildingExitSouth);
      final Exit coporateCafeExitEast = new Exit("E", corporateCafe); corporateLobby.addExit(coporateCafeExitEast);
      final Exit corporateLobbyExitWest = new Exit("W", corporateLobby); corporateCafe.addExit(corporateLobbyExitWest);
      final Exit elevatorExitWest = new Exit("W", corporateElevator); corporateLobby.addExit(elevatorExitWest);
      final Exit corporateLobbyExitEast = new Exit("E", corporateLobby); corporateElevator.addExit(corporateLobbyExitEast);
      final Exit elevatorSecondFloorExitUp = new Exit("U", elevatorSecondFloor); corporateElevator.addExit(elevatorSecondFloorExitUp);
      final Exit corporateElevatorExitDown = new Exit("D", corporateElevator); elevatorSecondFloor.addExit(corporateElevatorExitDown);
      final Exit officeRoomExitEast = new Exit("E", officeRoom); elevatorSecondFloor.addExit(officeRoomExitEast);
      final Exit elevatorSecondFloorExitWest = new Exit("W", elevatorSecondFloor); officeRoom.addExit(elevatorSecondFloorExitWest);
      final Exit storageSpaceExitWest = new Exit("W", storageSpace); elevatorSecondFloor.addExit(storageSpaceExitWest);
      final Exit elevatorSecondFloorExitEast = new Exit("E", elevatorSecondFloor); storageSpace.addExit(elevatorSecondFloorExitEast);
      final Exit 



      //SUMMERHILL DEAD END EXIT
      final Exit summerhillSubwayExitSouth = new Exit("S", summerhillSubway); stClairSubway.addExit(summerhillSubwayExitSouth);
      final Exit stClairSubwayExitNorth = new Exit("N", stClairSubway); summerhillSubway.addExit(stClairSubwayExitNorth);
      

      // final Exit  = new Exit("", ); .addExit();


      
     //ROOM LEGEND
     //final Room (room name) = new Room("description", room name all lowercase); roomMap.put(room name.getRoomName(), room name);
     // final Room  =  new Room("", ""); roomMap.put( .getRoomName(), );

     // EXIT LEGEND
     // final exit (destinationRoomExit(direction current room exits to) = new Exit(direction, destinationRoom); currentRoom.addExit(destinationRoomExit(direction current room exit to))
     // final exit  = new Exit("", );  .addExit( );

     
      //Union
        //unionPlatform code
        final Room unionPlatform = new Room ("Placeholder Description for unionPlatform", "unionplatform"); roomMap.put(unionPlatform.getRoomName(), unionPlatform);
        //unionShopArea
        final Room unionShopArea = new Room ("Placeholder Description for unionShopArea", "unionshoparea"); roomMap.put(unionShopArea.getRoomName(), unionShopArea);
        //unionTimHortons
        final Room unionTimHortons = new Room ("Placeholder Description for unionTimHortons", "uniontimhortons"); roomMap.put(unionTimHortons.getRoomName(), 
        unionTimHortons);
        //Main area
        final Room unionMainArea = new Room ("Placeholder Description for unionMainArea", "unionmainarea"); roomMap.put(unionMainArea.getRoomName(), unionMainArea);
        // boolean[] hasEnteredMainLobby = {false};
        // unionMainArea.setRunnable(new Runnable(){
          
        //   ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
        //   @Override
        //   public void run() {
        //     if (!hasEnteredMainLobby[0]) {
        //       hasEnteredMainLobby[0] = true;
        //       try {
        //         renderer.showCutScene(1100, "\\bin\\zork\\data\\unioncyruscall.txt");
        //       } catch (Exception e) {
        //         handleException(e);
        //       }
        //     }
        //   }
          
        // });
        //union Corner
        final Room unionCorner = new Room ("Placeholder Description for unionCorner", "unioncorner"); roomMap.put(unionCorner.getRoomName(), unionCorner);
        //scams Market
        final Room unionScamsMarket = new Room ("Placeholder Description for unionScamsMarket", "unionscamsmarket"); roomMap.put(unionScamsMarket.getRoomName(), unionScamsMarket);
        Coupon freeprime = new Coupon(2, "One Free Prime", false);
        unionScamsMarket.setRunnable(new Runnable(){
         
          @Override
          public void run() {
            ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
            if(ar.contains(freeprime)){
              //run the bossfight
            }
           
          }
        });
        //hallway
        final Room unionHallway = new Room ("Placeholder Description for unionHallway", "unionhallway"); roomMap.put(unionHallway.getRoomName(), unionHallway);
        //Guide room
        final Room unionGuideRoom = new Room ("Placeholder Description for unionGuideRoom", "unionguideroom"); roomMap.put(unionGuideRoom.getRoomName(), unionGuideRoom);
        //washroom
        final Room unionWashroom = new Room ("Placeholder Description for unionWashroom", "unionwashroom"); roomMap.put(unionWashroom.getRoomName(), unionWashroom);
        //sinkRoom
        final Room unionSinkRoom = new Room ("Placeholder Description for unionSinkRoom", "unionsinkroom"); roomMap.put(unionSinkRoom.getRoomName(), unionSinkRoom);
        //Maintenance room
        final Room unionMaintenanceRoom = new Room ("Placeholder Description for unionMaintenanceRoom", "unionmaintenanceroom"); roomMap.put(unionMaintenanceRoom.getRoomName(), unionMaintenanceRoom);
        //faculty closet
        final Room unionFacultyCloset = new Room ("Placeholder Description for unionFacultyCloset", "unionfacultycloset", true, "is locked, prob need a key"); roomMap.put(unionFacultyCloset.getRoomName(), unionFacultyCloset);
        unionFacultyCloset.setRunnable(new Runnable(){
         
          @Override
          public void run() {
            Game.getGame().getPlayer().getInventory().addItem(freeprime);
            System.out.println("you found a coupon for free prime. This can proboably be used somewhere...");
           
          }
        });
        //faculty room
        final Room unionFacultyRoom = new Room ("Placeholder Description for unionFacultyRoom", "unionfacultyroom"); roomMap.put(unionFacultyRoom.getRoomName(), unionFacultyRoom);

        //exits
        final Exit  unionShopAreaExitUp = new Exit("U", unionShopArea); unionPlatform.addExit( unionShopAreaExitUp);
        final Exit unionMainAreaExitNorth = new Exit("N",unionMainArea); unionShopArea.addExit(unionMainAreaExitNorth);  
        final Exit unionMainAreaExitDown = new Exit("D",unionPlatform); unionShopArea.addExit(unionMainAreaExitDown);  
        final Exit unionTimHortonsExitEast = new Exit("E",unionTimHortons); unionShopArea.addExit(unionTimHortonsExitEast);
        final Exit unionShopAreaExitEast = new Exit("W",unionShopArea); unionTimHortons.addExit(unionShopAreaExitEast);
        final Exit unionHallwayExitNorth = new Exit("N",unionHallway); unionMainArea.addExit(unionHallwayExitNorth);
        final Exit unionScamsMarketExitWest = new Exit("W",unionScamsMarket); unionMainArea.addExit(unionScamsMarketExitWest);     
        final Exit unionCornerExitEast = new Exit("E",unionCorner); unionMainArea.addExit(unionCornerExitEast); 
        final Exit unionCornerExitSouth = new Exit("S",unionShopArea); unionMainArea.addExit(unionCornerExitSouth); 
        final Exit unionMaintenanceRoomExitUp = new Exit("U",unionMaintenanceRoom);unionMainArea.addExit(unionMaintenanceRoomExitUp);   
        final Exit unionMainAreaExitWest = new Exit("W",unionMainArea); unionCorner.addExit(unionMainAreaExitWest);  
        final Exit unionMainAreaExitEast = new Exit("E",unionMainArea); unionScamsMarket.addExit(unionMainAreaExitEast);
        final Exit unionMainAreaExitSouth = new Exit("S",unionMainArea); unionHallway.addExit(unionMainAreaExitSouth); 
        final Exit unionGuideRoomExitEast = new Exit("E",unionGuideRoom); unionHallway.addExit(unionGuideRoomExitEast);
        final Exit unionWashroomExitNorth = new Exit("N",unionWashroom); unionHallway.addExit(unionWashroomExitNorth); 
        final Exit unionHallwayExitWest = new Exit("W",unionHallway); unionGuideRoom.addExit(unionHallwayExitWest);
        final Exit unionHallwayExitSouth = new Exit("S",unionHallway); unionWashroom.addExit(unionHallwayExitSouth); 
        final Exit unionSinkRoomExitEast = new Exit("E",unionSinkRoom); unionWashroom.addExit(unionSinkRoomExitEast);
        final Exit unionWashroomExitWest = new Exit("W",unionWashroom); unionSinkRoom.addExit(unionWashroomExitWest);
        final Exit unionMainAreaExitDown2 = new Exit("D",unionMainArea); unionMaintenanceRoom.addExit(unionMainAreaExitDown2);
        final Exit unionFacultyClosetExitNorth = new Exit("N",unionFacultyCloset); unionMaintenanceRoom.addExit(unionFacultyClosetExitNorth);
        final Exit unionFacultyRoomExitEast = new Exit("E",unionFacultyRoom); unionMaintenanceRoom.addExit(unionFacultyRoomExitEast); 
        final Exit unionMaintenanceRoomExitSouth = new Exit("S",unionMaintenanceRoom); unionFacultyCloset.addExit(unionMaintenanceRoomExitSouth);
        final Exit unionMaintenanceRoomExitWest = new Exit("W",unionMaintenanceRoom); unionFacultyRoom.addExit(unionMaintenanceRoomExitWest);
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
    this.player.setCurrentRoom(roomMap.get("stclairandyonge"));
    this.player.getInventory().addItem(new Weapon(5, "Big Rock", false, 5, 
      new Effect("Bleeding", 2, 2, 5, 0)));
    try {
      player.getCurrentRoom().printAscii();
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (!finished) {
      Command command;
      try {
        command = parser.getCommand();
        String[] params = parser.getParams();
        processCommand(command,params);
      } catch (NullPointerException e) {
        if(isTesting) e.printStackTrace();
      } catch (CommandNotFoundException e) {
        if(isTesting) e.printStackTrace();
      }

    } 
  }

  /**
   * Print out the opening message for the player.
   */

  private void handleException(Exception e) {
    // TODO: FINISH
    // broken
  }

  private void printWelcome() throws InterruptedException {
    Scanner in = new Scanner(System.in);
    if(!isTesting){
    titleCard c = new titleCard();
    SoundHandler.playTitleSound();
    c.printTitle();
    } else{
      System.out.print("\nType Start to begin: ");
    }
    
     
    boolean hasStart = false;
    while(!hasStart) {
      String result = in.nextLine().toLowerCase();
      if(result.equals("start")) {
        hasStart = true;
        if(!isTesting) {
          SoundHandler.stopSound("mainmenu.wav");
          SoundHandler.playSound("cutscene.wav", true);
        try {
          renderer.showCutScene(1500, "\\bin\\zork\\data\\cutscene.txt", 75);
        } catch (Exception e) {
          handleException(e);
        }
      }
        SoundHandler.startRadio();
        SoundHandler.startAfterInterruption();
        boolean hasChosenName = false;
        System.out.print("Please enter your name: ");
        while(!hasChosenName) {
          result = in.nextLine().toLowerCase();
          if(result.length() > 16) {
            System.out.print("Please enter a name between 1-16 Characters");
          } else if (result.equalsIgnoreCase("cameron")) { 
            System.out.print("Sorry that name is already taken, Please enter another name: ");
          } else if (result.equalsIgnoreCase("cagasuge")) { 
            System.out.println("Im not mad... Im just dissapointed     ");
          } else if (result.equalsIgnoreCase("kevin")){
            System.out.println("Hi Mr. Deslauriers, we hope you enjoy 'PRIMEQUEST'!");
          } else if (result.equalsIgnoreCase("snake")){
            System.out.println("Snake, this is major Zero. You have been tasked with infiltrating the enemy territory, and must collect every single flavour of what these people call 'PRIME'. Zero out.");
          } else if (result.equalsIgnoreCase("lucca")){
            System.out.println("Sorry that name is already taken, Please enter another name: ");
          } else if (result.equalsIgnoreCase("ethan")){
            System.out.println("Sorry that name is already taken, Please enter another name: ");
          } else if (result.equalsIgnoreCase("marco")){
            System.out.println("Sorry that name is already taken, Please enter another name: ");
          } else if (result.equalsIgnoreCase("jch")){
            System.out.println("your funniest person alive award is being sent to your location");
            hasChosenName = true;
            Game.getGame().getPlayer().setName(result);
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
   * @throws InterruptedException
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void processCommand(Command command, String[] args) throws InterruptedException {
    System.out.println(command.runCommand(args));
  }

  // implementations of user commands:

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */
}