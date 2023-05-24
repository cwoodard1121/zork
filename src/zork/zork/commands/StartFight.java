package zork.commands;

import datatypes.Location;
import zork.Command;
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
    public String runCommand(String... args) {
       
        Enemy tester = new ExampleEnemy(new Location(0, 0), new Room(), 30, null, 0, 10, "homless");
        Fight test = new Fight(tester);
        Item testItem = new Item(0, "health pot", false, true);
        Game.getGame().getPlayer().getInventory().addItem(testItem);
        Weapon testWeapon = new Weapon(0, "Diamond Pick", false, 5, null);
        Game.getGame().getPlayer().getInventory().addItem(testWeapon);
        test.fight();
        return Game.isTesting ? "done" : "";
    }

}
