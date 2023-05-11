package zork.commands;

import datatypes.Location;
import zork.Command;
import zork.Fight;
import zork.Room;
import zork.Constants.EntityConstants;
import zork.Constants.PlayerConstants;
import zork.enemies.ExampleEnemy;
import zork.entites.Enemy;

public class StartFight extends Command{
    

    public StartFight(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        
        Enemy tester = new ExampleEnemy(new Location(0, 0), new Room(), 20, null, 0, 10, "homless");
        Fight test = new Fight(tester);
        test.fight();
        // TODO Auto-generated method stub
        return super.runCommand(args);
    }

}
