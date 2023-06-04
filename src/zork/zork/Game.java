package zork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;

import datatypes.CommandNotFoundException;
import datatypes.Location;
import zork.Constants.PlayerConstants;
import zork.Utils.SoundHandler;
import zork.entites.Enemy;
import zork.entites.Player;
import zork.items.TestItem;
import zork.items.Prime;
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
      final Room rolexOffice = new Room("You have entered the office of the Rolex Canad building. The boss must be away right now", "rolexoffice"); roomMap.put(rolexOffice.getRoomName(), rolexOffice);
      final Room rolexHallway = new Room("in the green carpeted rolex halway, you can hear people working away in the offices adjacent. You should get out before you are found here.", "rolexhallway"); roomMap.put(rolexHallway.getRoomName(), rolexHallway);
      final Room rolexStairwell = new Room("A sign at the top of the stairs says 'to offices', a sign at the bottom says 'emergency exit'.", "rolexstairwell");
      final Room sketchyAlley = new Room("You stand outside the emergency exit of the Rolex Canada Building. This alley looks like it leads back to St. Clair Avenue West.", "sketchyalley"); roomMap.put(sketchyAlley.getRoomName(), sketchyAlley);
      final Room stClairAboveStreetcar = new Room("South of a sketchy alley, you stand on a sidewalk above a streetcar stop.", "stclairabovestreetcar"); roomMap.put(stClairAboveStreetcar.getRoomName(), stClairAboveStreetcar);
      final Room wetCement2 = new Room("You just stepped into wet cement! You'd better get out of there before you get stuck.", "wetcement!2"); roomMap.put(wetCement2.getRoomName(), wetCement2);
      final Room streetcar = new Room ("You have entered the streetcar. You look young enough to be under 13.", "streetcar"); roomMap.put(streetcar.getRoomName(), streetcar);

      final Room mcDonalds = new Room("You walk into the cramped subway station McDonald's", "mcdonalds"); roomMap.put(mcDonalds.getRoomName(), mcDonalds);
            mcDonalds.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Hi Welcome to Tim Hortons, Would you like to buy anything? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() <=1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Big Mac - 4$", 7);
                            text.slowTextSpeed("> McCafe Coffee - 2$", 7);
                            text.slowTextSpeed("> Happy Meal - 7$", 7);
                            String b = in.nextLine();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.equalsIgnoreCase("big mac")){
                                if((pMoney - 4)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=4);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Big Mac", false, 
                                    new Effect("Health up", 0, 0, 4, 20), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                }
                            }else if(b.equalsIgnoreCase("McCafe Coffee")){
                                if((pMoney - 2)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=2);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "McCafe Coffee", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                }
                            }else if(b.equalsIgnoreCase("Happy Meal")){
                                if((pMoney - 7)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=7);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Happy Meal", false, 
                                    new Effect("Super Delicous", 0, 0, -8, 30), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }
                          
                          if(finishedOrder){
                            while(true){
                              text.slowTextSpeed("Would you like anything else? y/n", 7);
                              String c = in.nextLine();
                              if(c.equalsIgnoreCase("y")){
                                finishedOrder = false;
                                break;
                              }else if(c.equalsIgnoreCase("n")){
                                finishedOrder = true;
                                text.slowTextSpeed("Thank you, come again!", 7);
                                break;
                              }
                            }
                          }
                        }
                    }else{
                      text.slowTextSpeed("ok come back soon", 7);
                    }
                    
                  } catch (Exception e) {
                    // TODO: handle exception
                  }
      

      //ST. CLAIR WEST AREA ROOMS
      final Room stClairWestStation = new Room ("As you enter the station, a distinct smell of garbage envelops your senses.", "stclairweststation"); roomMap.put(stClairWestStation.getRoomName(), stClairWestStation);
      final Room onTheRun = new Room("Stepping into the dank and small convenience store, a wall of candy and slim jims greet your eyes. *IMPLEMENT SHOPKEEPER FOR PRIME", "ontherun"); roomMap.put(onTheRun.getRoomName(), onTheRun);
      final Room stClairWestSubway = new Room ("'Probably one of the nicer subway stations', you think to yourself. ", "stclairwestsubway"); roomMap.put(stClairWestSubway.getRoomName(), stClairWestSubway);



      //SUMMER HILL DEAD END ROOM

      final Room summerhillSubway = new Room("Please stay on the train, police investigation underway", "summerhillSubway"); roomMap.put(summerhillSubway.getRoomName(), summerhillSubway);





      //ELLESMERE AREA ROOMS

      final Room ellesmereStationUnderground = new Room("Tap presto to enter subway.", "ellesmerestationunderground", "Ellesmere Station Entrance");
      final Room ellesmereSubwayNorthbound = new Room("Northbound to McCowan","ellesmeresubwaynorthbound","Ellesmere Station Track Northbound");
      final Room ellesmereSubwaySouthbound = new Room("Southbound to Kennedy","ellesmeresubwaysouthbound","Ellesmere Station Track Southbound");
      

       //BAYVIEW GLEN INDEPENDENT SCHOOL ROOMS
 
       final boolean[] hasEnteredLobby = new boolean[]{false};
       final boolean[] hasChosenInstrument = new boolean[]{false}; //These are all booleans to make sure everything only runs once
       final boolean[] hasGottenFirstKey = new boolean[]{false};

       final Room bayviewGlenLobby = new Room ("Ah the lobby, what a refreshing place, You look around and take a seccond to breathe it all in", "bayviewglenlobby"); roomMap.put(bayviewGlenLobby.getRoomName(), bayviewGlenLobby);  // north exit outside for later looking south when walking in
       
       final Room bayviewGlenOutsideLobby = new Room ("Parking lot outside of bayviewGlen. You see one car parked there, thats odd, nobody should be at the school right now...", "bayviewglenoutsidelobby"); roomMap.put(bayviewGlenOutsideLobby.getRoomName(), bayviewGlenOutsideLobby);
       final Room bayviewGlenHallwayCafeteria = new Room ("you enter a Hallway, to your left is the cafeteria and too your right is the learning commons", "bayviewglenhallwaycafeteria"); roomMap.put(bayviewGlenHallwayCafeteria.getRoomName(), bayviewGlenHallwayCafeteria); // to the east from lobby
       final Room bayviewGlenHallwayPrepGym = new Room("you enter a Hallway, gyms to your left and right, oo which one to choose", "bayviewglenhallwayprepgym", false, "Since you know the gym is probably locked maybe the hallway by the gym isn't. you were dead wrong"); roomMap.put(bayviewGlenHallwayPrepGym.getRoomName(), bayviewGlenHallwayPrepGym);
       
       final Room bayviewGlenHallwayTheatreFront = new Room("Hallway next to the theatre, you gase at the theatre through the doorway, the seats, the curtain. Then you realize the door is closed and none of that makes any sense.", "bayviewglenhallwaytheatrefront", false, "the door is locked, you try to barge in but its locked so that doesnt make any sense."); roomMap.put(bayviewGlenHallwayTheatreFront.getRoomName(), bayviewGlenHallwayTheatreFront);
       final Room bayviewGlenOutsideHallwayTheatreFront = new Room ("Parking lot outside of bayview glen, There is a door leading into the school to your south", "bayviewglenoutsidehallwaytheatrefront"); roomMap.put(bayviewGlenOutsideHallwayTheatreFront.getRoomName(), bayviewGlenOutsideHallwayTheatreFront);
       final Room bayviewGlenCafeteriaFoodArea = new Room("You enter the cafeteria, the smell of the food immediatly hits your nose. But nothings been cooked here for a month. huh", "bayviewglencafeteriafoodarea"); roomMap.put(bayviewGlenCafeteriaFoodArea.getRoomName(), bayviewGlenCafeteriaFoodArea);
       bayviewGlenCafeteriaFoodArea.addItemGround(new Item(0, null, true, new Effect("Healing", 0, 0, 0, 30), false));
       final Room bayviewGlenOutsideCafeteria = new Room ("You are outside of the cafeteria, the giant glass wall is directly south of you.", "bayviewglenoutsidecafeteria"); roomMap.put(bayviewGlenOutsideCafeteria.getRoomName(), bayviewGlenOutsideCafeteria);
       final Room bayviewGlenKitchen = new Room("You enter the kitchen of bayview glen. You wonder if your supposed to be in here", "bayviewglenkitchen"); roomMap.put(bayviewGlenKitchen.getRoomName(), bayviewGlenKitchen);
       bayviewGlenKitchen.addItemGround(new Weapon(4, "Knife", finished, 12, new Effect("bleeding", 1, 2, 2, 0)));
       Inventory mutatedFood = new Inventory(6);
       mutatedFood.addItem(new Weapon(3, "Acid Shot", false, 10, null));
       mutatedFood.addItem(new Weapon(3, "Rotted Chunk", false, 3, new Effect("Poison", 3, 6, 2, 0)));
       final Enemy mutatedFoodEnemy = new Enemy(null, bayviewGlenKitchen, 50, mutatedFood , 0, "MutatedFood", 75);
       bayviewGlenKitchen.enemies.add(mutatedFoodEnemy);
       bayviewGlenKitchen.setRunnable(() -> {
          if(bayviewGlenKitchen.enemies.contains(mutatedFoodEnemy)) {
            Fight f = new Fight(mutatedFoodEnemy);
            boolean won = f.fight();
            if(won) {
              bayviewGlenKitchen.enemies.remove(mutatedFoodEnemy);
            }
          }
       });

       final Room bayviewGlenCafeteriaDiningArea = new Room("You enter the Cafeteria Dining Area, so many seats you can't pick one. so you don't.", "bayviewglencafeteriadiningarea", false, "The cafeteria is closed, you hear your stomach grumbling but then you realize its cafeteria food and your urges subside"); roomMap.put(bayviewGlenCafeteriaDiningArea.getRoomName(), bayviewGlenCafeteriaDiningArea);
       final Room bayviewGlenPrepGym = new Room("You enter the gym, all the baskets are down. to your north you see an exit outside, which definitly will not lock behind you.", "bayviewglenprepgym", false, "The gym is closed and you are unable to get in"); roomMap.put(bayviewGlenPrepGym.getRoomName(), bayviewGlenPrepGym);
       final Room bayviewGlenWeightRoom = new Room("You enter the weight room. You are surrounded by heavy weights and machines.", "bayviewglenweightroom"); roomMap.put(bayviewGlenWeightRoom.getRoomName(), bayviewGlenWeightRoom);
       final Room bayviewGlenOutsidePrepGymNorth = new Room("You are outside the prep gym. the gym is directly south of you", "bayviewglenoutsideprepgymnorth"); roomMap.put(bayviewGlenOutsidePrepGymNorth.getRoomName(), bayviewGlenOutsidePrepGymNorth);
       final Room bayviewGlenLearningCommons = new Room("You enter the learning commons. but theres no books! oh no", "bayviewglenlearningcommons"); roomMap.put(bayviewGlenLearningCommons.getRoomName(), bayviewGlenLearningCommons);
       final Room bayviewGlenLearningCommonsSubArea = new Room ("...", "bayviewglenlearningcommonssubarea"); roomMap.put(bayviewGlenLearningCommonsSubArea.getRoomName(), bayviewGlenLearningCommonsSubArea);
       final Room bayviewGlenTheatre = new Room("You enter the theatre, the seats are retracted you see a door to the south", "bayviewglentheatre"); roomMap.put(bayviewGlenTheatre.getRoomName(), bayviewGlenTheatre);
       final Room bayviewGlenHallwayTheatreBack = new Room ("You walk into a hallway behind the theatre, you see the grade 11 common area to the east", "bayviewglenhallwaytheatreback"); roomMap.put(bayviewGlenHallwayTheatreBack.getRoomName(), bayviewGlenHallwayTheatreBack);
       final Room bayviewGlenHallway2ndFloorToUpperSchool = new Room("Hallway towards the grade 11 common area. you see closed doors ahead to the south.", "bayviewglenhallway2ndfloortoupperschool"); roomMap.put(bayviewGlenHallway2ndFloorToUpperSchool.getRoomName(), bayviewGlenHallway2ndFloorToUpperSchool);
       
       final Room bayviewGlenG11CommonArea = new Room("Welcome to objectivly the worst common area", "bayviewgleng11commonarea", false, "You go up the stairs and try to open the door, but as usual its locked an nobody is there to let you in."); roomMap.put(bayviewGlenG11CommonArea.getRoomName(), bayviewGlenG11CommonArea);
       final Room bayviewGlenGradHallway = new Room("You walk down the hallway filled with names of all the people who did better than you. a single tear runs down your cheek", "bayviewglengradhallway"); roomMap.put(bayviewGlenGradHallway.getRoomName(), bayviewGlenGradHallway);
       final Room bayviewGlenUpperMusicHallway = new Room ("You walk a narrow hallway past the music room", "bayviewglenuppermusichallway"); roomMap.put(bayviewGlenUpperMusicHallway.getRoomName(), bayviewGlenUpperMusicHallway);
       final Room bayviewGlenDramaRoom = new Room ("You enter the drama room, you see in large bold text #1 27. You don't know what it means but it sounds important so you write it down", "bayviewglendramaroom"); roomMap.put(bayviewGlenDramaRoom.getRoomName(), bayviewGlenDramaRoom);
       final Room bayviewGlenDeck = new Room ("You walk into the deck, you get a compelte view of the entire building from here.", "bayviewglendeck"); roomMap.put(bayviewGlenDeck.getRoomName(), bayviewGlenDeck);
       final Room bayviewGlenYorkMills = new Room ("You're on york mills street, don mills is to the east.", "bayviewglenyorkmills"); roomMap.put(bayviewGlenYorkMills.getRoomName(), bayviewGlenYorkMills);
       final Room bayviewGlen3rdFloorLobby = new Room ("You go to the third floor above the lobby, wow there's so much to do here!", "bayviewglen3rdfloorlobby"); roomMap.put(bayviewGlen3rdFloorLobby.getRoomName(), bayviewGlen3rdFloorLobby);
       final Room bayviewGlen3rdFloorWestPrepStairwayHallway = new Room ("Hallway, hallway and more hallway.", "bayviewglen3rdfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorWestPrepStairwayHallway);
       final Room bayviewGlen3rdFloorEastPrepStairwayHallway = new Room ("You are in a hallway, you look down at the gym from above.", "bayviewglen3rdflooreastprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorEastPrepStairwayHallway);
       final Room bayviewGlenG9CommonArea = new Room ("Here lies meatball, you monster", "bayviewgleng9commonarea"); roomMap.put(bayviewGlenG9CommonArea.getRoomName(), bayviewGlenG9CommonArea);
       Inventory meatBall = new Inventory(5);
       meatBall.addItem(new Weapon(3, "Fork", false, 10, null));
       final Enemy cyrus_meatball = new Enemy(null, bayviewGlenG9CommonArea, Constants.PlayerConstants.DEFAULT_HEALTH, meatBall , 0, "Meatball", 75);
       bayviewGlenG9CommonArea.enemies.add(cyrus_meatball);
       bayviewGlenG9CommonArea.setRunnable(new Runnable() {
 
         @Override
         public void run() {           
            if(Game.getGame().getPlayer().getCurrentRoom().enemies.contains(cyrus_meatball)) {
              System.out.println("Meatball is Blocking the Way");
              Fight f = new Fight(cyrus_meatball);
              boolean won = f.fight();
              if(won) {
              Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(cyrus_meatball);
             }
            }    
          }
        });
       final Room bayviewGlenOutsideStaircase = new Room ("You see a long staircase across the east side of the school", "bayviewglenoutsidestaircase"); roomMap.put(bayviewGlenOutsideStaircase.getRoomName(), bayviewGlenOutsideStaircase);
       final Room bayviewGlenOutsideWest = new Room ("You see the entire west side of the school", "bayviewglenoutsidewest"); roomMap.put(bayviewGlenOutsideWest.getRoomName(), bayviewGlenOutsideWest);
       final Room bayviewGlenMusicRoom = new Room ("The music room, you remember the fight you once had here.", "bayviewglenmusicroom"); roomMap.put(bayviewGlenMusicRoom.getRoomName(), bayviewGlenMusicRoom);
      Inventory musicMan = new Inventory(10);
      musicMan.addItem(new Weapon(3, "Depressing Song", false, 2, new Effect("Depression", 3, 6, 2, 0)));
      musicMan.addItem(new Weapon(3, "Baton", false, 15, null));
      final Enemy musicManEnemy = new Enemy(null, bayviewGlenMusicRoom, 75, musicMan , 0, "Music Man", 83);
      bayviewGlenMusicRoom.addEnemies(musicManEnemy);
       bayviewGlenMusicRoom.setRunnable(() -> {
         Scanner in = new Scanner(System.in);
         Graphics text = new Graphics();
         try {
         if (hasChosenInstrument[0]) {
          return;
         }

        while(!hasChosenInstrument[0]) {
          if (Game.getGame().getPlayer().getCurrentRoom().getEnemies().get(0).equals(musicManEnemy)) {
            Fight f = new Fight(musicManEnemy);
            boolean won = f.fight();
            boolean pickedWeapon = false;
            if(won) {
              Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(musicManEnemy);
            }
          }
            
            text.slowTextSpeed("As you enter the music room you see 5 instruments in 5 different cases\n Which one will you take:\n", 15);
            text.slowTextSpeed(" > Clarinet \n > Trumpet \n > Trombone \n > Oboe \n > Saxophone \n > None \n", 120);
            String ans = in.nextLine();
            if (ans.equalsIgnoreCase("clarinet")) {
              Game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Clarinet", true, 20, null));
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Clarinet", 15);
              hasChosenInstrument[0] = true; Thread.sleep(1000);

            } else if (ans.equalsIgnoreCase("trumpet")) {
              Game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Trumpet", true, 25, null));
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Trumpet", 15);
              hasChosenInstrument[0] = true; Thread.sleep(1000);
              
            } else if (ans.equalsIgnoreCase("trombone")) {
              Game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Trombone", true, 25, null));
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Trombone", 15);
              hasChosenInstrument[0] = true; Thread.sleep(1000);
              
            } else if (ans.equalsIgnoreCase("saxophone")) {
              Game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Saxophone", true, 25, new Effect("Aggitated", 2, 0, -20, 0)));
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Saxophone", 15);
              hasChosenInstrument[0] = true; Thread.sleep(1000);
              
            } else if (ans.equalsIgnoreCase("oboe")) {
              Game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Oboe", true, 15, new Effect("Ruptured Eardrum", 2, 5, 5, 0)));
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Flute", 15);
              hasChosenInstrument[0] = true; Thread.sleep(1000);
              
            } if (ans.equalsIgnoreCase("none")){ 
              hasChosenInstrument[0] = true;
              text.slowTextSpeed("You decide to leave such a huge choice for another day", 15); Thread.sleep(1000);
              return;
            } else if (hasChosenInstrument[0] == true) {
              text.slowTextSpeed("As you select your instrument, the other instruments dissapear along with musicMan", 15);
            } else {
              text.slowTextSpeed("Please Select an instrument or type None to leave: ", 15);
            }
            
           
        }
        hasChosenInstrument[0] = false;
        } catch (InterruptedException e) {
        e.printStackTrace();
       }
       });
       
       
       
       final Room bayviewGlen2ndFloorUpperHallway = new Room ("You enter a Winding hallway filled with locked classrooms", "bayviewglen2ndfloorupperhallway"); roomMap.put(bayviewGlen2ndFloorUpperHallway.getRoomName(), bayviewGlen2ndFloorUpperHallway);
       final Room bayviewGlenHallway3rdFloorByElevator = new Room ("You are in a hallway next to the elevator, you remember you arent supposed to use the elevator... right?", "bayviewglenhallway3rdfloorbyelevator"); roomMap.put(bayviewGlenHallway3rdFloorByElevator.getRoomName(), bayviewGlenHallway3rdFloorByElevator);
       final Room bayviewGlenMathWing = new Room ("The home of cyrusses locker, what a sight to behold", "bayviewglenmathwing"); roomMap.put(bayviewGlenMathWing.getRoomName(), bayviewGlenMathWing);
       final Room bayviewGlen3rdFloorUpperSchoolHallway = new Room ("As you walk passed Ms.Fenili's office a chill runs down your spine", "bayviewglen3rdfloorupperschoolhallway"); roomMap.put(bayviewGlen3rdFloorUpperSchoolHallway.getRoomName(), bayviewGlen3rdFloorUpperSchoolHallway);
       final Room bayviewGlenG10CommonArea = new Room ("You enter a nice common area, filled with couches and chairs", "bayviewgleng10commonarea"); roomMap.put(bayviewGlenG10CommonArea.getRoomName(), bayviewGlenG10CommonArea);
       final Room bayviewGlen3rdFloorBridge = new Room ("You enter a hallway that connects the upper and prep school", "bayviewglen3rdfloorbridge"); roomMap.put(bayviewGlen3rdFloorBridge.getRoomName(), bayviewGlen3rdFloorBridge);
       final Room bayviewGlen4thFloorEastPrepStairwayHallway = new Room ("What is even up here you may ask, the answer, is in your imagination", "bayviewglen4thflooreastprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorEastPrepStairwayHallway);
       final Room bayviewGlen4thFloorWestPrepStairwayHallway = new Room ("And more hallway, when does it end!", "bayviewglen4thfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorWestPrepStairwayHallway);
       final Room bayviewGlen4thFloorLobby = new Room ("You enter the top floor, right below you is right above the lobby. neat!", "bayviewglen4thfloorlobby"); roomMap.put(bayviewGlen4thFloorLobby.getRoomName(), bayviewGlen4thFloorLobby);
       final Room bayviewGlenPrepStaffRoom = new Room ("Nice little staff room overlooking the Learning Commons", "bayviewglenprepstaffroom"); roomMap.put(bayviewGlenPrepStaffRoom.getRoomName(), bayviewGlenPrepStaffRoom);
       Inventory questionMark = new Inventory(10);
       questionMark.addItem(new Weapon(3, "White Noise", false, 10, new Effect("Confusion", 20, 2, 0, 0)));
       questionMark.addItem(new Weapon(3, "the FIST", false, 20, null));
       final Enemy questionMarkEnemy = new Enemy(null, bayviewGlenPrepStaffRoom, 175, questionMark , 0, "?????", 129);
       bayviewGlenPrepStaffRoom.addEnemies(questionMarkEnemy);
       bayviewGlenPrepStaffRoom.setRunnable(() -> {
          Graphics text = new Graphics();
          boolean hasChosenKey = false;
          try {
          if (!hasGottenFirstKey[0]) {
            while(!hasChosenKey) {
              Scanner in = new Scanner(System.in);
              text.slowTextSpeed("You see a key dangling on a hook\n" + "Do you grab it Y/N? \n" , 20);
              String ans = in.nextLine();
              if (ans.equalsIgnoreCase("y")) {
                hasChosenKey = true;
                text.slowTextSpeed("You take the key from the wall, but as you exit you feel a sharp pain - \n ??? - I'm going to need that key back... \n You - Who are you? \n ??? - Thats none of your concern, now hand over the key \n You - Not without a fight!" , 20);
                Fight f = new Fight(questionMarkEnemy);
                boolean won = f.fight();
                if (won) {
                  text.slowTextSpeed(" ??? - Ok you beat me \n You - WHO ARE YOU??? \n ??? - I- *Cough cough* am.. \n You - WHO? \n ??? - dead \n", 20);
                  Game.getGame().getPlayer().getInventory().addItem(new Key("G11CommonAreaKey", "golden key", 1));
                  hasGottenFirstKey[0] = true;
                }
              } else if (ans.equalsIgnoreCase("n")) {
                return;
              } else {
                text.slowTextSpeed("Please type Y or N \n" , 20);
              }
            }
          }
          } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        });
       final Room bayviewGlen1stFloorBelowG11CommonArea = new Room ("Placeholder Description for bayviewGlen1stFloorBelowG11CommonArea", "bayviewglen1stfloorbelowg11commonarea"); roomMap.put(bayviewGlen1stFloorBelowG11CommonArea.getRoomName(), bayviewGlen1stFloorBelowG11CommonArea);
       final Room bayviewGlenG12CommonArea = new Room ("Placeholder Description for bayviewGlenG12CommonArea", "bayviewgleng12commonarea"); roomMap.put(bayviewGlenG12CommonArea.getRoomName(), bayviewGlenG12CommonArea);
       final Room bayviewGlenArtRoom = new Room ("Placeholder Description for bayviewGlenArtRoom", "bayviewglenartroom"); roomMap.put(bayviewGlenArtRoom.getRoomName(), bayviewGlenArtRoom);
       final Room bayviewGlenHallwayOutsideUpperGym = new Room ("Placeholder Description for bayviewGlenHallwayOutsideUpperGym", "bayviewglenhallwayoutsideuppergym"); roomMap.put(bayviewGlenHallwayOutsideUpperGym.getRoomName(), bayviewGlenHallwayOutsideUpperGym);
       final Room bayviewGlenUpperGym = new Room ("Placeholder Description for bayviewGlenUpperGym", "bayviewglenuppergym", false, "The gym is closed, and you can't get in."); roomMap.put(bayviewGlenUpperGym.getRoomName(), bayviewGlenUpperGym);
 
       bayviewGlenLobby.setRunnable(new Runnable(){
 
         @Override
         public void run() {
           if (!hasEnteredLobby[0]) {
             hasEnteredLobby[0] = true;
             try {
               renderer.showCutScene(1500, "\\bin\\zork\\data\\bayviewglencyruscall.txt", 15);
             } catch (Exception e) {
               handleException(e);
             }
           }
         }
       });
 
 
       bayviewGlenMathWing.setRunnable(() -> {
         Scanner in = new Scanner(System.in);
         Graphics text = new Graphics();
         try {
         text.slowTextSpeed("You see Cyrus's Locker, would you like to open it Y/N", 15);
           String ans = in.nextLine();
           if(ans.equalsIgnoreCase("y")) {
             text.slowTextSpeed("What is the code? format: XX, XX, XX", 15);
             ans = in.nextLine();
             if(ans.equalsIgnoreCase("27, 05, 17") || ans.equalsIgnoreCase(" 27, 05, 17") || ans.equalsIgnoreCase("27 05 17")) {
               Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "STRAWBERRY WATERMELON PRIME", isTesting, null, finished, ans));
               text.slowTextSpeed("You slowly twist the locker 27, and then 05 and finally 17. \n the lock opens. as you open the locker you see at the top shelf a shiny pink bottle.\n STRAWBERRY WATTERMELON tm. Prime", 15);
               Thread.sleep(1000);
             }
           }
         }catch (Exception e) {
           System.out.println("SlowTextSpeedNoWork");
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
       final Exit bayviewGlenTheatreExitSouth = new Exit("S",bayviewGlenTheatre, "Theatre's closed. sorry bucko", true); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenTheatreExitSouth);
       final Exit bayviewGlenLobbyExitEast = new Exit("E",bayviewGlenLobby); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenLobbyExitEast);
       final Exit bayviewGlenOutsideWestExitWest = new Exit("W",bayviewGlenOutsideWest); bayviewGlenHallwayTheatreFront.addExit(bayviewGlenOutsideWestExitWest);
       final Exit bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp = new Exit("U",bayviewGlen3rdFloorWestPrepStairwayHallway); bayviewGlenHallwayTheatreFront.addExit(bayviewGlen3rdFloorWestPrepStairwayHallwayExitUp);
       
       final Exit bayviewGlenHallwayTheatreFrontExitSouth = new Exit("S",bayviewGlenHallwayTheatreFront, true); bayviewGlenOutsideHallwayTheatreFront.addExit(bayviewGlenHallwayTheatreFrontExitSouth);
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
       final Exit bayviewGlenCafeteriaDiningAreaExitSouth = new Exit("S",bayviewGlenCafeteriaDiningArea, true); bayviewGlenOutsideCafeteria.addExit(bayviewGlenCafeteriaDiningAreaExitSouth);
       
       final Exit bayviewGlenOutsidePrepGymExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenPrepGym.addExit(bayviewGlenOutsidePrepGymExitNorth);
       final Exit bayviewGlenHallwayPrepGymExitSouth = new Exit("S",bayviewGlenHallwayPrepGym); bayviewGlenPrepGym.addExit(bayviewGlenHallwayPrepGymExitSouth);
 
       final Exit bayviewGlenHallwayPrepGymExitNorth = new Exit("N",bayviewGlenHallwayPrepGym); bayviewGlenWeightRoom.addExit(bayviewGlenHallwayPrepGymExitNorth);
 
       final Exit bayviewGlenOutsideCafeteriaExitWest = new Exit("W",bayviewGlenOutsideCafeteria); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsideCafeteriaExitWest);
       final Exit bayviewGlenOutsideStaircaseExitEastTwo = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenOutsideStaircaseExitEastTwo);
       final Exit bayviewGlenPrepGymExitSouth = new Exit("S",bayviewGlenPrepGym, true); bayviewGlenOutsidePrepGymNorth.addExit(bayviewGlenPrepGymExitSouth);
 
       final Exit bayviewGlenOutsidePrepGymNorthExitNorth = new Exit("N",bayviewGlenOutsidePrepGymNorth); bayviewGlenOutsideStaircase.addExit(bayviewGlenOutsidePrepGymNorthExitNorth);
       final Exit bayviewGlenHallwayPrepGymExitWest = new Exit("W",bayviewGlenHallwayPrepGym, true); bayviewGlenOutsideStaircase.addExit(bayviewGlenHallwayPrepGymExitWest); 
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
       final Exit bayviewGlenG11CommonAreaExitSouth = new Exit("S",bayviewGlenG11CommonArea, "The doors are shut, you try to open them but they are locked, you again try to open them but they are locked, you again try to open them but they are locked, you again try to open them but they are locked, yo", true); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenG11CommonAreaExitSouth);
       final Exit bayviewGlenGradHallwayExitEast = new Exit("E",bayviewGlenGradHallway, "There are barrels upon barrels of coal in the way, you can't get through all of them" , true); bayviewGlenHallway2ndFloorToUpperSchool.addExit(bayviewGlenGradHallwayExitEast);
 
       final Exit bayviewGlenYorkMillsExitSouth = new Exit("S",bayviewGlenYorkMills); bayviewGlenDeck.addExit(bayviewGlenYorkMillsExitSouth);
       final Exit bayviewGlenG11CommonAreaExitUp = new Exit("U",bayviewGlenG11CommonArea, true); bayviewGlenDeck.addExit(bayviewGlenG11CommonAreaExitUp);
       final Exit bayviewGlenOutsideStaircaseExitEastThree = new Exit("E",bayviewGlenOutsideStaircase); bayviewGlenDeck.addExit(bayviewGlenOutsideStaircaseExitEastThree); 
       final Exit bayviewGlenOutsideWestExitWestThree = new Exit("W",bayviewGlenOutsideWest); bayviewGlenDeck.addExit(bayviewGlenOutsideWestExitWestThree);
       final Exit bayviewGlenUpperGymExitNorth = new Exit("N",bayviewGlenUpperGym,true); bayviewGlenDeck.addExit(bayviewGlenUpperGymExitNorth);
 
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
       final Exit bayviewGlenHallwayTheatreFrontExitEast = new Exit("E",bayviewGlenHallwayTheatreFront, true); bayviewGlenOutsideWest.addExit(bayviewGlenHallwayTheatreFrontExitEast);
       
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
 
       final Exit bayviewGlenG9CommonAreaExitSouth = new Exit("S",bayviewGlenG9CommonArea, "Sorry kids, the bridge is closed, you read a perculiar sign saying meatball is waiting... Prolly nothing", true); bayviewGlen3rdFloorBridge.addExit(bayviewGlenG9CommonAreaExitSouth);
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

       bayviewGlenHallway2ndFloorToUpperSchool.setRunnable(() -> {
        Graphics text = new Graphics();
        for (Item I : Game.getGame().getPlayer().getInventory().getItems()) {
          if (I.getName().equals("golden key")) {
            if (I.getKeyId().equalsIgnoreCase("G11CommonAreaKey")) {
              try {
                text.slowTextSpeed("You use your key and the door to your south clicks open", 10);
                Thread.sleep(1000);
                bayviewGlenG11CommonAreaExitSouth.setIsExitLocked(false);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          } 
        }
       });
       
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
      final Exit catwalkExitWest = new Exit("W", catwalk); storageSpace.addExit(catwalkExitWest);
      final Exit storageSpaceExitEast = new Exit("E", storageSpace); catwalk.addExit(storageSpaceExitEast);  
      final Exit rolexOfficeExitWest = new Exit ("W", rolexOffice); catwalk.addExit(rolexOfficeExitWest);
      final Exit catwalkExitEast = new Exit("E", catwalk); rolexOffice.addExit(catwalkExitEast);
      final Exit rolexHallwayExitWest = new Exit("W", rolexHallway); rolexOffice.addExit(rolexHallwayExitWest);
      final Exit rolexOfficeExitEast = new Exit ("E", rolexOffice); rolexHallway.addExit(rolexOfficeExitEast);
      final Exit rolexStairwellExitWest = new Exit("W", rolexStairwell); rolexHallway.addExit(rolexStairwellExitWest);
      final Exit rolexHallwaysExitEast = new Exit ("E", rolexHallway); rolexStairwell.addExit(rolexHallwaysExitEast);
      final Exit sketchyAlleyExitWest = new Exit ("W", sketchyAlley); rolexStairwell.addExit(sketchyAlleyExitWest); 
      final Exit rolexStairwellExitEast = new Exit("E", rolexStairwell); sketchyAlley.addExit(rolexStairwellExitEast);
      final Exit stClairAboveStreetCarExitSouth = new Exit("S", stClairAboveStreetcar); sketchyAlley.addExit(stClairAboveStreetCarExitSouth);
      final Exit sketchyAlleyExitNorth = new Exit ("N", sketchyAlley); stClairAboveStreetcar.addExit(sketchyAlleyExitNorth);
      final Exit wetCementExitEast = new Exit ("E", wetCement2); stClairAboveStreetcar.addExit(wetCementExitEast);
      final Exit stClairAboveStreetcarExitWest = new Exit ("W", stClairAboveStreetcar); wetCement2.addExit(stClairAboveStreetcarExitWest);
      final Exit streetcarExitSouth = new Exit ("S", streetcar); stClairAboveStreetcar.addExit(streetcarExitSouth);
      final Exit stClairAboveStreetCarExitNorth = new Exit ("N", stClairAboveStreetcar); streetcar.addExit(stClairAboveStreetCarExitNorth);

      //St. Clair West Station Exits
      final Exit stClairWestStationExitWest = new Exit ("W", stClairWestStation); streetcar.addExit(stClairWestStationExitWest);
      final Exit streetcarExitEast = new Exit("E", streetcar); stClairWestStation.addExit(streetcarExitEast);
      final Exit onTheRunExitWest = new Exit("W", onTheRun); stClairWestStation.addExit(onTheRunExitWest);
      final Exit stClairWestStationExitEast = new Exit("E", stClairWestStation); onTheRun.addExit(stClairWestStationExitEast);


      



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
          
          unionShopArea.setRunnable(new Runnable(){

                boolean[] fightDone = {false};
                @Override
                public void run() {
                  if(!fightDone[0]){
                    Inventory i = new Inventory(2600);
                    i.addItem(new Weapon(12,"Crow bar", false, 10, null));
                    final Enemy PRIME_THEIF = new Enemy(null, unionShopArea, 20, i, 20, "Prime Theif", 8);
                    unionShopArea.addEnemies(PRIME_THEIF); 
                    try { 
                      
                      Fight f = new Fight(PRIME_THEIF);
                      Graphics text = new Graphics();
                      text.slowTextSpeed("HEY YOU... I heard that your on a prime quest so i know you have some. NOW GIVE IT TOO ME!!!!", 12);
                      boolean won = f.fight();
                      if(won) {
                          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(PRIME_THEIF);
                          fightDone[0] = true;
                      }
                    } catch (Exception e) {
                      // TODO: handle exception
                    }
                  }
                }

             
            });

        //unionTimHortons
        final Room unionTimHortons = new Room ("Placeholder Description for unionTimHortons", "uniontimhortons"); roomMap.put(unionTimHortons.getRoomName(), 
        unionTimHortons);
            unionTimHortons.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Hi Welcome to Tim Hortons, Would you like to buy anything? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() <=1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Boston Cream Donut - 4$", 7);
                            text.slowTextSpeed("> French Vanilla - 2$", 7);
                            text.slowTextSpeed("> Toasted Bagal With Cream Cheese - 7$", 7);
                            String b = in.nextLine();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.equalsIgnoreCase("boston cream donut")){
                                if((pMoney - 4)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=4);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Boston Cream Donut", false, 
                                    new Effect("Health up", 0, 0, 4, 20), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                }
                            }else if(b.equalsIgnoreCase("French Vanilla")){
                                if((pMoney - 2)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=2);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "French Vanilla", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                }
                            }else if(b.equalsIgnoreCase("Toasted Bagal With Cream Cheese")){
                                if((pMoney - 7)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=7);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Toasted Bagal With Cream Cheese", false, 
                                    new Effect("Super Delicous", 0, 0, -8, 30), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }
                          
                          if(finishedOrder){
                            while(true){
                              text.slowTextSpeed("Would you like anything else? y/n", 7);
                              String c = in.nextLine();
                              if(c.equalsIgnoreCase("y")){
                                finishedOrder = false;
                                break;
                              }else if(c.equalsIgnoreCase("n")){
                                finishedOrder = true;
                                text.slowTextSpeed("Thank you, come again!", 7);
                                break;
                              }
                            }
                          }
                        }
                    }else{
                      text.slowTextSpeed("ok come back soon", 7);
                    }
                    
                  } catch (Exception e) {
                    // TODO: handle exception
                  }

                }
            });
        //Main area
        final Room unionMainArea = new Room ("Placeholder Description for unionMainArea", "unionmainarea"); roomMap.put(unionMainArea.getRoomName(), unionMainArea);
            if(!isTesting){
              boolean[] hasEnteredMainArea = {false};
              unionMainArea.setRunnable(new Runnable(){
                
                ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
                @Override
                public void run() {
                  if (!hasEnteredMainArea[0]) {
                    hasEnteredMainArea[0] = true;
                    try {
                      renderer.showCutScene(1100, "\\bin\\zork\\data\\unioncyruscall.txt", 15);
                    } catch (Exception e) {
                      handleException(e);
                    }
                  }
                }
              
            });
              }
        //union Corner
        final Room unionCorner = new Room ("Placeholder Description for unionCorner", "unioncorner"); roomMap.put(unionCorner.getRoomName(), unionCorner);
        //scams Market
        final Room unionScamsMarket = new Room ("Placeholder Description for unionScamsMarket", "unionscamsmarket"); roomMap.put(unionScamsMarket.getRoomName(), unionScamsMarket);
        unionScamsMarket.setRunnable(new Runnable(){
         
          @Override
          public void run() {
            ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
            boolean hasCoupon = false;
              for(int i = 0; i<ar.size(); i++){
                  if(ar.get(i).getName().equals("Free Prime Coupon"))
                    hasCoupon = true;
                  
              }
              if(hasCoupon){
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
        unionFacultyCloset.addItemGround(new Item(2, "Free Prime Coupon", false, null, false));
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
    this.player.setCurrentRoom(roomMap.get("unionplatform"));
    this.player.getInventory().addItem(new Weapon(0, "Fists", false, 5, 
      new Effect("Bleeding", 2, 2, 5, 0)));
    if (isTesting) {
      this.player.getInventory().addItem(new Weapon(0, "Ban Hammer", false, 1000000, null));
    }
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
            System.out.print("Please enter a name between 1-16 Characters: ");
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
          } else if (result.equalsIgnoreCase("jch shampoo")){
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