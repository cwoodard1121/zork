package zork.commands;

import java.util.ArrayList;

import zork.Command;
import zork.Game;
import zork.Inventory;
import zork.Move;
import zork.items.Weapon;
public class Use extends Command {
    public Use(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        if(Game.getGame().getPlayer().getIsMoveMenu()) {
            ArrayList<Move> moves = Game.getGame().getPlayer().getMove();
            for (int i = 0; i < moves.size(); i++) {
                if(args[0].equalsIgnoreCase(moves.get(i).getMove())) {
                    Game.getGame().getPlayer().setIsCurrentMove(true);
                    Game.getGame().getPlayer().setCurrentMove(moves.get(i));
                    return moves.get(i).getMove();
                }
            }
            return "That is not a valid move";
        }else if(Game.getGame().getPlayer().isInWeaponMenu()){
            ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
            for (int i = 0; i < arr.size(); i++) {
                if(args[0].equalsIgnoreCase(arr.get(i).getWeapon())) {
                    Game.getGame().getPlayer().setIsCurrentMove(true);
                    Game.getGame().getPlayer().setCurrentWeapon(arr.get(i));
                    return arr.get(i).getWeapon();
                }
            }
        }
        return "You are not in a fight"; //make it do stuff outside of battle later
    }

    
}