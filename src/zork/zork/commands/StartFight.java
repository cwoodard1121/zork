package zork.commands;

import zork.Command;
import zork.Fight;
import zork.enemies.HomelessGuy;
import zork.entites.Enemy;

public class StartFight extends Command{
    

    public StartFight(String name) {
        super(name);
    }

    @Override
    public String runCommand(String... args) {
        Enemy tester = new HomelessGuy(null, null, 0, null, null, 0, 10, "homless");
        tester.create();
        Fight test = new Fight(tester);
        test.fight();
        // TODO Auto-generated method stub
        return super.runCommand(args);
    }

}
