package zork.commands;

import java.io.IOException;

import zork.Command;
import zork.Game;
import zork.Graphics;
import zork.Exit;

public class Help extends Command {

    public Help(String name) {
        super(name);
        addAlias("h");
    }

    @Override
    public String runCommand(String... args){
        final Graphics renderer = new Graphics();
        try {
            renderer.showCutScene(1100, "\\bin\\zork\\data\\help.txt", 15);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}

