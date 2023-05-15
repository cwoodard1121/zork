package zork.commands;

import zork.Command;
import zork.Game;

public class Weapon extends Command {
    public Weapon(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        if(Game.getGame().getPlayer().isInFight() == true){
        Game.getGame().getPlayer().setInWeaponMenu(true);
        Game.getGame().getPlayer().setChoosingMenu(false);
        return "going into the menu";
        }else{
            return "not in a fight";
        }


    }
}