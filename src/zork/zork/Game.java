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

import javax.swing.SortOrder;
import javax.swing.event.SwingPropertyChangeSupport;

import com.google.gson.Gson;

import datatypes.CommandNotFoundException;
import datatypes.Location;
import zork.Constants.PlayerConstants;
import zork.Utils.SoundHandler;
import zork.entites.Enemy;
import zork.entites.Player;
import zork.items.Prime;
import zork.items.Weapon;
import java.lang.Runnable;

public class Game {

  private final Graphics renderer = new Graphics();
  public static AtomicBoolean bool = new AtomicBoolean();
  private final Gson gson = new Gson();
  public static boolean music = false;
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
      final Room pickeringCameronsHouse = new Room ("Home sweet home. use this as an area to store things if you run out of carrying capacity. or don't I couldn't really care less.", "pickeringcameronshouse"); roomMap.put(pickeringCameronsHouse.getRoomName(), pickeringCameronsHouse);
      // SHEPPARD YONGE ROOMS
      final Room sheppardYongeOffice = new Room("Sheppard yonge office. Prime everywhere!", "sheppardyongeoffice");
      roomMap.put(sheppardYongeOffice.getRoomName(), sheppardYongeOffice);
      boolean[] hasFoughtCEO = new boolean[] { false };

      final Room sheppardYongeCorporateElevator = new Room("Corporate elevator, it's going up.",
          "sheppardyongecorporateelevator");
      roomMap.put(sheppardYongeCorporateElevator.getRoomName(), sheppardYongeCorporateElevator);
      sheppardYongeCorporateElevator.setRunnable(() -> {
        Graphics g = getRenderer();
        try {
          g.slowTextSpeed("You're almost at the top. The elevator slows as it reaches the final floor.", 50);
          Thread.sleep(2000);
          g.slowTextSpeed(
              "You've arrived at the top! the office has prime bottles everywhere.\nRemember what the security said!",
              50);
          Game.getGame().getPlayer().changeRoom(sheppardYongeOffice);
        } catch (InterruptedException e) {
        }
      });
      final Room sheppardYongeSecretRoom = new Room(
          "There are windows on your left and right and an elevator gets you in, and up.", "sheppardyongesecretroom");
      boolean[] hasFoughtSecurity = new boolean[] { false };
      Inventory securityInventory = new Inventory(1);
      Effect electricity = new Effect("Zap", 2, 5, -10, 0);
      securityInventory.addItem(new Weapon(5, "Tazer", false, 15, electricity));
      Enemy securityMan = new Enemy(null, sheppardYongeSecretRoom, 300, securityInventory, 200, "Security", 450);

      sheppardYongeSecretRoom.setRunnable(() -> {
        if (!hasFoughtSecurity[0]) {

          try {
            Thread.sleep(3000);
            Game.getGame().getRenderer().slowTextSpeed(
                "You see a security guard in the room. He stands between you and the corporate elevator.", 50);
            Game.getGame().getRenderer()
                .slowTextSpeed("Your goal is to get in, and get up. He isn't letting that happen.", 50);
            Game.getGame().getRenderer().slowTextSpeed("Time to fight.", 500);
            Fight securityGuardFight = new Fight(securityMan);
            if (securityGuardFight.fight()) {
              Game.getGame().getRenderer().slowTextSpeed(
                  "The security guard backs down.\nHe gives you his elevator keycard. \nHe says to make it quick, better listen.",
                  50);
              Game.getGame().getPlayer().changeRoom(roomMap.get("sheppardyongecorporateelevator"));
              hasFoughtSecurity[0] = true;
              // UNLOCK ELEVATOR
            } else {
              System.out.println("YOU DIED! YOU DONT GET TO GO UP!");
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
            }
          } catch (InterruptedException e) {
          }
        }
      });
      roomMap.put("sheppardyongesecretroom", sheppardYongeSecretRoom);
      final Room sheppardYongeLine1 = new Room(
          "Going south will lead you to Lawrence, North to Finch is under maintainence.", "sheppardyongeline1");
      roomMap.put(sheppardYongeLine1.getRoomName(), sheppardYongeLine1);
      final Room sheppardYongeLine4 = new Room(
          "Going east will lead you to Bayview. Going west will lead you into a tunnel.", "sheppardyongeline4");
      roomMap.put(sheppardYongeLine4.getRoomName(), sheppardYongeLine4);
      final Room sheppardYongeLine4StreetHallway = new Room(
          "The escalator is stopped. The door to the street is nearby.", "sheppardyongeline4streethallway");
      roomMap.put(sheppardYongeLine4StreetHallway.getRoomName(), sheppardYongeLine4StreetHallway);
      final Room sheppardYongeLine1HallwayBeforeStreet = new Room("TTC Harlandale Avenue. Nothing here.",
          "sheppardyongeline1hallwaybeforestreet");
      roomMap.put(sheppardYongeLine1HallwayBeforeStreet.getRoomName(), sheppardYongeLine1HallwayBeforeStreet);

      /*
       * Example enemy and usage when entering room.
       */
      boolean[] hasFoughtCrackhead = new boolean[] { false };
      Inventory crackHeadInventory = new Inventory(5);
      Effect crackHeadPoison = new Effect("Poison", 5, 5, -5, 0);
      crackHeadInventory.addItem(new Weapon(5, "Needle", false, 10, crackHeadPoison));
      // make crackhead
      Enemy crackHead = new Enemy(null, sheppardYongeLine4StreetHallway, 25, crackHeadInventory, 0, "Crackhead", 1);
      sheppardYongeLine4StreetHallway.setRunnable(() -> {
        if (!hasFoughtCrackhead[0]) {
          try {
            Game.getGame().getRenderer()
                .slowTextSpeed("You see a crackhead yelling at innocent TTC Passengers. He wants your prime.", 20);
          } catch (InterruptedException e) {
          }
          // fight crackhead
          Fight crackHeadFight = new Fight(crackHead);
          if (crackHeadFight.fight()) {
            Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(crackHead);
            hasFoughtCrackhead[0] = true;
          } else {
            System.out.println("didnt win. crackhead steals all ur stuff");
            Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
          }
        }
      });

      sheppardYongeOffice.setRunnable(() -> {
        if (!hasFoughtCEO[0]) {
          // TODO: CHANGE YELLOW TO REAL FALVOR
          Inventory i = new Inventory(5);
          i.addItem(new Prime(5, "TROPICAL PUNCH PRIME", false, "YELLOW", false, "CEO"));
          i.addItem(new Weapon(0, "TROPICAL PUNCH PRIME", false, 20, null));
          Enemy CEO = new Enemy(null, sheppardYongeOffice, 100, i, 0, "CEO", 500);
          Fight f = new Fight(CEO);
          if (f.fight()) {
            hasFoughtCEO[0] = true;
            Graphics g = getRenderer();
            try {
              g.slowTextSpeed(
                  "You won. You got the tropical punch prime. The crazy CEO decides to quit his job and trade bitcoin.",
                  50);
              g.slowTextSpeed("The elevator beeps. It's time to go back down.", 50);
              Thread.sleep(2000);
              new Thread(new Runnable() {

                @Override
                public void run() {
                  player.changeRoom(roomMap.get("sheppardyongesecretroom"));
                }

              }).start();

            } catch (InterruptedException e) {

            }
          } else {
            System.out.println("BETTER LUCK NEXT TIME LOL!");
            Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
          }
        }
      });



      //YORK MILLS AREA ROOMS
      final Room yorkMillsBusTerminal = new Room("The bus","yorkmillsbusterminal"); roomMap.put(yorkMillsBusTerminal.getRoomName(),yorkMillsBusTerminal);
      final Room facultyRoom = new Room("A staff room with a few tables", "facultyroom"); roomMap.put(facultyRoom.getRoomName(), facultyRoom);
      
      final Room yorkMillsSubwayHallway = new Room("A Hallway is ahead leading to the Subway, Chuck Page plays some guitar for passersby.","yorkmillssubwayhallway"); roomMap.put(yorkMillsSubwayHallway.getRoomName(), yorkMillsSubwayHallway);
      final Room yorkMillsSubway = new Room("Please come back later, unscheduled maintenance has just been scheduled, shuttlebuses are available.", "yorkmillssubway","york mills subway",true,"Come back when maintenance is completed");
      final Room yorkMillsBus = new Room("This 95A bus will lead you east to Bayview Glen, or West to York Mills Station.", "yorkmillsbus");

