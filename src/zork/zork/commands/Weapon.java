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
            Game.getGame().getPlayer().setChoosingMenu(false);
            Game.getGame().getPlayer().setInWeaponMenu(true);
            return Game.getGame().isTesting ? "going into the menu" : "";
        }else{
            return "not in a fight";
        }


    }
}