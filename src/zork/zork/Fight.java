package zork;

import java.util.ArrayList;

import zork.entites.Enemy;

public class Fight {
    private Enemy enemy;

    public Fight(Enemy bad){
        this.enemy = bad;
    }

    public void fight(){
        int playerHealth = Game.getGame().getPlayer().getHealth();
        int enemyHealth = enemy.getHealth();

        int playerSpeed = Game.getGame().getPlayer().getSpeed();
        int enemySpeed = enemy.getSpeed();
        
        ArrayList<Item> playerStuff = Game.getGame().getPlayer().getInventory();
        ArrayList<Item> enemyStuff = enemy.getInventory();

        ArrayList<Moves> playerMoves = Game.getGame().getPlayer().getMoves();
        ArrayList<Moves> enemyMoves = enemy.getMoves();

        /*Fighting
         * Whoever has most speed goes first, lets say its player
         * player is given their moves and asked which weapon they want to use if they have more than one
         * if they dont it will give them the only weapon they have
         * they choose which move and it does it 
         *damage from the weapon is added to the damage of the move
         * 
         * 
         * 
         * 
         * 
         */

    }
}
