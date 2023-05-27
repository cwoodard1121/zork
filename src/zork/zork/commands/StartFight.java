package zork.commands;

import datatypes.Location;
import zork.Command;
import zork.Effects;
import zork.Fight;
import zork.Game;
import zork.Item;
import zork.Room;
import zork.enemies.ExampleEnemy;
import zork.entites.Enemy;
import zork.items.Weapon;


public class StartFight extends Command {
    

    public StartFight(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) throws InterruptedException {
       
        Enemy tester = new ExampleEnemy(new Location(0, 0), new Room(), 30, null, 0, 10, "homless");
        Fight test = new Fight(tester);
        Game.getGame().getPlayer().getInventory().addItem(new Item(0, "health pot", false, new Effects("health up", 0, 0, 2, 12), false));
        Game.getGame().getPlayer().getInventory().addItem(new Weapon(0, "Diamond Pick", false, 5, null));
        test.fight();
        return Game.isTesting ? "done" : "";
    }

}