      final Room gatewayNewsstands = new Room("Hello, would you like to purchase anything?", "gatewaynewsstands"); roomMap.put(gatewayNewsstands.getRoomName(), gatewayNewsstands);
            gatewayNewsstands.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Hello, would you like to purchase anything? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Gummy Bears - 1$", 7);
                            text.slowTextSpeed("> Snickers Bar - 2$", 7);
                            text.slowTextSpeed("> Rusty Scissors - 5$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("gummy bears")){
                                if((pMoney - 10)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=10);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Gummy Bears", false, 
                                    new Effect("Health up", 0, 0, 4, 10), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else if(b.contains("a snickers bar")){
                                if((pMoney - 8)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=8);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "A Snickers Bar", false, 
                                    new Effect("Health Up", 0, 0, 0, 15), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;
                                }
                            }else if(b.contains("rusty scissors")){
                                if((pMoney - 5)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=5);
                                  Effect scissorBleeding = new Effect("Bleeding", 2, 3, 1, 0);
                                  Game.getGame().getPlayer().getInventory().addItem( new Weapon(15, "Rusty Scissors", false, 20, scissorBleeding));
                                    
                                    
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR");
                            finishedOrder = false;
                            break;
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
      
      
      


      yorkMillsSubway.addItemGround(new Item(1,  "transfer",false, null, false));
      final Room eglintonShuttleBus = new Room("Going south will lead you to Eglinton Station via the shuttle bus", "eglintonshuttlebus"); roomMap.put(eglintonShuttleBus.getRoomName(), eglintonShuttleBus); roomMap.put(yorkMillsSubway.getRoomName(), yorkMillsSubway);
      yorkMillsSubway.addItemGround(new Item(1,  "transfer", false, null, false));

      //EGLINTON AREA ROOMS

      final Room yorkMillsShuttleBus = new Room("Going north will lead you to York Mills Station", "yorkmillsshuttlebus"); roomMap.put(yorkMillsShuttleBus.getRoomName(), yorkMillsShuttleBus);
      final Room eglintonBusStop = new Room("You face the completely halted traffic of Yonge and Eglinton", "eglintonbusstop"); roomMap.put(eglintonBusStop.getRoomName(), eglintonBusStop);
      final Room eglintonStation = new Room("you have entered Eglinton Station. It smells of cinnabons.", "eglintonstation"); roomMap.put(eglintonStation.getRoomName(), eglintonStation);

      final Room eglintonStreet = new Room("You are on the sidewalk on Eglinton Street, you can feel the subway rumble below you.", "eglintonstreet"); roomMap.put(eglintonStreet.getRoomName(), eglintonStreet);
      final Room yongeEglintonMall = new Room("You stand in the lobby of the Yonge and Eglinton Mall.", "yongeeglintonmall"); roomMap.put(yongeEglintonMall.getRoomName(), yongeEglintonMall);
      
      
      final Room eglintonSubway = new Room ("South leads to St. Clair station, North leads to York Mills Station", "eglintonsubway",true, "come back when maintenance is completed"); roomMap.put(eglintonSubway.getRoomName(), eglintonSubway);

      final Room foodCourt = new Room ("You can hear the subway rumbling in the background", "foodcourt"); roomMap.put(foodCourt.getRoomName(), foodCourt);
          boolean[] boughtPrime1 = {false};    
        foodCourt.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Yes, you see the price tags. Inflation is a real killer. Buy something? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                      
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> A skittle - 10$", 7);
                            text.slowTextSpeed("> A peanut - 2$", 7);
                            if(!boughtPrime1[0])text.slowTextSpeed("> PRIME - 50$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("a skittle")){
                                if((pMoney - 10)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=10);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "A Skittle", false, 
                                    new Effect("Health up", 0, 0, 4, 5), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else if(b.contains("a peanut")){
                                if((pMoney - 8)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=8);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "A Peanut", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;
                                }
                            }else if(b.contains("prime") && !boughtPrime1[0]){
                                if((pMoney - 50)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=50);
                                  Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "ICE POP PRIME", false, "ICE", true, "foodcourt"));
                                  try {
                                    renderer.showCutScene(1500, "\\bin\\zork\\data\\foodcourtcyruscall.txt", 15);
                                  } catch (Exception e) {
                                    handleException(e);
                                  }   
                                    boughtPrime1[0] = true;
                                    
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR");
                            finishedOrder = false;
                            break;
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

      
            boolean boughtPrime2[] = {false};
      final Room circleK = new Room ("", "circlek"); roomMap.put(circleK.getRoomName(), circleK);
            circleK.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("I am forever grateful to you for defending us against the homeless. Does anything in my humble shop attract you? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Slim Jim - 2$", 7);
                            text.slowTextSpeed("> Mike and Ike - 3$", 7);
                            if(!boughtPrime2[0])text.slowTextSpeed("> PRIME - 10$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("slim jim")){
                                if((pMoney - 4)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=2);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Slim Jim", false, 
                                    new Effect("Health up", 0, 0, 3, 10), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                  break;
                                }
                            }else if(b.contains("mike and ike")){
                                if((pMoney - 3)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=3);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Mike and Ike", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                  break;
                                }
                            }else if(b.contains("prime") && !boughtPrime2[0]){
                                if((pMoney - 10)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=10);
                                  Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "BLUE RASPBERRY PRIME", false, "Blue", true, "circlek"));
                                  eglintonSubway.setLocked(false);
                                  yorkMillsSubway.setLocked(false);
                                  try {
                                    renderer.showCutScene(1500, "\\bin\\zork\\data\\circlekcyruscall.txt", 15);
                                  } catch (Exception e) {
                                    handleException(e);
                                  }
                                  
                                    boughtPrime2[0] = true;
                                    
                                    finishedOrder = true;

                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR FOR THE DON");
                            finishedOrder = false;
                            break;
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
            

      boolean[] hasFoughtHomeless = new boolean[]{false};
      Inventory homelessInventory = new Inventory(5);
      
      homelessInventory.addItem(new Weapon(15, "lead pipe",false, 5,null));
    
      Enemy homeless = new Enemy(null, yongeEglintonMall, 25, homelessInventory, 10, "Homeless dude", 30);
      yongeEglintonMall.setRunnable(() -> {
        if(!hasFoughtHomeless[0]) {
      
        Fight homelessFight = new Fight(homeless);
        if(homelessFight.fight()) {
          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(homeless);
          game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Lead Pipe", false, 23, null));
          hasFoughtHomeless[0] = true;
        } else {
          System.out.println("didnt win. homeless dude steals all ur stuff");
          Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
        }
      }
      });

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

      

      boolean[] hasFoughtDriver = new boolean[]{false};
      Inventory driverInventory = new Inventory(5);
      
      driverInventory.addItem(new Weapon(5, "Presto Card",false, 10,null));
     
      Enemy driver = new Enemy(null, streetcar, 20, driverInventory, 10, "TTC Driver", 40);
      streetcar.setRunnable(() -> {
        if(!hasFoughtDriver[0]) {
        
        Fight driverFight = new Fight(driver);
        if(driverFight.fight()) {
          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(driver);
          hasFoughtDriver[0] = true;
        } else {
          System.out.println("didnt win. Your body has been kicked off the streetcar.");
          Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
        }
      }
      });

      final Room corporateCafe = new Room("You step into a bleak cafe", "corporatecafe"); roomMap.put(corporateCafe.getRoomName(), corporateCafe);
            corporateCafe.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Would you like to buy one of our various uninspired foods? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Plain Donut - 3$", 7);
                            text.slowTextSpeed("> Watered down tea - 1$", 7);
                            text.slowTextSpeed("> 'Fresh' ham sandwich - 5$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("plain donut")){
                                if((pMoney - 3)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=3);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "plain donut", false, 
                                    new Effect("Health up", 0, 0, 4, 20), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                  break;
                                }
                            }else if(b.contains("watered down tea")){
                                if((pMoney - 1)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=1);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "watered down tea", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                  break;

                                }
                            }else if(b.contains("fresh ham sandwich")){
                                if((pMoney - 5)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=5);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "'Fresh' ham sandwich", false, 
                                    new Effect("Not Delicous", 0, 0, -8, 30), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR");
                            finishedOrder = false;
                            break;
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

      final Room mcDonalds = new Room("You walk into the cramped subway station McDonald's", "mcdonalds"); roomMap.put(mcDonalds.getRoomName(), mcDonalds);
            mcDonalds.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Hi Welcome to McDonald's, Would you like to buy anything? y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Big Mac - 4$", 7);
                            text.slowTextSpeed("> McCafe Coffee - 2$", 7);
                            text.slowTextSpeed("> Happy Meal - 7$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("big mac")){
                                if((pMoney - 4)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=4);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Big Mac", false, 
                                    new Effect("Health up", 0, 0, 4, 20), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else if(b.contains("mccafe coffee")){
                                if((pMoney - 2)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=2);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "McCafe Coffee", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else if(b.contains("happy meal")){
                                if((pMoney - 7)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=7);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Happy Meal", false, 
                                    new Effect("Super Delicous", 0, 0, -8, 30), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke for Don's LMAO", 7); //CHANGE LATER
                                  break;

                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR FOR THE DON");
                            finishedOrder = false;
                            break;
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

            boolean[] hasFoughtEmployee = new boolean[]{false};
      Inventory employeeInventory = new Inventory(5);
      
      employeeInventory.addItem(new Weapon(5, "Ballpoint Pen",false, 10,null));
     
      Enemy employee = new Enemy(null, officeRoom, 45, employeeInventory, 30, "Disgruntled Employee", 30);
      officeRoom.setRunnable(() -> {
        if(!hasFoughtEmployee[0]) {
        
        Fight employeeFight = new Fight(employee);
        if(employeeFight.fight()) {
          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(employee);
          hasFoughtEmployee[0] = true;
        } else {
          System.out.println("didnt win. Your body has been reported to security.");
          Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
        }
      }
      });

      boolean[] hasFoughtSmoker = new boolean[]{false};
      Inventory smokerInventory = new Inventory(5);
      Effect smokerBurning = new Effect("Burning", 3, 5, -5, 0);
      Item lighter = new Weapon(5, "lighter",true, 20, smokerBurning);
      
      smokerInventory.addItem(lighter);
      
      Enemy smoker = new Enemy(null, rolexStairwell, 30, smokerInventory, 10, "Smoker", 20);
      
      rolexStairwell.setRunnable(() -> {
        Graphics text = new Graphics();
        if(!hasFoughtSmoker[0]) {
    
        Fight smokerFight = new Fight(smoker);
        if(smokerFight.fight()) {
          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(smoker);
          hasFoughtSmoker[0] = true;
          Game.getGame().getPlayer().getInventory().addItem(lighter);
        } else {
          try {
            text.slowTextSpeed("You died. Your casket will smell like tobacco.", 20);
            Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
          } catch (Exception e) {
            // TODO: handle exception
          }
          
        }
      }
      });
      

      //ST. CLAIR WEST AREA ROOMS
      final Room stClairWestStation = new Room ("As you enter the station, a distinct smell of garbage envelops your senses.", "stclairweststation"); roomMap.put(stClairWestStation.getRoomName(), stClairWestStation);
      
      final Room stClairWestSubway = new Room ("'Probably one of the nicer subway stations', you think to yourself. ", "stclairwestsubway"); roomMap.put(stClairWestSubway.getRoomName(), stClairWestSubway);

      final Room onTheRun = new Room("Stepping into the dank and small convenience store, a wall of candy and slim jims greet your eyes.", "ontherun"); roomMap.put(onTheRun.getRoomName(), onTheRun);
          boolean[] boughtPrime3 = {false};    
        onTheRun.setRunnable(new Runnable(){
                public void run(){
                  try {
                    Graphics text = new Graphics();
                    Scanner in = new Scanner(System.in);
                    text.slowTextSpeed("Please, take this PRIME? It means nothing to me now. y/n", 7);
                    String a = in.nextLine();
                    if(a.equalsIgnoreCase("y")){
                      boolean finishedOrder = false;
                        while(!finishedOrder){
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Slim Jim - 2$", 7);
                            text.slowTextSpeed("> Mike and Ike - 3$", 7);
                            if(!boughtPrime3[0])text.slowTextSpeed("> PRIME - 0$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("slim jim")){
                                if((pMoney - 2)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=2);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Slim Jim", false, 
                                    new Effect("Health up", 0, 0, 3, 15), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                }
                            }else if(b.contains("mike and ike")){
                                if((pMoney - 3)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=3);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Mike and Ike", false, 
                                    new Effect("Health Up", 0, 0, 0, 8), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                                }
                            }else if(b.contains("prime") && !boughtPrime3[0]){
                                if((pMoney - 0)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=0);
                                  Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "GRAPE PRIME", false, "Purple", true, "On The Run"));
                                  try {
                                    renderer.showCutScene(1100, "\\bin\\zork\\data\\stclairwestcyruscall.txt", 15);
                                  } catch (Exception e) {
                                    handleException(e);
                                  }
                                  
        
                                    
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                                }
                                boughtPrime3[0] = true;
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          }else{
                            System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR FOR THE DON");
                            finishedOrder = false;
                            break;
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



      //MISCELLANEOUS ROOMS

      final Room summerhillSubway = new Room("Please stay on the train, police investigation underway", "summerhillSubway"); roomMap.put(summerhillSubway.getRoomName(), summerhillSubway);
      final Room bloorYongeSubway = new Room("This was the next station available that wasn't under construction", "blooryongesubway"); roomMap.put(bloorYongeSubway.getRoomName(), bloorYongeSubway);
      final Room dundasSubway = new Room("The first station that's not undergoing a police investigation.", "dundassubway"); roomMap.put(dundasSubway.getRoomName(), dundasSubway);
      final Room lawrenceSubway = new Room("You fell asleep and ended up at Lawrence station. Yuck. Better get out of here fast.", "lawrencesubway"); roomMap.put(lawrenceSubway.getRoomName(), lawrenceSubway);
      final Room bloorYongeStation = new Room ("You'd rather not be here, given it's line 2 and all, but you know you must carry on in the name of PRIME", "blooryongestation"); roomMap.put(bloorYongeStation.getRoomName(), bloorYongeStation);
      final Room bloorYongeLine2 = new Room(" Caution tape blocks of the westward tunnel. I suppose the only way forward is to scarborough.", "blooryongeline2"); roomMap.put(bloorYongeLine2.getRoomName(), bloorYongeLine2);
      final Room yorkMillsAndDonMills = new Room("North goes to don mills. Take the 25b.","yorkmillsanddonmills"); roomMap.put(yorkMillsAndDonMills.getRoomName(), yorkMillsAndDonMills);
      final Room donMillsSubway = new Room("This is the only way to sheppard yonge.","donmillssubway"); roomMap.put(donMillsSubway.getRoomName(), donMillsSubway);

      



      //ELLESMERE AREA ROOMS

      final Room ellesmereStationUnderground = new Room("The Scarborough effect has set in. You now have the 'crippling fear' effect.", "ellesmerestationunderground", "Ellesmere Station Entrance"); roomMap.put(ellesmereStationUnderground.getRoomName(), ellesmereStationUnderground);
      final Room ellesmereSubway = new Room("You can't stand this place already, and exit the subway.","ellesmeresubway"); roomMap.put(ellesmereSubway.getRoomName(), ellesmereSubway);
      final Room sketchyStreetCorner = new Room("Just outside of the subway station, you are greeted by a sketchy street corner.", "sketchystreetcorner"); roomMap.put(sketchyStreetCorner.getRoomName(), sketchyStreetCorner);
      boolean[] hasFoughtThug = new boolean[]{false};
      Inventory thugInventory = new Inventory(5);
      
      thugInventory.addItem(new Weapon(5, "Socket Wrench",false, 20,null));
     
      Enemy thug = new Enemy(null, sketchyStreetCorner, 20, thugInventory, 10, "Thug", 40);
      sketchyStreetCorner.setRunnable(() -> {
        if(!hasFoughtThug[0]) {
        
        Fight thugFight = new Fight(thug);
        if(thugFight.fight()) {
          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(thug);
          Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "ORANGE PRIME", false, "ORANGE", false, "sketchystreetcorner"));
          try {
            renderer.showCutScene(1100, "\\bin\\zork\\data\\thugcyruscall.txt", 15);
          } catch (Exception e) {
            handleException(e);
          }
          hasFoughtThug[0] = true;
        } else {
          System.out.println("You won't be messing with him again.");
          Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
        }
      }
      });
      
      final Room krispyKreme = new Room("A sort of reward for braving the streets of scarborough. A place of salvation: Krispy Kreme", "krispykreme"); roomMap.put(krispyKreme.getRoomName(), krispyKreme);
                  krispyKreme.setRunnable(new Runnable(){
                    public void run(){
                      try {
                        Graphics text = new Graphics();
                        Scanner in = new Scanner(System.in);
                        text.slowTextSpeed("Please, indulge in our one-of-a-kind donuts, its sure to please you. y/n", 7);
                        String a = in.nextLine();
                        if(a.equalsIgnoreCase("y")){
                          boolean finishedOrder = false;
                            while(!finishedOrder){
                              if(Game.getGame().getPlayer().getMoney() >= 3){
                                text.slowTextSpeed("What would you like to buy?", 7);
                                text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                                text.slowTextSpeed("> Classic Donut - 3$", 7);
                                text.slowTextSpeed("> Chocolate Donut - 4$", 7);
                                text.slowTextSpeed("> Donut Mega pack - 10$", 7);
                                String b = in.nextLine().toLowerCase();
                                double pMoney = Game.getGame().getPlayer().getMoney();
                                if(b.contains("classic donut")){
                                    if((pMoney - 3)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=3);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Classic Donut", false, 
                                        new Effect("Krispy Kreme's creaming effect", 0, 0, 4, 35), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      text.slowTextSpeed("Im Sorry, Your too broke for Kreme's LMAO", 7); //CHANGE LATER
                                    }
                                }else if(b.contains("chocolate donut")){
                                    if((pMoney - 4)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=4);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Chocolate Donut", false, 
                                        new Effect("Health Up", 0, 0, 0, 40), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      text.slowTextSpeed("Im Sorry, Your too broke for Kreme's LMAO", 7); //CHANGE LATER
                                    }
                                }else if(b.contains("donut mega pack")){
                                    if((pMoney - 15)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=15);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Donut Mega Pack", false, 
                                        new Effect("Bussin on god", 0, 0, -2, 70), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      
                                      text.slowTextSpeed("Im Sorry, Your too broke for Kreme's LMAO", 7); //CHANGE LATER
                                    }
                                }else{
                                  
                                  
                                    text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                                  
                                  
                                }
                              } else {
                                text.slowTextSpeed("Im sorry, you cant afford anything on the menu", 0);
                                finishedOrder = false;
                                break;
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

      

       //BAYVIEW GLEN INDEPENDENT SCHOOL ROOMS
 
       final boolean[] hasEnteredLobby = new boolean[]{false};
       final boolean[] hasChosenInstrument = new boolean[]{false}; //These are all booleans to make sure everything only runs once
       final boolean[] hasGottenFirstKey = new boolean[]{false};
       final boolean[] dramaRoomDoorOpened = new boolean[]{false};
       final int[] learningCommonsSubAreaState = new int[]{0};
       final boolean[] artRoomLightsOn = new boolean[]{false, false, false};
       Item owensIphone = new Item(2, "Owen's IPhone", false, null, false);
       final boolean[] hasEnteredArtRoom = new boolean[]{false};

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
       bayviewGlenKitchen.addItemGround(new Weapon(10, "Knife", finished, 12, new Effect("bleeding", 1, 2, 2, 0)));
       Inventory mutatedFood = new Inventory(6);
       mutatedFood.addItem(new Weapon(3, "Acid Shot", false, 10, null));
       mutatedFood.addItem(new Weapon(3, "Rotted Chunk", false, 3, new Effect("Poison", 3, 6, 2, 0)));
       final Enemy mutatedFoodEnemy = new Enemy(null, bayviewGlenKitchen, 50, mutatedFood , 10, "MutatedFood", 75);
       bayviewGlenKitchen.enemies.add(mutatedFoodEnemy);
       bayviewGlenKitchen.setRunnable(() -> {
          if(bayviewGlenKitchen.enemies.contains(mutatedFoodEnemy)) {
            Fight f = new Fight(mutatedFoodEnemy);
            boolean won = f.fight();
            Effect mysteryHealth = new Effect("Weird lookin' piece of meat", 1, 0, 0, 30);
            if(won) {
              bayviewGlenKitchen.enemies.remove(mutatedFoodEnemy);
              Game.getGame().getPlayer().getInventory().addItem(new Item(5, "mystery food", true, mysteryHealth, false));
              

            }else{
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
            }
          }
       });

       final Room bayviewGlenCafeteriaDiningArea = new Room("You enter the Cafeteria Dining Area, so many seats you can't pick one. so you don't.", "bayviewglencafeteriadiningarea", false, "The cafeteria is closed, you hear your stomach grumbling but then you realize its cafeteria food and your urges subside"); roomMap.put(bayviewGlenCafeteriaDiningArea.getRoomName(), bayviewGlenCafeteriaDiningArea);
       final Room bayviewGlenPrepGym = new Room("You enter the gym, all the baskets are down. to your north you see an exit outside, which definitly will not lock behind you.", "bayviewglenprepgym", false, "The gym is closed and you are unable to get in"); roomMap.put(bayviewGlenPrepGym.getRoomName(), bayviewGlenPrepGym);
       final Room bayviewGlenWeightRoom = new Room("You enter the weight room. You are surrounded by heavy weights and machines.", "bayviewglenweightroom"); roomMap.put(bayviewGlenWeightRoom.getRoomName(), bayviewGlenWeightRoom);
       final Room bayviewGlenOutsidePrepGymNorth = new Room("You are outside the prep gym. the gym is directly south of you", "bayviewglenoutsideprepgymnorth"); roomMap.put(bayviewGlenOutsidePrepGymNorth.getRoomName(), bayviewGlenOutsidePrepGymNorth);
       final Room bayviewGlenLearningCommons = new Room("You enter the learning commons. but theres no books! oh no", "bayviewglenlearningcommons"); roomMap.put(bayviewGlenLearningCommons.getRoomName(), bayviewGlenLearningCommons);
       final Room bayviewGlenLearningCommonsSubArea = new Room ("...", "bayviewglenlearningcommonssubarea"); roomMap.put(bayviewGlenLearningCommonsSubArea.getRoomName(), bayviewGlenLearningCommonsSubArea);
       
       final Room bayviewGlenTheatre = new Room("You enter the theatre, the seats are out you see a door to the south", "bayviewglentheatre"); roomMap.put(bayviewGlenTheatre.getRoomName(), bayviewGlenTheatre);
       bayviewGlenTheatre.setRunnable(() -> {
        Graphics text = new Graphics();
        Scanner in = new Scanner(System.in);
        try {
          if (artRoomLightsOn[2] == false) {
          text.slowTextSpeed("You take a moment to walk around the stage, taking it all in. Huh thats? strange you notice a blue button in the back corner...\nDo you press it? Y/N ", 20);
          String ans = in.nextLine();
          if(ans.equalsIgnoreCase("Y")) {
            artRoomLightsOn[2] = true;
            text.slowTextSpeed("You feel the ground shaking, but then it stops. huh strange i wonder what that button did?", 20);
          } else {
            return;
          }
        }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        
       });
       
       final Room bayviewGlenHallwayTheatreBack = new Room ("You walk into a hallway behind the theatre, you see the grade 11 common area to the east", "bayviewglenhallwaytheatreback"); roomMap.put(bayviewGlenHallwayTheatreBack.getRoomName(), bayviewGlenHallwayTheatreBack);
       final Room bayviewGlenHallway2ndFloorToUpperSchool = new Room("Hallway towards the grade 11 common area. you see closed doors ahead to the south.", "bayviewglenhallway2ndfloortoupperschool"); roomMap.put(bayviewGlenHallway2ndFloorToUpperSchool.getRoomName(), bayviewGlenHallway2ndFloorToUpperSchool);
       
       final Room bayviewGlenG11CommonArea = new Room("Welcome to objectively the worst common area", "bayviewgleng11commonarea", false, "You go up the stairs and try to open the door, but as usual its locked an nobody is there to let you in."); roomMap.put(bayviewGlenG11CommonArea.getRoomName(), bayviewGlenG11CommonArea);
       
       
    
       
       final Room bayviewGlenGradHallway = new Room("You walk down the hallway filled with names of all the people who did better than you. a single tear runs down your cheek", "bayviewglengradhallway"); roomMap.put(bayviewGlenGradHallway.getRoomName(), bayviewGlenGradHallway);
       final Room bayviewGlenUpperMusicHallway = new Room ("You walk a narrow hallway past the music room", "bayviewglenuppermusichallway"); roomMap.put(bayviewGlenUpperMusicHallway.getRoomName(), bayviewGlenUpperMusicHallway);
       final Room bayviewGlenDramaRoom = new Room ("You enter the drama room, you see in large bold text #1 27. You don't know what it means but it sounds important so you write it down", "bayviewglendramaroom"); roomMap.put(bayviewGlenDramaRoom.getRoomName(), bayviewGlenDramaRoom);
       final Room bayviewGlenDeck = new Room ("You walk into the deck, you get a compelte view of the entire building from here.", "bayviewglendeck"); roomMap.put(bayviewGlenDeck.getRoomName(), bayviewGlenDeck);
       final Room bayviewGlenYorkMills = new Room ("You're on york mills street, Bayview Glen is to the north.", "bayviewglenyorkmills"); roomMap.put(bayviewGlenYorkMills.getRoomName(), bayviewGlenYorkMills);
       final Room bayviewGlen3rdFloorLobby = new Room ("You go to the third floor above the lobby, wow there's so much to do here!", "bayviewglen3rdfloorlobby"); roomMap.put(bayviewGlen3rdFloorLobby.getRoomName(), bayviewGlen3rdFloorLobby);
       final Room bayviewGlen3rdFloorWestPrepStairwayHallway = new Room ("Hallway, hallway and more hallway.", "bayviewglen3rdfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorWestPrepStairwayHallway);
       final Room bayviewGlen3rdFloorEastPrepStairwayHallway = new Room ("You are in a hallway, you look down at the gym from above.", "bayviewglen3rdflooreastprepstairwayhallway"); roomMap.put(bayviewGlen3rdFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen3rdFloorEastPrepStairwayHallway);
       final Room bayviewGlenG9CommonArea = new Room ("Here lies meatball, you monster", "bayviewgleng9commonarea"); roomMap.put(bayviewGlenG9CommonArea.getRoomName(), bayviewGlenG9CommonArea);
       Inventory meatBall = new Inventory(5);
       meatBall.addItem(new Weapon(3, "Fork", false, 10, null));
       final Enemy cyrus_meatball = new Enemy(null, bayviewGlenG9CommonArea, Constants.PlayerConstants.DEFAULT_HEALTH, meatBall , 5, "Meatball", 75);
       bayviewGlenG9CommonArea.enemies.add(cyrus_meatball);
       bayviewGlenG9CommonArea.setRunnable(new Runnable() {
 
         @Override
         public void run() {           
            if(Game.getGame().getPlayer().getCurrentRoom().enemies.contains(cyrus_meatball)) {
              System.out.println("Meatball is Blocking the Way");
              Fight f = new Fight(cyrus_meatball);
              Effect meatballHealth = new Effect("yummy meatball", 1, 0, 0, 20);
              
              boolean won = f.fight();
              if(won) {
              
              Game.getGame().getPlayer().getInventory().addItem(new Item(5, "Swedish Meatball", true, meatballHealth, false));
              Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(cyrus_meatball);
             }else{
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
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
            }else{
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
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
              text.slowTextSpeed(Game.getGame().getPlayer().getName() + " Got the Oboe", 15);
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
       bayviewGlenG10CommonArea.setRunnable(() -> {
          if(!artRoomLightsOn[1]) {
            Graphics text = new Graphics();
            Scanner in = new Scanner(System.in);
            try {
              text.slowTextSpeed("You see a red button in the corner... \n do you want to press it? Y/N: ", 20);
              String ans = in.nextLine();
              if(ans.equalsIgnoreCase("y")) {
                text.slowTextSpeed("Are you sure?? Y/N:", 20);
                ans = in.nextLine();
                if(ans.equalsIgnoreCase("y")) {
                  text.slowTextSpeed("Positive? Y/N:", 20);
                  ans = in.nextLine();
                  if(ans.equalsIgnoreCase("y")) {
                    text.slowTextSpeed("You are completly sure you want to press the button? Y/N: ", 20);
                    ans = in.nextLine();
                    if(ans.equalsIgnoreCase("y")) {
                      text.slowTextSpeed("100% sure you want to press it? Y/N", 20);
                      ans = in.nextLine();
                      if(ans.equalsIgnoreCase("y")) {
                        text.slowTextSpeed("What if something bad happens? Y/N", 20);
                        ans = in.nextLine();
                        if(ans.equalsIgnoreCase("y")) {
                          text.slowTextSpeed("Ok last time, im just looking out for you? Y/N", 20);
                          ans = in.nextLine();
                          if(ans.equalsIgnoreCase("y")) {
                            text.slowTextSpeed("Cmon seriously, are you COMPLETLY SURE you want to PUSH this BUTTON???? Y/N", 20);
                            ans = in.nextLine();
                            if(ans.equalsIgnoreCase("y")) {
                              text.slowTextSpeed("No joke are you serious, do you want to press this button?? Y/N", 20);
                              ans = in.nextLine();
                              if(ans.equalsIgnoreCase("y")) {
                                text.slowTextSpeed("You press the button", 500);
                                text.slowTextSpeed("...", 2000);
                                text.slowTextSpeed("The button makes a clicking sound, fun!", 20);
                                artRoomLightsOn[1] = true;
                                } 
                            } 
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } else {
                return;
              }
            } catch (Exception e) {
              // TODO: handle exception
            }
          }
       });

       
       
       final Room bayviewGlen3rdFloorBridge = new Room ("You enter a hallway that connects the upper and prep school", "bayviewglen3rdfloorbridge"); roomMap.put(bayviewGlen3rdFloorBridge.getRoomName(), bayviewGlen3rdFloorBridge);
       final Room bayviewGlen4thFloorEastPrepStairwayHallway = new Room ("What is even up here you may ask, the answer, is in your imagination", "bayviewglen4thflooreastprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorEastPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorEastPrepStairwayHallway);
       final Room bayviewGlen4thFloorWestPrepStairwayHallway = new Room ("And more hallway, when does it end!", "bayviewglen4thfloorwestprepstairwayhallway"); roomMap.put(bayviewGlen4thFloorWestPrepStairwayHallway.getRoomName(), bayviewGlen4thFloorWestPrepStairwayHallway);
       bayviewGlen4thFloorWestPrepStairwayHallway.setRunnable(() -> {
          Graphics text = new Graphics();
          Scanner in = new Scanner(System.in);
          try {
            text.slowTextSpeed("You see a rat dressed in a suit, you crouch down to hear him", 20);
            text.slowTextSpeed(" Tim - Well hello there traveller, welcome to Randy Rats shop, im Tim \n You - Wait if you name is Tim, why is it called Randy Rats shop \n Tim - Because tim doesnt rhyme idiot \n You - Ok well can i buy something \n Tim - Well im fresh out of stock, except for this stupid key I can't sell... they say it goes downstairs but my little rat legs could never get there \n You - How much? \n Tim - I'll give it to ya for 5 coins \n\n Would you like to buy the Key? Y/N: ", 20);
            String ans = in.nextLine();
            if(ans.equalsIgnoreCase("y")) {
              if(Game.getGame().getPlayer().getMoney()>=5) {
                text.slowTextSpeed(" You - Alright, i'll take it \n Tim - Alright here ya go" , 0);
                Game.getGame().getPlayer().getInventory().addItem(new Key("BasementBayviewGlenKey", "Basement Key", 2));
                Game.getGame().getPlayer().setMoney(Game.getGame().getPlayer().getMoney()-5);
                text.slowTextSpeed("You can now access the 1st Floor!", 20);
              } else
              text.slowTextSpeed("You cannot afford the little rat key", 20);
            } else {
              text.slowTextSpeed(" You - Maybe next time", 20);
              return;
            }
          } catch (Exception e) {
            
          }
          
       });
       final Room bayviewGlen4thFloorLobby = new Room ("You enter the top floor, right below you is right above the lobby. neat!", "bayviewglen4thfloorlobby"); roomMap.put(bayviewGlen4thFloorLobby.getRoomName(), bayviewGlen4thFloorLobby);
       final Room bayviewGlenPrepStaffRoom = new Room ("Nice little staff room overlooking the Learning Commons", "bayviewglenprepstaffroom"); roomMap.put(bayviewGlenPrepStaffRoom.getRoomName(), bayviewGlenPrepStaffRoom);
       Inventory questionMark = new Inventory(10);
       questionMark.addItem(new Weapon(3, "White Noise", false, 10, new Effect("Confusion", 20, 2, 0, 0)));
       questionMark.addItem(new Weapon(3, "the FIST", false, 20, null));
       final Enemy questionMarkEnemy = new Enemy(null, bayviewGlenPrepStaffRoom, 150, questionMark , 10, "?????", 100);
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
                }else{
                  Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
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
       final Room bayviewGlen1stFloorBelowG11CommonArea = new Room ("You enter a nice hallway leading to the g12 common area", "bayviewglen1stfloorbelowg11commonarea"); roomMap.put(bayviewGlen1stFloorBelowG11CommonArea.getRoomName(), bayviewGlen1stFloorBelowG11CommonArea);
       final Room bayviewGlenG12CommonArea = new Room ("You enter the grade 12 Common area... how did that theif even get in the school?", "bayviewgleng12commonarea"); roomMap.put(bayviewGlenG12CommonArea.getRoomName(), bayviewGlenG12CommonArea);
       final Room bayviewGlenArtRoom = new Room ("Why didn't cyrus just deactivate the robot? Maybe he couldn't who knows? maybe cyrus is the real enemy after all", "bayviewglenartroom"); roomMap.put(bayviewGlenArtRoom.getRoomName(), bayviewGlenArtRoom);
       final Room bayviewGlenHallwayOutsideUpperGym = new Room ("You enter a hallway, looking at a gym completly void of people", "bayviewglenhallwayoutsideuppergym"); roomMap.put(bayviewGlenHallwayOutsideUpperGym.getRoomName(), bayviewGlenHallwayOutsideUpperGym);
       final Room bayviewGlenUpperGym = new Room ("You enter the Upper School Gym, you shoot a basketball but it misses, just like old times", "bayviewglenuppergym", false, "The gym is closed, and you can't get in."); roomMap.put(bayviewGlenUpperGym.getRoomName(), bayviewGlenUpperGym);
 
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
               Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "Strawberry Watermelon Prime", isTesting, "pink", finished, ans));
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
       final Exit bayviewGlenHallwayPrepGymExitEast = new Exit("E",bayviewGlenHallwayPrepGym); bayviewGlenHallwayCafeteria.addExit(bayviewGlenHallwayPrepGymExitEast);

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
       final Exit bayviewGlenDramaRoomExitSouth = new Exit("S",bayviewGlenDramaRoom, "Door is locked", true); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenDramaRoomExitSouth);
       final Exit bayviewGlenG11CommonAreaExitEast = new Exit("E",bayviewGlenG11CommonArea); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenG11CommonAreaExitEast);
       final Exit bayviewGlenOutsideWestExitWestFour = new Exit("W",bayviewGlenOutsideWest); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenOutsideWestExitWestFour);
       final Exit bayviewGlenMathWingExitUp = new Exit("U",bayviewGlenMathWing); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenMathWingExitUp);
       final Exit bayviewGlenHallwayOutsideUpperGymExitDown = new Exit("D",bayviewGlenHallwayOutsideUpperGym, "You try to open the door but it doesn't budge", true); bayviewGlenHallwayTheatreBack.addExit(bayviewGlenHallwayOutsideUpperGymExitDown);
 
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
       final Exit bayviewGlen1stFloorBelowG11CommonAreaExitDown = new Exit("D",bayviewGlen1stFloorBelowG11CommonArea, "Doors locked bud", true); bayviewGlenG11CommonArea.addExit(bayviewGlen1stFloorBelowG11CommonAreaExitDown);
 
       final Exit bayviewGlenG12CommonAreaExitDown = new Exit("D",bayviewGlenG12CommonArea, "You try the handle but it doesnt budge", true); bayviewGlen2ndFloorUpperHallway.addExit(bayviewGlenG12CommonAreaExitDown);
 
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
        if(bayviewGlenG11CommonAreaExitSouth.getIsExitLocked()) {
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
      }
       });

       
       Inventory dramaRoomDoor = new Inventory(0);
       dramaRoomDoor.addItem(new Weapon(0, "Silence", false, 0, null));
       final Enemy dramaRoomDoorEnemy = new Enemy(null, bayviewGlenHallwayTheatreBack, 100, dramaRoomDoor , 0, "Door", 1);
       bayviewGlenHallwayTheatreBack.addEnemies(dramaRoomDoorEnemy);
       bayviewGlenHallwayTheatreBack.setRunnable(() -> {
        System.out.println("gothere");
        if(!dramaRoomDoorOpened[0]) {
          try {
            Graphics text = new Graphics();
            bayviewGlenHallwayTheatreBack.printAscii();
            text.slowTextSpeed("The door to the south is locked :/", 20);  
            text.slowTextSpeed("...", 2000); 
            text.slowTextSpeed("Would you.. like to fight the door? Y/N", 20);  
            Scanner in = new Scanner(System.in);
            String ans = in.nextLine();
            if(ans.equalsIgnoreCase("y")) {
              Fight f = new Fight(dramaRoomDoorEnemy);
              boolean won = f.fight();
              if(won) {
                text.slowTextSpeed("The door bursts open", 20);
                bayviewGlenDramaRoomExitSouth.setIsExitLocked(false);
                dramaRoomDoorOpened[0] = true;
                bayviewGlenHallwayTheatreBack.enemies.remove(dramaRoomDoorEnemy);
              }else{
                Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
              }
            } else {
              return;
            }
          } catch (Exception e) {
              
          }
        }
        if (bayviewGlenHallwayOutsideUpperGymExitDown.getIsExitLocked()) {
          for (Item i : Game.getGame().getPlayer().getInventory().getItems()) {
            if (i.getName().equals("Basement Key")) {
              if (i.getKeyId().equals("BasementBayviewGlenKey")) {
                Graphics text = new Graphics();
                bayviewGlenHallwayOutsideUpperGymExitDown.setIsExitLocked(false);
                try {
                  text.slowTextSpeed("You use the rat's key to unlock the 1st floor here", 20);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      });
       



       bayviewGlenLearningCommonsSubArea.setRunnable(() -> {
        Graphics text = new Graphics();
        
        try {
          bayviewGlenLearningCommons.printAscii();
          if(learningCommonsSubAreaState[0] == 0) {
            text.slowTextSpeed(" ??? - Hello \n You - Hello? \n Owen - Hello nice to meet you, im owen \n You - Hello Owen \n Owen - I know you're looking for the STRAWBERRY WATERMELON tm. prime, I think I know something that could help with that... \n You - Really, thats great \n Owen - Yes I have the second digit, its on this note I found \n You - What is it? \n Owen - Not so fast, I need something from you first. \n You - Ugh fine what is it \n Owen - I seem to have lost my phone somewhere in this building, if you can find it I can give you this sheet \n You - Alright i'll get right to it.", 20);
            learningCommonsSubAreaState[0] = 1;
          } else if (learningCommonsSubAreaState[0] == 1) {
              if(Game.getGame().getPlayer().getInventory().getItems().contains(owensIphone)) {
                text.slowTextSpeed(" Owen - Did you get my phone? \n You - Yes here it is \n Owen - Thank you!, now i'll hold up my end of the deal \n You - Thank you \n NOTE ADDED TO INVENTORY", 20);
                Game.getGame().getPlayer().getInventory().getItems().remove(owensIphone);
                Game.getGame().getPlayer().getInventory().addItem(new Item(1, "#2 - 05", false, null, false));
                learningCommonsSubAreaState[0] = 2;
              } else {
                text.slowTextSpeed(" Owen - Did you get my phone? \n You - No... \n Owen - Well then get going then, i dont got all day", 20);
              }            
          } else if (learningCommonsSubAreaState[0] == 2) {
              text.slowTextSpeed(" Owen - Get out of here i'm working on my zork project \n You think to yourself... Wasn't it due ages ago?", 20);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        
      });
      

       Inventory theif = new Inventory(10);
       theif.addItem(new Weapon(3, "Knife", false, 15, new Effect("Bleeding",4, 2, 5, 0)));
       final Enemy theifEnemy = new Enemy(null, bayviewGlenG12CommonArea, 80, theif , 0, "Theif", 60);
       bayviewGlenG12CommonArea.addEnemies(theifEnemy);
       bayviewGlenG12CommonArea.setRunnable(() -> {
        Graphics text = new Graphics();
          if(bayviewGlenG12CommonArea.enemies.contains(theifEnemy)) {
            try {
              text.slowTextSpeed("You see a theif taking something on the counter \n !! THEY SAW YOU !! \n Theif - NO WITNESSES!!", 20);
              Fight f = new Fight(theifEnemy);
              boolean won = f.fight();
              if (won) {
                Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(theifEnemy);
                Effect grantsElectricalProblems = new Effect("Grant's electrical problems", 3, 9, 0, 0);
                game.getGame().getPlayer().getInventory().addItem(new Weapon(70, "Robotics club prototype", false, 3, grantsElectricalProblems));
                text.slowTextSpeed("You see a black rectangle on the ground \n its OWENS PHONE! \n its on the ground", 20);
                bayviewGlenG12CommonArea.addItemGround(owensIphone);
              }else{
                Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
              }
              
            } catch (Exception e) {
              
            }
          }
          String greenLight; String redLight; String blueLight;
          if(!artRoomLightsOn[0]) {
            greenLight = "off";
          } else {
            greenLight = "on";
          }

          if(!artRoomLightsOn[1]) {
            redLight = "off";
          } else {
            redLight = "on";
          }

          if(!artRoomLightsOn[2]) {
            blueLight = "off";
          } else {
            blueLight = "on";
          }

          try {
            text.slowTextSpeed("\n\nAs you enter the common area your eyes immediatly notice the lights above the art room \nGreen Light: " + greenLight + ", Red Light: " + redLight + ", Blue Light: " + blueLight, 20);
            if(greenLight.equals(redLight) && greenLight.equals(blueLight) && redLight.equals("on")) {
              bayviewGlenArtRoomExitDown.setIsExitLocked(false);
              text.slowTextSpeed("You see all lights are on, and the gate in front of the art room has moved and there is a free passage down", 20);
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          };
        });
          
          Inventory cyrusRobot = new Inventory(10);
          cyrusRobot.addItem(new Weapon(3, "Lazer Beam", false, 25, null));
          cyrusRobot.addItem(new Weapon(3, "Missile", false, 20, null));
          cyrusRobot.addItem(new Weapon(3, "Pocket Atomic Bomb", false, 0, new Effect("Radiation Poisoning", 10, 10, 0, 0)));
          final Enemy cyrusRobotEnemy = new Enemy(null, bayviewGlenArtRoom, 150, cyrusRobot , 0, "Cyrus-Robot", 60);
          bayviewGlenArtRoom.addEnemies(cyrusRobotEnemy);


          bayviewGlenArtRoom.setRunnable(() -> {
            Graphics text = new Graphics();
          if (!hasEnteredArtRoom[0]) {
           try {
            text.slowTextSpeed("Ring Ring \nRing Ring \nClick\n > Cyrus: Hey recruit, There is something I forgot to have mentioned. I see you are entering the Art Room and I forgot to tell you that the final peice of my locker combonation is protected by my own killer robot... but uhh hey no problem for you right, go ahead and get that last code!", 20);
            text.slowTextSpeed("...", 2000);
            text.slowTextSpeed("WHO-DARES-DISTURB-ME-AND-THE-MIGHTY-CYRUS", 50);
            text.slowTextSpeed("ELIMINATE, ELIMINATE", 50);
            Fight f = new Fight(cyrusRobotEnemy);
            boolean won = f.fight();
            if (won) {
              text.slowTextSpeed("NOOOOOOOOOOOOOOOOOOOOOOOOO- \nCyrus Robot Was Defeated", 50);
              text.slowTextSpeed("You turn on the lights to read #3 - 17 written on the board behind you", 50);
              hasEnteredArtRoom[0] = true;
            }else{
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
            }
          } catch (Exception e) {
            
           }
          } else {
            try {
              text.slowTextSpeed("You turn on the lights to read #3 - 17 written on the board behind you", 50);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });

        bayviewGlenG11CommonArea.setRunnable(() -> {
          if (bayviewGlen1stFloorBelowG11CommonAreaExitDown.getIsExitLocked()) {
            for (Item i : Game.getGame().getPlayer().getInventory().getItems()) {
              if (i.getName().equals("Basement Key")) {
                if (i.getKeyId().equals("BasementBayviewGlenKey")) {
                  Graphics text = new Graphics();
                  bayviewGlen1stFloorBelowG11CommonAreaExitDown.setIsExitLocked(false);
                  try {
                    text.slowTextSpeed("You use the rat's key to unlock the bottom door", 20);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              }
            }
          }
        });

        Inventory gymGuy = new Inventory(6);
        gymGuy.addItem(new Weapon(3, "Fist", false, 15, null));
        final Enemy gymGuyEnemy = new Enemy(null, bayviewGlenWeightRoom, 80, gymGuy , 10, "Gym Guy", 150);
        bayviewGlenWeightRoom.enemies.add(gymGuyEnemy);
 
        bayviewGlenWeightRoom.setRunnable(() -> {
         try {
           bayviewGlenWeightRoom.printAscii();
           Scanner in = new Scanner(System.in);
           Graphics text = new Graphics();
           if (bayviewGlenWeightRoom.enemies.contains(gymGuyEnemy)) {
             text.slowTextSpeed("You see a Guy benching 2 plates. Behind him you see a big green button, do you press it? Y/N: ", 20);
             String ans = in.nextLine();
             if(ans.equalsIgnoreCase("y")) {
               text.slowTextSpeed("You go to press the button, \n Gym guy - HEY, you wanna press that button you gotta get through me \n You - Try me", 20);
               Fight f = new Fight(gymGuyEnemy);
               boolean won = f.fight();
               if(won) {
                 bayviewGlenWeightRoom.enemies.remove(gymGuyEnemy);
                 game.getGame().getPlayer().getInventory().addItem(new Weapon(5, "Dumbell", false, 30, null));
               }else{
                Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
               }
               text.slowTextSpeed("You press the button, you hear a big clunk downstairs.", 20);
               artRoomLightsOn[0] = true;
             } else {
               return;
             }
           }
         } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         }
 
      });
       
      // PICKERING AREA ROOMS

      final Room pickeringKingstonAndRougemount = new Room ("You leave the go bus and step into the greatest city in Canada: Pickering!", "pickeringkingstonandrougemount"); roomMap.put(pickeringKingstonAndRougemount.getRoomName(), pickeringKingstonAndRougemount);
      
      final Room pickeringCircleK = new Room ("You enter the beautiful pickering circle K", "pickeringcirclek"); roomMap.put(pickeringCircleK.getRoomName(), pickeringCircleK);
      boolean[] boughtPrime4 = {false};
      pickeringCircleK.setRunnable(new Runnable(){
        public void run(){
          try {
            Graphics text = new Graphics();
            Scanner in = new Scanner(System.in);
            text.slowTextSpeed("Welcome to beautiful Pickering, can I help you today? y/n", 7);
            String a = in.nextLine();
            if(a.equalsIgnoreCase("y")){
              boolean finishedOrder = false;
                while(!finishedOrder){
                  if(Game.getGame().getPlayer().getMoney() >= 1){
                    text.slowTextSpeed("What would you like to buy?", 7);
                    text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                    text.slowTextSpeed("> Slim Jim - 2$", 7);
                    text.slowTextSpeed("> Mike and Ike - 3$", 7);
                    if(Game.getGame().getPlayer().getPrimeCounter() == 7 && !boughtPrime4[0]) {
                      text.slowTextSpeed("> PRIME - 10$", 7);
                    }
                    String b = in.nextLine().toLowerCase();
                    double pMoney = Game.getGame().getPlayer().getMoney();
                    if(b.contains("slim jim")){
                        if((pMoney - 4)>= 0){
                          Game.getGame().getPlayer().setMoney(pMoney-=2);
                          Game.getGame().getPlayer().getInventory().addItem(
                            new Item(2, "Slim Jim", false, 
                            new Effect("Health up", 1, 0, 3, 10), false)
                            );
                            finishedOrder = true;
                        }else{
                          text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                          break;
                        }
                    }else if(b.contains("mike and ike")){
                        if((pMoney - 3)>= 0){
                          Game.getGame().getPlayer().setMoney(pMoney-=3);
                          Game.getGame().getPlayer().getInventory().addItem(
                            new Item(2, "Mike and Ike", false, 
                            new Effect("Health Up", 0, 0, 0, 8), false)
                            );
                            finishedOrder = true;
                        }else{
                          text.slowTextSpeed("Im Sorry, Your too broke", 7); //CHANGE LATER
                          break;
                        }
                    }else if(b.contains("prime") && Game.getGame().getPlayer().getPrimeCounter() == 7 && !boughtPrime4[0]){
                        if((pMoney - 10)>= 0){
                          Game.getGame().getPlayer().setMoney(pMoney-=10);
                          Game.getGame().getPlayer().getInventory().addItem(new Prime(1, "META MOON PRIME", false, "Blue", true, "circlek"));
                          eglintonSubway.setLocked(false);
                          boughtPrime4[0] = true;

                            
                            finishedOrder = true;

                        }else{
                          
                          text.slowTextSpeed("Im Sorry, Your too broke LMAO", 7); //CHANGE LATER
                          break;
                        }
                    }else{
                      
                      
                        text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                      
                      
                    }
                  }else{
                    System.out.println("YOU HAVE NO MONEY. YOURE TOO POOR FOR THE DON");
                    finishedOrder = false;
                    break;
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
  
          }
        }
    });
      // PICKERING EXITS

      final Exit pickeringCameronsHouseExitNorth = new Exit("N",pickeringCameronsHouse); pickeringKingstonAndRougemount.addExit(pickeringCameronsHouseExitNorth);
      final Exit pickeringCircleKExitEast = new Exit("E",pickeringCircleK); pickeringKingstonAndRougemount.addExit(pickeringCircleKExitEast);
      final Exit pickeringKingstonAndRougemountExitSouth = new Exit("S",pickeringKingstonAndRougemount); pickeringCameronsHouse.addExit(pickeringKingstonAndRougemountExitSouth);
      final Exit pickeringKingstonAndRougemountExitWest = new Exit("W",pickeringKingstonAndRougemount); pickeringCircleK.addExit(pickeringKingstonAndRougemountExitWest); 

      //YORK MILLS AREA EXITS

      final Exit yorkMillsSubwayHallwayExitDown = new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown);
      final Exit pickeringKingstonAndRougemountExitNorth = new Exit("N", pickeringKingstonAndRougemount); yorkMillsBusTerminal.addExit(pickeringKingstonAndRougemountExitNorth);
      final Exit yorkMillsBusTerminalExitSouth = new Exit("S", yorkMillsBusTerminal); pickeringKingstonAndRougemount.addExit(yorkMillsBusTerminalExitSouth);
      final Exit yorkMillsBusSubwayHallwayExitNorth = new Exit("N", yorkMillsSubwayHallway); yorkMillsSubway.addExit(yorkMillsBusSubwayHallwayExitNorth);
      final Exit yorkMillsBusTerminalExitUp = new Exit("U", yorkMillsBusTerminal); yorkMillsSubwayHallway.addExit(yorkMillsBusTerminalExitUp);
      final Exit gatewayNewsstandsExitEast = new Exit("E", gatewayNewsstands); yorkMillsSubwayHallway.addExit(gatewayNewsstandsExitEast);
      final Exit yorkMillsSubwayExitSouth = new Exit("S", yorkMillsSubway); yorkMillsSubwayHallway.addExit(yorkMillsSubwayExitSouth);
      final Exit yorkMillsSubwayHallwayExitWest = new Exit("W", yorkMillsSubwayHallway); gatewayNewsstands.addExit(yorkMillsSubwayHallwayExitWest);
      final Exit facultyRoomExitWest = new Exit("W", facultyRoom); yorkMillsSubwayHallway.addExit(facultyRoomExitWest);
      final Exit yorkMillsSubwayHallwayExitEast = new Exit("E", yorkMillsSubwayHallway); facultyRoom.addExit(yorkMillsSubwayHallwayExitEast);
      final Exit eglintonShuttleBusExitWest = new Exit("W", eglintonShuttleBus); yorkMillsBusTerminal.addExit(eglintonShuttleBusExitWest);
      final Exit yorkMillsBusTerminalExitEast = new Exit("E", yorkMillsBusTerminal); eglintonShuttleBus.addExit(yorkMillsBusTerminalExitEast);
      final Exit eglintonBusStopExitSouth = new Exit("S", eglintonBusStop); eglintonShuttleBus.addExit(eglintonBusStopExitSouth);
      final Exit yorkMillsBusExitEast = new Exit("E", yorkMillsBus); yorkMillsBusTerminal.addExit(yorkMillsBusExitEast);
      final Exit yorkMillsBusTerminalExitWest = new Exit("W", yorkMillsBusTerminal); yorkMillsBus.addExit(yorkMillsBusTerminalExitWest);
      final Exit bayviewGlenYorkMillsExitEast = new Exit("E", bayviewGlenYorkMills); yorkMillsBus.addExit(bayviewGlenYorkMillsExitEast);
      final Exit yorkMillsBusExitWest = new Exit("W", yorkMillsBus); bayviewGlenYorkMills.addExit(yorkMillsBusExitWest);
      final Exit bayviewGlenDeckExitNorth = new Exit("N", bayviewGlenDeck); bayviewGlenYorkMills.addExit(bayviewGlenDeckExitNorth);
      final Exit donMillsExitEast = new Exit("E",yorkMillsAndDonMills); bayviewGlenYorkMills.addExit(donMillsExitEast);
      final Exit bayviewGlenExitWest = new Exit("W",bayviewGlenYorkMills); yorkMillsAndDonMills.addExit(bayviewGlenExitWest);
      final Exit donMillsStationExitNorth = new Exit("N",donMillsSubway); yorkMillsAndDonMills.addExit(donMillsStationExitNorth);
      final Exit donMillsAndYorkMillsExitSouth = new Exit("S",yorkMillsAndDonMills); donMillsSubway.addExit(donMillsAndYorkMillsExitSouth);
      final Exit donMillsSheppardExitWest = new Exit("W",sheppardYongeLine4); donMillsSubway.addExit(donMillsSheppardExitWest);
      final Exit sheppardYongeExitEast = new Exit("E",donMillsSubway); sheppardYongeLine4.addExit(sheppardYongeExitEast);

      // SHEPPARD YONGE EXITS

      final Exit sheppardYongeLine1ExitNorth = new Exit("N", sheppardYongeLine4StreetHallway);
      sheppardYongeLine4.addExit(sheppardYongeLine1ExitNorth);
      final Exit sheppardYongeLine1ExitWest = new Exit("W", sheppardYongeLine1HallwayBeforeStreet);
      sheppardYongeLine1.addExit(sheppardYongeLine1ExitWest);
      final Exit sheppardYongeHallwayB4StreetExitWest = new Exit("W", sheppardYongeSecretRoom);
      sheppardYongeLine4StreetHallway.addExit(sheppardYongeHallwayB4StreetExitWest);
      final Exit sheppardYongeLine4StreetHallwayExitSouth = new Exit("S", sheppardYongeLine4);
      sheppardYongeLine4StreetHallway.addExit(sheppardYongeLine4StreetHallwayExitSouth);
      final Exit sheppardYongeSecretRoomExitEast = new Exit("E", sheppardYongeLine4StreetHallway);
      sheppardYongeSecretRoom.addExit(sheppardYongeSecretRoomExitEast);
      final Exit sheppardYongeLine4ExitDown = new Exit("D", sheppardYongeLine1);
      sheppardYongeLine4.addExit(sheppardYongeLine4ExitDown);
      final Exit sheppardYongeLine1ExitUp = new Exit("U", sheppardYongeLine4);
      sheppardYongeLine1.addExit(sheppardYongeLine1ExitUp);
      


      //ELLESMERE AREA EXITS
      final Exit ellesmereSubwayExitEast = new Exit("E", ellesmereSubway); bloorYongeLine2.addExit(ellesmereSubwayExitEast);
      final Exit bloorYongeLine2ExitWest = new Exit("W", bloorYongeLine2); ellesmereSubway.addExit(bloorYongeLine2ExitWest);
      final Exit sketchyStreetCornerExitEast = new Exit("E", sketchyStreetCorner); ellesmereStationUnderground.addExit(sketchyStreetCornerExitEast);
      final Exit ellesmereStationUndergroundExitWest = new Exit("W", ellesmereStationUnderground); sketchyStreetCorner.addExit(ellesmereStationUndergroundExitWest);
      final Exit krispyKremeExitEast = new Exit("E", krispyKreme); sketchyStreetCorner.addExit(krispyKremeExitEast);
      final Exit sketchyStreetCornerExitWest = new Exit("W", sketchyStreetCorner); krispyKreme.addExit(sketchyStreetCornerExitWest);
      final Exit ellesmereStationUndergroundExitUp = new Exit("U", ellesmereStationUnderground); ellesmereSubway.addExit(ellesmereStationUndergroundExitUp);
      final Exit ellesmereSubwayExitDown = new Exit("D", ellesmereSubway); ellesmereStationUnderground.addExit(ellesmereSubwayExitDown);
      
      
      
      
      
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
      final Exit eglintonShuttleBusExitNorth = new Exit("N", eglintonShuttleBus); yorkMillsShuttleBus.addExit(eglintonShuttleBusExitNorth);
      final Exit yorkMillsSubwayExitNorth = new Exit("N", yorkMillsSubway); eglintonSubway.addExit(yorkMillsSubwayExitNorth);
      final Exit eglintonSubwayExitSouth = new Exit("S", eglintonSubway); yorkMillsSubway.addExit(eglintonSubwayExitSouth);

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
      final Exit stClairWestSubwayExitDown = new Exit("D", stClairWestSubway); stClairWestStation.addExit(stClairWestSubwayExitDown);
      final Exit stClairWestStationExitUp = new Exit ("U", stClairWestStation); stClairWestSubway.addExit(stClairWestStationExitUp);

      



      



      
      

      // final Exit  = new Exit("", ); .addExit();


      
     //ROOM LEGEND
     //final Room (room name) = new Room("description", room name all lowercase); roomMap.put(room name.getRoomName(), room name);
     // final Room  =  new Room("", ""); roomMap.put( .getRoomName(), );

     // EXIT LEGEND
     // final exit (destinationRoomExit(direction current room exits to) = new Exit(direction, destinationRoom); currentRoom.addExit(destinationRoomExit(direction current room exit to))
     // final exit  = new Exit("", );  .addExit( );

     
      //Union
        //unionPlatform code
        final Room unionPlatform = new Room ("You look around you and see a stairway leading upwards. It looks like its the only way forward", "unionplatform"); roomMap.put(unionPlatform.getRoomName(), unionPlatform);
        
        
        
        //unionShopArea
        final Room unionShopArea = new Room ("The shopping area in union seems desolated with most of the shops closed. \n However to the east you see a Tim Hortons cafe and another room to the north", "unionshoparea"); roomMap.put(unionShopArea.getRoomName(), unionShopArea);
          
          unionShopArea.setRunnable(new Runnable(){

                boolean[] fightDone = {false};
                @Override
                public void run() {
                  if(!fightDone[0]){
                    Inventory i = new Inventory(2600);
                    i.addItem(new Weapon(12,"Crow bar", false, 10, null));
                    final Enemy PRIME_THEIF = new Enemy(null, unionShopArea, 50, i, 20, "Prime Theif", 8);
                    unionShopArea.addEnemies(PRIME_THEIF); 
                    try { 
                      
                      Fight f = new Fight(PRIME_THEIF);
                      Graphics text = new Graphics();
                      text.slowTextSpeed("HEY YOU... I heard that your on a prime quest so i know you have some. NOW GIVE IT TOO ME!!!!", 12);
                      boolean won = f.fight();
                      if(won) {
                          Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(PRIME_THEIF);
                          fightDone[0] = true;
                      }else{
                        Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
                      }
                    } catch (Exception e) {
                      // TODO: handle exception
                    }
                  }
                }

             
            });

        //unionTimHortons
        final Room unionTimHortons = new Room ("You are standing in a beautiful area full of people waiting in line for their tims", "uniontimhortons"); roomMap.put(unionTimHortons.getRoomName(), 
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
                          if(Game.getGame().getPlayer().getMoney() >= 1){
                            text.slowTextSpeed("What would you like to buy?", 7);
                            text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                            text.slowTextSpeed("> Boston Cream Donut - 4$", 7);
                            text.slowTextSpeed("> French Vanilla - 2$", 7);
                            text.slowTextSpeed("> Toasted Bagel With Cream Cheese - 7$", 7);
                            String b = in.nextLine().toLowerCase();
                            double pMoney = Game.getGame().getPlayer().getMoney();
                            if(b.contains("boston cream donut")){
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
                            }else if(b.contains("french vanilla")){
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
                            }else if(b.contains("toasted bagel with cream cheese")){
                                if((pMoney - 7)>= 0){
                                  Game.getGame().getPlayer().setMoney(pMoney-=7);
                                  Game.getGame().getPlayer().getInventory().addItem(
                                    new Item(2, "Toasted Bagel With Cream Cheese", false, 
                                    new Effect("Super Delicous", 0, 0, -8, 30), false)
                                    );
                                    finishedOrder = true;
                                }else{
                                  
                                  text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                }
                            }else{
                              
                              
                                text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                              
                              
                            }
                          } else {
                            finishedOrder = false;
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
        final Room unionMainArea = new Room ("The main area of union seems desolate today.. maybe people are getting ready for the KSI fight.", "unionmainarea"); roomMap.put(unionMainArea.getRoomName(), unionMainArea);
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
              Inventory rogueEconTest = new Inventory(10);
       rogueEconTest.addItem(new Weapon(3, "MC=MR < ATC", false, 0, new Effect("Making A Loss in the Short Run", 3, 10, 0, 0)));
       rogueEconTest.addItem(new Weapon(3, "Supply and Demand Equilibrium", false, 15, null));
       rogueEconTest.addItem(new Weapon(3, "Unregulated Natural Monopoly", false, 10, new Effect("Extreme Inneficiency", 4, 5, -2, 0)));
       final Enemy rogueEconTestEnemy = new Enemy(null, bayviewGlen2ndFloorUpperHallway, 75, rogueEconTest , 0, "Rogue Econ Test", 94);
       bayviewGlen2ndFloorUpperHallway.addEnemies(rogueEconTestEnemy);
       bayviewGlen2ndFloorUpperHallway.setRunnable(() -> {
         try {
           if(bayviewGlen2ndFloorUpperHallway.enemies.contains(rogueEconTestEnemy)) {
             Fight f = new Fight(rogueEconTestEnemy);
             boolean won = f.fight();
             if(won) {
               bayviewGlen2ndFloorUpperHallway.enemies.remove(rogueEconTestEnemy);
             }else{
              Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
             }
           }
           if (bayviewGlenG12CommonAreaExitDown.getIsExitLocked()) {
            for (Item i : Game.getGame().getPlayer().getInventory().getItems()) {
              if (i.getName().equals("Basement Key")) {
                if (i.getKeyId().equals("BasementBayviewGlenKey")) {
                  Graphics text = new Graphics();
                  bayviewGlenG12CommonAreaExitDown.setIsExitLocked(false);
                  text.slowTextSpeed("You take the time to unlock the door on the way down", 20);
                }
              }
            }
          }
         } catch (Exception e) {
           e.printStackTrace();
         }
       });
        //union Corner
        final Room unionCorner = new Room ("This corner reeks of urine and regret", "unioncorner"); roomMap.put(unionCorner.getRoomName(), unionCorner);
      
          
          unionCorner.setRunnable(new Runnable(){
            
            ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
            @Override
            public void run() {
              try{
                Scanner in = new Scanner(System.in);
                Graphics text = new Graphics();
                text.slowTextSpeed("You get approached by a gambling addict", 7);
                text.slowTextSpeed("Hey kid, wanna try your luck in blackjack? y/n", 7);
                String d = in.nextLine();
                if(d.equalsIgnoreCase("y")){
                  BlackJack n = new BlackJack();
                  n.play();
                }else{
                  text.slowTextSpeed("alright, your loss...", 20);
                }
              }catch (Exception e){
                //ajsdasda
              }
              
            }
          
        });
          
        //scams Market
        final Room unionScamsMarket = new Room ("As you enter the store you feel the money in your wallet disappear", "unionscamsmarket"); roomMap.put(unionScamsMarket.getRoomName(), unionScamsMarket);
        unionScamsMarket.setRunnable(new Runnable(){
         
          @Override
          public void run() {
            try{
            ArrayList<Item> ar = Game.getGame().getPlayer().getInventory().getItems();
            boolean hasCoupon = false;
            Graphics text = new Graphics();
            text.slowTextSpeed("ShopKeeper: I heard about your little prime quest... I'll give you my only prime for a BAJILLION DOLLARS HAHAHAH", 7);
              for(int i = 0; i<ar.size(); i++){
                  if(ar.get(i).getName().equals("Free Prime Coupon"))
                    hasCoupon = true;
                  
              }
              if(hasCoupon){
                Inventory i = new Inventory(2600);
                i.addItem(new Weapon(12,"Metal Bat", false, 40, new Effect("Concussion", 4, 0, -2, 0)));
                final Enemy SHOPKEEPER = new Enemy(null, unionScamsMarket, 150, i, 80, "ShopKeeper", 200);
                Fight f = new Fight(SHOPKEEPER);
                text.slowTextSpeed("WHAT IS THAT, A FREE PRIME COUPON??!?!? YOU SCAMMER THATS MY JOB!", 7);
                boolean won = f.fight();
                
                if(won){
                  Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(SHOPKEEPER);
                  text.slowTextSpeed("As the shopkeeper falls, you see a prime and fall out of his pocket and onto the ground. He also drops his metal bat", 7);
                  Game.getGame().getPlayer().getInventory().addItem(new Prime(0, "Lemonade Prime", false, "Yellow", false, "unionscamsmarket"));
                  unionScamsMarket.addItemGround(new Weapon(12,"Metal Bat", false, 18, new Effect("Concussion", 4, 0, -2, 0)));
                }else{
                  Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
                }
              }
            }catch(Exception e){
              //aklsdjalsd
            }
          }
        });
        //hallway
        final Room unionHallway = new Room ("A narrow hallway leading to the washrooms", "unionhallway"); roomMap.put(unionHallway.getRoomName(), unionHallway);
        //Guide room


        //washroom
        final Room unionWashroom = new Room ("A suprisingly clean washroom for a major subway station. to the east, you see a perculiar room", "unionwashroom"); roomMap.put(unionWashroom.getRoomName(), unionWashroom);
          boolean[] s = {false};
          unionWashroom.setRunnable(new Runnable(){
          
            @Override
            public void run() {
              try{
                
              if(!s[0]){
              Graphics text = new Graphics();
              text.slowTextSpeed("On the ground you spot a 20$ bill and pick it up... sweet", 0);
              Game.getGame().getPlayer().setMoney(Game.getGame().getPlayer().getMoney() + 20);
                s[0] = true;
              }
            
              }catch(Exception e){
                //aklsdjalsd
              }
            }
          });
        //sinkRoom
        final Room unionSinkRoom = new Room ("There is a lone sink in the corner, how depressing", "unionsinkroom"); roomMap.put(unionSinkRoom.getRoomName(), unionSinkRoom);
          unionSinkRoom.setRunnable(new Runnable(){
          
            @Override
            public void run() {
              try{
              Graphics text = new Graphics();
              text.slowTextSpeed("as you gaze at the lonely sink in the corner,\n you feel like playing a good game of soccer with cyrus when this is all over", 7);

            
              }catch(Exception e){
                //aklsdjalsd
              }
            }
          });

        //faculty closet
        final Room unionFacultyCloset = new Room ("You feel the walls closing in, in this cramped room.", "unionfacultycloset", true, "is locked, prob need a key"); roomMap.put(unionFacultyCloset.getRoomName(), unionFacultyCloset);
        unionFacultyCloset.addItemGround(new Item(2, "Free Prime Coupon", false, null, false));
        //faculty room
        final Room unionFacultyRoom = new Room ("a nice office where ttc works are safe, until now i guess", "unionfacultyroom"); roomMap.put(unionFacultyRoom.getRoomName(), unionFacultyRoom);
        boolean[] e = {false};
        unionFacultyRoom.setRunnable(new Runnable(){
          
          @Override
          public void run() {
            try{
              if(!e[0]){
              Graphics text = new Graphics();
              text.slowTextSpeed("TTC worker: HEY YOUR NOT SUPPOSE TO BE HERE!", 7);
                  Inventory i = new Inventory(2600);
                  i.addItem(new Weapon(12,"Broom", false, 8, null));
                  final Enemy TTCWORKER = new Enemy(null, unionFacultyRoom, 30, i, 20, "ShopKeeper", 25);
                  Fight f = new Fight(TTCWORKER);
                  boolean won = f.fight();
                  if(won){
                    Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(TTCWORKER);
                    text.slowTextSpeed("The innocent TTC worker falls to the ground defeated", 7);
                    text.slowTextSpeed("You notice a key falling from his pocket to the ground", 7);
                    unionFacultyRoom.addItemGround(new Item(2, "Closet Key", false, null, false));
                    e[0] = true;
                  }else{
                    Game.getGame().getPlayer().setCurrentRoom(pickeringCameronsHouse);
                  }
                 
              }

          
            }catch(Exception e){
              //aklsdjalsd
            }
          }
        });

        //Maintenance room
        final Room unionMaintenanceRoom = new Room ("A dusty room which makes sense, \n to the north you see a closet and to the east a staff room", "unionmaintenanceroom"); roomMap.put(unionMaintenanceRoom.getRoomName(), unionMaintenanceRoom);
        unionMaintenanceRoom.setRunnable(new Runnable(){
            
          @Override
          public void run() {
            try{
              boolean hasKey = false;
              ArrayList<Item> arr = Game.getGame().getPlayer().getInventory().getItems();
              for(int i = 0; i<arr.size(); i++){
                if(arr.get(i).getName().equalsIgnoreCase("Closet Key")){
                  hasKey = true;
                }
              }
              Graphics text = new Graphics();
              if(hasKey){
                text.slowTextSpeed("You rush to the closet door in the room, key in hand", 7);
                text.slowTextSpeed("Using the key, you unlock the door", 7);
                unionFacultyCloset.setLocked(false);
                for(int i = 0; i<arr.size(); i++){
                  if(arr.get(i).getName().equalsIgnoreCase("Closet Key")){
                    arr.remove(i);
                  }
                }
                hasKey = false;
              }
              

          
            }catch(Exception e){
              //aklsdjalsd
            }
          }
        });

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
        final Exit unionWashroomExitNorth = new Exit("N",unionWashroom); unionHallway.addExit(unionWashroomExitNorth); 
        final Exit unionHallwayExitSouth = new Exit("S",unionHallway); unionWashroom.addExit(unionHallwayExitSouth); 
        final Exit unionSinkRoomExitEast = new Exit("E",unionSinkRoom); unionWashroom.addExit(unionSinkRoomExitEast);
        final Exit unionWashroomExitWest = new Exit("W",unionWashroom); unionSinkRoom.addExit(unionWashroomExitWest);
        final Exit unionMainAreaExitDown2 = new Exit("D",unionMainArea); unionMaintenanceRoom.addExit(unionMainAreaExitDown2);
        final Exit unionFacultyClosetExitNorth = new Exit("N",unionFacultyCloset); unionMaintenanceRoom.addExit(unionFacultyClosetExitNorth);
        final Exit unionFacultyRoomExitEast = new Exit("E",unionFacultyRoom); unionMaintenanceRoom.addExit(unionFacultyRoomExitEast); 
        final Exit unionMaintenanceRoomExitSouth = new Exit("S",unionMaintenanceRoom); unionFacultyCloset.addExit(unionMaintenanceRoomExitSouth);
        final Exit unionMaintenanceRoomExitWest = new Exit("W",unionMaintenanceRoom); unionFacultyRoom.addExit(unionMaintenanceRoomExitWest);
        final Exit unionPlatFormExitSouth = new Exit("S", unionPlatform); stClairWestSubway.addExit(unionPlatFormExitSouth);
        final Exit stClairWestSubwayExitWest = new Exit("W", stClairWestSubway); unionPlatform.addExit(stClairWestSubwayExitWest);


        //UK Rooms
        final boolean[] hasBeatenKsi = new boolean[]{false};
        final Room UKOutsideKsiHouse = new Room ("You stare at the huge house of KSI", "ukoutsideksihouse"); roomMap.put(UKOutsideKsiHouse.getRoomName(), UKOutsideKsiHouse);
        final Room ksiHouse = new Room ("You finally stop falling and wake up in Japan", "ksihouse"); roomMap.put(ksiHouse.getRoomName(), ksiHouse);

        Inventory KSI1ndForm = new Inventory(1000);
        KSI1ndForm.addItem(new Weapon(10, "Prime Branded Sword", false, 15, null));
        KSI1ndForm.addItem(new Weapon(25, "Prime Branded Gun", false, 20, new Effect("Bullet Wound", 3, 10, 10, 0)));

        Inventory KSI2ndForm = new Inventory(1000);
        KSI2ndForm.addItem(new Weapon(10, "Meta Moon Prime", false, 10, new Effect("Moon Sickness", 2, 20, 0, 0)));
        KSI2ndForm.addItem(new Weapon(25, "Ice Pop Prime", false, 25, new Effect("Frozen", 3, 0, 100, 0)));
        KSI2ndForm.addItem(new Weapon(30, "Orange Prime", false, 30, null));
        KSI2ndForm.addItem(new Weapon(35, "Blue Rasperry Prime", false, 35, null));
        KSI2ndForm.addItem(new Weapon(20, "Grape Prime", false, 20, new Effect("Medicinal Taste", 3, 15, 0, 0)));
        KSI2ndForm.addItem(new Weapon(10, "Lemon Lime Prime", false, 10, new Effect("Lemon Acid", 10, 5, 0, 0)));
        KSI2ndForm.addItem(new Weapon(30, "Strawberry Watermelon Prime", false, 30, null));
        KSI2ndForm.addItem(new Weapon(40, "Tropical Punch Prime", false, 40, null));

        final Enemy KSI1ndFormEnemy = new Enemy(null, ksiHouse, 150, KSI1ndForm , 0, "KSI", 0);
        final Enemy KSI2ndFormEnemy = new Enemy(null, ksiHouse, 500, KSI2ndForm , 0, "KSI-PRIME", 0);
       bayviewGlen2ndFloorUpperHallway.addEnemies(rogueEconTestEnemy);
        

        unionPlatform.setRunnable(() -> {
          if(Game.getGame().getPlayer().getPrimeCounter() == 8) {
           Graphics text = new Graphics();
           Scanner in = new Scanner(System.in);
           try {
             renderer.showCutScene(1500, "\\bin\\zork\\data\\uniongobuscyruscall.txt", 15);
             text.slowTextSpeed("No going back now, would you like to leave for pearson? Y/N:", 50);
             String ans = in.nextLine(); 
             if(ans.equalsIgnoreCase("y")) {
               text.slowTextSpeed("Alright, lets get this show on the road", 20);
               renderer.showCutScene(1500, "\\bin\\zork\\data\\gotoukcutscene.txt", 15);
               Game.getGame().getPlayer().setCurrentRoom(UKOutsideKsiHouse);
               UKOutsideKsiHouse.printAscii();
               
             }
           } catch (Exception Exception) {
             
           }
          }
 
 
         });

        final Exit unionPlatformExitSouthTwo = new Exit("S",unionPlatform); UKOutsideKsiHouse.addExit(unionPlatformExitSouthTwo);
        final Exit ksiHouseExitNorth = new Exit("N",ksiHouse); UKOutsideKsiHouse.addExit(ksiHouseExitNorth);
        final Exit UKOutsideKsiHouseExitSouth = new Exit("S",UKOutsideKsiHouse); ksiHouse.addExit(UKOutsideKsiHouseExitSouth);

        //MISCELLANEOUS ROOMS EXITS
      final Exit summerhillSubwayExitSouth = new Exit("S", summerhillSubway); stClairSubway.addExit(summerhillSubwayExitSouth);
      final Exit stClairSubwayExitNorth = new Exit("N", stClairSubway); summerhillSubway.addExit(stClairSubwayExitNorth);
      final Exit dundassSubwayExitNorth = new Exit("N", dundasSubway); unionPlatform.addExit(dundassSubwayExitNorth);
      final Exit unionPlatformExitSouth = new Exit("S", unionPlatform); dundasSubway.addExit(unionPlatformExitSouth);
      final Exit bloorYongeSubwayExitNorth = new Exit("N", bloorYongeSubway); dundasSubway.addExit(bloorYongeSubwayExitNorth);
      final Exit dundasSubwayExitSouth = new Exit("S", dundasSubway); bloorYongeSubway.addExit(dundasSubwayExitSouth);
      final Exit lawrenceSubwayExitNorth = new Exit("N", lawrenceSubway); bloorYongeSubway.addExit(lawrenceSubwayExitNorth);
      final Exit bloorYongeSubwayExitSouth = new Exit("S", bloorYongeSubway); lawrenceSubway.addExit(bloorYongeSubwayExitSouth);
      final Exit yongeSheppardLine1ExitNorth = new Exit("N", sheppardYongeLine1); lawrenceSubway.addExit(yongeSheppardLine1ExitNorth);
      final Exit lawrenceSubwayExitSouth = new Exit("S", lawrenceSubway); sheppardYongeLine1.addExit(lawrenceSubwayExitSouth);
      final Exit bloorYongeStationExitUp = new Exit("U", bloorYongeStation); bloorYongeSubway.addExit(bloorYongeStationExitUp);
      final Exit bloorYongeSubwayExitDown = new Exit("D", bloorYongeSubway); bloorYongeStation.addExit(bloorYongeSubwayExitDown);
      final Exit bloorYongeLine2ExitEast = new Exit("E", bloorYongeLine2); bloorYongeStation.addExit(bloorYongeLine2ExitEast);
      final Exit bloorYongeStationExitWest = new Exit("W", bloorYongeStation); bloorYongeLine2.addExit(bloorYongeStationExitWest);


          //Tokyo Japan
          final Room HoleInTheGroundJapan = new Room ("Around you are large buildings yet no people. I wonder where they are?", "holeinthegroundjapan"); roomMap.put(HoleInTheGroundJapan.getRoomName(), HoleInTheGroundJapan);
          final Room japanNationalStadiumPlaza = new Room ("Another empty area, at least theres a tims to the east, you should probobly stock up. also to the north is a stadium where you can hear people cheering", "japannationalstadiumplaza"); roomMap.put(japanNationalStadiumPlaza.getRoomName(), japanNationalStadiumPlaza);
          japanNationalStadiumPlaza.addItemGround(new Item(11, "test object", false, null, false));
          final Room japanTimHortons = new Room ("A very nice tim hortons no good food though", "japantimhortons"); roomMap.put(japanTimHortons.getRoomName(), japanTimHortons);
                  japanTimHortons.setRunnable(new Runnable(){
                    public void run(){
                      try {
                        Graphics text = new Graphics();
                        Scanner in = new Scanner(System.in);
                        text.slowTextSpeed("Konichiwa! Welcome to Tim Hortons, Would you like to buy anything? y/n", 7);
                        String a = in.nextLine();
                        if(a.equalsIgnoreCase("y")){
                          boolean finishedOrder = false;
                            while(!finishedOrder){
                              if(Game.getGame().getPlayer().getMoney() >= 6){
                                text.slowTextSpeed("What would you like to buy?", 7);
                                text.slowTextSpeed("You have " + Game.getGame().getPlayer().getMoney() + "$" , 0);
                                text.slowTextSpeed("> Runny Nutella Cookie - 10$", 7);
                                text.slowTextSpeed("> Donut Sushi - 6$", 7);
                                text.slowTextSpeed("> Toasted Bagel With Cream Cheese - 15$", 7);
                                String b = in.nextLine().toLowerCase();
                                double pMoney = Game.getGame().getPlayer().getMoney();
                                if(b.contains("runny nutella cookie")){
                                    if((pMoney - 10)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=10);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Runny Nutella Cookie", false, 
                                        new Effect("Health up + Sugar Rush", 0, 0, 4, 35), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                    }
                                }else if(b.contains("donut sushi")){
                                    if((pMoney - 6)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=6);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Donut Sushi", false, 
                                        new Effect("Health Up", 0, 0, 0, 18), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                    }
                                }else if(b.contains("toasted bagel with cream cheese")){
                                    if((pMoney - 15)>= 0){
                                      Game.getGame().getPlayer().setMoney(pMoney-=15);
                                      Game.getGame().getPlayer().getInventory().addItem(
                                        new Item(2, "Toasted Bagel With Cream Cheese", false, 
                                        new Effect("Super Delicous", 0, 0, -2, 60), false)
                                        );
                                        finishedOrder = true;
                                    }else{
                                      
                                      text.slowTextSpeed("Im Sorry, Your too broke for tims LMAO", 7); //CHANGE LATER
                                    }
                                }else{
                                  
                                  
                                    text.slowTextSpeed("Sorry, That item is not avaliable right now. Please pick another", 7);
                                  
                                  
                                }
                              } else {
                                text.slowTextSpeed("Im sorry, you cant afford anything on the menu", 0);
                                finishedOrder = false;
                                break;
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
          final Room japanNationalStadium = new Room ("Your final fight, goodluck", "japannationalstadium"); roomMap.put(japanNationalStadium.getRoomName(), japanNationalStadium);
              japanNationalStadium.setRunnable(new Runnable(){
                
                @Override
                public void run() {
                  try{
                    Graphics text = new Graphics();
                    text.slowTextSpeed("So, you finally arrive, i've been expecting you " + Game.getGame().getPlayer().getName(), 7);
                    text.slowTextSpeed("to get my final prime, you have to kill me for it", 7);
                    text.slowTextSpeed("Time for your final fight", 60);
                    Inventory t = new Inventory(12020);
                    t.addItem(new Weapon(0,"Boxing Gloves", false, 45, null));
                    t.addItem(new Weapon(0, "Bad podcast", false, 25, new Effect("Embaressement", 2, 10, 0, 0)));
                    t.addItem(new Weapon(0, "Horrible diss track", false, 20, new Effect("Bleeding Ears", 4, 8, 2, 0)));
                    final Enemy LOGAN_PAUL = new Enemy(null, japanNationalStadium, 500, t, 800, "Logan Paul", 0);
                    Fight f = new Fight(LOGAN_PAUL);
                    boolean didWin = f.fight();
                      if(didWin){
                        Game.getGame().getPlayer().getCurrentRoom().getEnemies().remove(LOGAN_PAUL);
                        renderer.showCutScene(1100, "\\bin\\zork\\data\\finalcutscene.txt", 15);
                        
                        //cutscence with logan paul dieing and you getting the prime, then bringing all the primes back to cyrus and playing soccor with him
                      }else{
                        Game.getGame().getPlayer().setCurrentRoom(japanNationalStadiumPlaza);
                      }
        
                
                  }catch(Exception e){
                    //aklsdjalsd
                  }
                }
              });
          //exits
          final Exit japanNationalStadiumPlazaExitNorth = new Exit("N",japanNationalStadiumPlaza); HoleInTheGroundJapan.addExit(japanNationalStadiumPlazaExitNorth);
          final Exit japanNationalStadiumExitNorth = new Exit("N",japanNationalStadium); japanNationalStadiumPlaza.addExit(japanNationalStadiumExitNorth);
          final Exit japanTimHortonsExitEast = new Exit("E",japanTimHortons); japanNationalStadiumPlaza.addExit(japanTimHortonsExitEast);
          final Exit HoleInTheGroundJapanExitSouth = new Exit("S",HoleInTheGroundJapan); japanNationalStadiumPlaza.addExit(HoleInTheGroundJapanExitSouth);
          final Exit japanNationalStadiumPlazaExitWest = new Exit("W",japanNationalStadiumPlaza); japanTimHortons.addExit(japanNationalStadiumPlazaExitWest);
          final Exit japanNationalStadiumPlazaExitSouth = new Exit("S",japanNationalStadiumPlaza); japanNationalStadium.addExit(japanNationalStadiumPlazaExitSouth);
          
          ksiHouse.setRunnable(() -> {
            Graphics text = new Graphics();
            if (!hasBeatenKsi[0]) {
              try {
                text.slowTextSpeed("You enter Ksi's House... The silence unerves you", 25);
                text.slowTextSpeed(" KSI: Hello. I've been expecting you... \n You: Show yourself! \n KSI: You, trying to take over my company, pathetic \n You: Im stronger than you think \n KSI: We'll see about that!!!", 25);
                Fight f = new Fight(KSI1ndFormEnemy);
                boolean won = f.fight();
                if (won) {
                  text.slowTextSpeed(" KSI: H-H-HOW?? \n You: I'm Simply better \n KSI: N-NO! I won't go down like this!! \n > You see KSI pull out a Syringe full of a mixture of all 8 prime flavors \n KSI: This power may kill me, but I need to try \n > You see KSI inject himself with the Primes \n KSI: Ready for round two? \n > You see KSI's body changing to a unrecognizable form, he charges at you with pure rage in his eyes.", 50);
                  Fight f2 = new Fight(KSI2ndFormEnemy);
                  won = f2.fight();
                  if(won) {
                    text.slowTextSpeed(" KSI: You are.. much stronger then i thought..., Curse you cyrus, couldn't even fight me himself. *Cough Cough \n KSI: Well I guess thats it- \n YOU HAVE DEFEATED KSI... But whats this? you feel the ground below you start to shake... \n A hole opens up below you and you start falling... \n an you keep on falling for another 42 minutes. \n ", 25);
                    hasBeatenKsi[0] = true;
                    Game.getGame().getPlayer().setCurrentRoom(HoleInTheGroundJapan);
                  }
                }
              
              } catch (Exception e1) {
                e1.printStackTrace();
              }
              
            } else {
              try {
                text.slowTextSpeed(" As you enter the house you immediatly remember the gaping hole that is there. You fall and keep falling for another 42 minutes", 25);
              } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
              }
              Game.getGame().getPlayer().setCurrentRoom(HoleInTheGroundJapan);
            }
          });
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
    this.player.setCurrentRoom(roomMap.get("holeinthegroundjapan"));
    this.player.setMoney(5);
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
