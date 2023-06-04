package zork;


import java.util.Scanner;

public class BlackJack {

    static Scanner in = new Scanner(System.in);
    static final int MIN_BET = 1;
    private static final int WIN = 1;
    private static final int LOST = -1;
    private static final int TIE = 0;
    private static final int NUM_SUITS = 4;
    private static final String HEARTS = "H";
    private static final String SPADES = "S";
    private static final String CLUBS = "C";
    private static final String DIAMONDS = "D";
    private static final int NUM_VALUES = 13;
    private static final String ACE = "A";
    private static final String JACK = "J";
    private static final String QUEEN = "Q";
    private static final String KING = "K";
    public void play(){
        
        double wallet = Game.getGame().getPlayer().getMoney();
        boolean stillPlaying = true;

        while(stillPlaying){
            System.out.println("Money you have: " + wallet + "$");
            double bet = getBet(wallet);
            String playerHand = getCard() + " " + getCard();
            String dealerHand = getCard();
            
            displayHand(dealerHand, true, "Dealer: ");
            displayHand(playerHand, false, "Player: ");
            
            //returns who won
            int result = playHand(playerHand, dealerHand);


            if(result == WIN){
                System.out.println("YOU WIN!");
                wallet += bet;
                System.out.println("The money you have: " + wallet + "$");
            }else if(result == LOST){
                System.out.println("YOU LOSE");
                wallet -= bet;
                System.out.println("The money you have: " + wallet + "$");
            }
            
            if(wallet< MIN_BET){
                stillPlaying = false;
                System.out.println("have fun on the streets");
            }else{
                stillPlaying = playAgain();
            }
        }
        Game.getGame().getPlayer().setMoney(wallet);
        


    }

    private static boolean playAgain() {
        
        boolean complete = false;
        boolean result = false;

        while(!complete){
        try{
        System.out.println("Do you want to play again? (1) Yes (2) No");
        int answer = Integer.parseInt(in.nextLine());

        if(answer == 1){
            complete = true;
            result =  true;
        }else if(answer == 2){
            complete = true;
            result =  false;
            System.out.println("99% of gamblers quit before their big win...");
            
        }else{
            System.out.println("Invalid answer");
        }
        }catch(NumberFormatException ex){
            System.out.println("Invalid answer");
        }
        }
        return result;

    }

    // return win if player wins, return lost if player lost and tie if they tie
    private static int playHand(String playerHand, String dealerHand) {
        playerHand = playerTurn(playerHand);
        dealerHand = dealerTurn(dealerHand);

        int playerScore = getCardValue(playerHand);
        int dealerScore = getCardValue(dealerHand);

        if(playerScore <= 21 && (dealerScore> playerScore) || (playerScore <= 21 && (dealerScore > 21)))
            return WIN;
        else if(playerScore> 21 || dealerScore > playerScore)
            return LOST;
        else
            return TIE;

    }

    private static int getCardValue(String cards) {
        int numAces = 0;

        int scoreBeforeAces = 0;
        for(int i = 0; i<cards.length(); i++){
            String s = cards.substring(i, i+1);
            if("JQK1".indexOf(s)>=0)
                scoreBeforeAces += 10;
            else if("23456789".indexOf(s) >= 0){
                scoreBeforeAces += Integer.parseInt(s);
            }else if("A".indexOf(s) >= 0)
                numAces++;


        }


        if(numAces > 0 && (scoreBeforeAces + 11 + numAces - 1) <= 21)
        scoreBeforeAces += 11 + numAces - 1;
        else
        scoreBeforeAces += numAces;

        return scoreBeforeAces;

    }


    private static String dealerTurn(String dealerHand) {
        dealerHand += " " + getCard();
        displayHand(dealerHand, false, "Dealer hand: ");
        while(getCardValue(dealerHand) < 17){
            dealerHand += " " + getCard();
            displayHand(dealerHand, false, "Dealer hand: ");
        }

        return dealerHand;
    }

    private static String playerTurn(String playerHand) {
        displayHand(playerHand, false, "Player Hand: ");

        while(true){;
           if(takeCard()){
                playerHand += " " + getCard();
                displayHand(playerHand, false, "Player Hand: ");
                if(getCardValue(playerHand) > 21)
                    return playerHand;
                
           }else{
                return playerHand;
           }
        }
    }

    

    private static boolean takeCard() {
        while(true){
            System.out.println("Hit (1) or Stand (2): ");
            String result = in.nextLine();

            if(result.equals("1")){
                return true;
            }else if(result.equals("2"))
                return false;
            else
                System.out.println("Invalid input");
        }
    }

    private static void displayHand(String cards, boolean isHidden, String label) {
        if(isHidden){
            System.out.println(label + "XX" + " " + cards);
        }else{
            System.out.println(label + cards);
        }

        
    }

    private static String getCard() {
        return getValue() + getSuit();
    }

    private static String getSuit() {
        int iSuit = (int) (Math.random()* NUM_SUITS) + 1;
        if(iSuit == 1)
            return HEARTS;
        else if(iSuit == 2)
            return SPADES;
        else if(iSuit == 3)
            return CLUBS;
        else
            return DIAMONDS;

    }

    private static String getValue() {
        int iValue = (int)(Math.random()* NUM_VALUES) + 1;

        if(iValue == 1)
            return ACE;
        else if(iValue == 11)
            return JACK;
        else if(iValue == 12)
            return QUEEN;
        else if(iValue == 13)
            return KING;
        else
            return "" + iValue;

        
    }

    private static double getBet(double wallet) {
        
        boolean validBet = false;
        double bet = 0;
        System.out.print("Please enter bet (MIN: $" + MIN_BET + ")" );
        
        while(!validBet){
        try{
            bet = Integer.parseInt(in.nextLine());

            if(bet> wallet){
                System.out.print("Please enter bet(MAX: $" + wallet + "):" );
            }else if(bet < MIN_BET){
                System.out.print("Please enter bet (MIN: $" + MIN_BET + "):" );
            }else{
                validBet = true;
            }
        }catch(NumberFormatException ex){
            System.out.println("Invalid bet");
            System.out.print("Please enter bet (MIN: $" + MIN_BET + "):" );
        }

    }
        return bet;
    }
}

