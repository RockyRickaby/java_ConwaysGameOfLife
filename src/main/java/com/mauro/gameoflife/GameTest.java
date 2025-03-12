package com.mauro.gameoflife;

public class GameTest {
    public static void main(String[] args) {
        GameOfLife game = new GameOfLife(12, 30);
        //game.tick();
        game.toggle(1, 2);
        game.toggle(2, 2);
        game.toggle(3, 2);
        game.toggle(2, 1);
        game.toggle(2, 3);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.print();
    }   
}
