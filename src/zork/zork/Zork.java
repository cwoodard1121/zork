package zork;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Zork {
  public static void main(String[] args) throws InterruptedException {
    Game.getGame().play();
  }
}
