package zork;

import java.util.Scanner;

public class LocationMaking {
  public static void main(String[] args) throws InterruptedException {
    boolean name = true;
    boolean exits = true;
    boolean hasNumber = false;
    Integer numRepeatInt = 1;
    while(name) {
        Scanner in = new Scanner(System.in);
        System.out.println("Whats the name of the Room");
        String ans = in.nextLine();
        System.out.println("final Room " + ans + " = new Room (\"Placeholder Description for " + ans + "\", \"" + ans.toLowerCase() + "\"); roomMap.put(" + ans + ".getRoomName(), " + ans + ");");
        System.out.println("how many exits does this room have?");
        while(!hasNumber) {
        String numRepeat = in.nextLine();
        try {
            numRepeatInt = Integer.parseInt(numRepeat);
            hasNumber = true;
        } catch (Exception e) {
            System.out.println("enter a number");
        }
    }
        for (int i = 0; i < numRepeatInt; i++) {
            System.out.println("What room does this lead to and which direction do you walk to get there, for example D yorkMillsSubwayHallway");
            String str = in.nextLine();
            String directionLetter = str.substring(0,1).toUpperCase();
            String goTo = str.substring(2);
            String direction = "";
            if (directionLetter.equals("U"))
                direction = "Up";
            else if (directionLetter.equals("D"))
                direction = "Down";
            else if (directionLetter.equals("N"))
                direction = "North";
            else if (directionLetter.equals("S"))
                direction = "South";
            else if (directionLetter.equals("E"))
                direction = "East";
            else if (directionLetter.equals("W"))
                direction = "West";
            System.out.println("final Exit " + goTo + "Exit" + direction + " = new Exit(\"" + directionLetter + "\"," + goTo + "); " + ans + ".addExit(" + goTo + "Exit" + direction + ");");
            
        }
        System.out.println("Room Completed");
    } 
    // final Room yorkMillsBusTerminal = new Room ("Placeholder Description for yorkMillsBusTerminal", "yorkmillsbusterminal"); roomMap.put(yorkMillsBusTerminal.getRoomName(), yorkMillsBusTerminal)
    // final ExityorkMillsSubwayHallwayExit Down new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown)
    // final Exit yorkMillsSubwayHallwayExit Down new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown);
    // final Exit yorkMillsSubwayHallwayExitDown = new Exit("D",yorkMillsSubwayHallway); yorkMillsBusTerminal.addExit(yorkMillsSubwayHallwayExitDown);
    // final Exit yorkMillsBusSubwayHallwayExitNorth = new Exit("N", yorkMillsSubwayHallway); yorkMillsSubway.addExit(yorkMillsBusSubwayHallwayExitNorth);
    // final Exit yorkMillsBusTerminalExitUp = new Exit("U", yorkMillsBusTerminal); yorkMillsSubwayHallway.addExit(yorkMillsBusTerminalExitUp);
    // EXIT LEGEND
    // final exit (destinationRoomExit(direction current room exits to) = new Exit(direction, destinationRoom); currentRoom.addExit(destinationRoomExit(direction current room exit to))
    // final exit  = new Exit("", );  .addExit( );
  }
} 