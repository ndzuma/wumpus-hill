package com.ndzumamalate.wumpus;

import java.util.Random;

public class Game {
    private boolean isGameOver = false;
    private Display display;
    private Player player;
    private Board board;
    // private int points;
    // private int goldAcquired;
    // private int movesTaken;
    // private int hintsUsed;

    public Game() {
        this.player = new Player();
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void startGame() {
        placePlayer(5);
        this.board = new Board(5, this.player.getxPosition(), this.player.getyPosition());
        while (!isGameOver) {
            System.out.println(display.board(5, this.board));
            display.getPlayerInput();
        }
    }

    private void placePlayer(int size) {
        Random rand = new Random();
        int playerPositionX = rand.nextInt(size);
        int playerPositionY;
        if (playerPositionX == 0 || playerPositionX == size - 1) {
            playerPositionY = rand.nextInt(size);
        } else {
            playerPositionY = rand.nextBoolean() ? 0 : size - 1;
        }
        this.player.setxPosition(playerPositionX);
        this.player.setyPosition(playerPositionY);
    }

    public void handleInput(String command) {

    }
}
