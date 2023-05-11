package zork.commands;

import java.util.ArrayList;
import zork.Command;
import zork.Game;
import zork.Inventory;
import zork.Item;
import zork.items.Weapon;
public class Use extends Command {
    public Use(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        if(Game.getGame().getPlayer().isInWeaponMenu()){
            ArrayList<Weapon> arr = Game.getGame().getPlayer().getInventory().getWeapons();
            for (int i = 0; i < arr.size(); i++) {
                if(args[0].equalsIgnoreCase(arr.get(i).getWeapon())) {
                    Game.getGame().getPlayer().setIsCurrentMove(true);
                    Game.getGame().getPlayer().setCurrentWeapon(arr.get(i));
                    return arr.get(i).getWeapon();
                }
            }
        }else if(Game.getGame().getPlayer().getIsItemMenu()){
            ArrayList<Item> pInventory = Game.getGame().getPlayer().getInventory().getItems();
            for (int i = 0; i < pInventory.size(); i++) {
                if(args[0].equalsIgnoreCase(pInventory.get(i).getName())){
                    Game.getGame().getPlayer().setIsCurrentMove(true);
                    Game.getGame().getPlayer().setCurrentItem(pInventory.get(i));
                    return pInventory.get(i).getName();
                }
            }
        }
        return "You are not in a fight"; //make it do stuff outside of battle later
    }

    
}