package com.ndzumamalate.wumpus;

import java.util.Random;
import java.io.IOException;


public class Game {
    private boolean isGameOver = false;
    private Display display;
    private Player player;
    private Board board;
    private Stats stats;
    private int gridSize = 0;
    private int[] allowedGridSizes = {3, 5, 10,15};

    public Game() {
        this.player = new Player();
        this.stats = new Stats();
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
        int currentSelection = 0;
        while (this.gridSize == 0) {
            display.clearTerminal();
            display.gameLanding(this.allowedGridSizes, currentSelection);
            String input = GameInput.getNormalInput();
            switch (input) {
                case "w":
                    if (currentSelection == 0) {
                        currentSelection = this.allowedGridSizes.length - 1;
                    } else {
                        currentSelection--;
                    }
                    break;
                case "s":
                    if (currentSelection == this.allowedGridSizes.length - 1) {
                        currentSelection = 0;
                    } else {
                        currentSelection++;
                    }
                    break;
                case "q":
                    handleInput(input);
                    break;
                case "":
                    this.gridSize = this.allowedGridSizes[currentSelection];
                    break;
                default:
                    break;
            }
            try {
                Thread.sleep(100);  // 100 milliseconds (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        placePlayer(this.gridSize);
        this.board = new Board(this.gridSize, this.player.getxPosition(), this.player.getyPosition());
        while (!isGameOver) {
            display.clearTerminal();
            display.game(this.gridSize, this.board, this.stats, this.isGameOver);
            handleInput(GameInput.getNormalInput());
        }
        display.clearTerminal();
        display.gameOver(this.gridSize, this.board, this.stats, this.isGameOver);
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

    private void handleInput(String command) {
        int x = this.player.getxPosition();
        int y = this.player.getyPosition();
        if (Direction.UP.matches(command)) {
            if (x != 0) {
                if (!this.board.getPosition((x - 1), y).hasHurdles()) {
                    this.player.setxPosition(x - 1);
                    this.board.getPosition(x, y).setPlayer(false);
                    this.board.getPosition((x - 1), y).setPlayer(true);
                    this.board.getPosition((x - 1), y).setTraversed();
                    this.stats.moveTaken();
                    if (this.board.getPosition((x - 1), y).hasGold()) {
                        this.stats.goldAcquired();
                        this.board.getPosition((x - 1), y).removeGold();
                    }
                } else {this.isGameOver = true; this.stats.foundHurdle();}
            } else {System.out.println("You can't move up");}
        } else if (Direction.DOWN.matches(command)) {
            if (x != this.board.getBoardSize()-1) {
                if (!this.board.getPosition((x + 1), y).hasHurdles()) {
                    this.player.setxPosition(x + 1);
                    this.board.getPosition(x, y).setPlayer(false);
                    this.board.getPosition((x + 1), y).setPlayer(true);
                    this.board.getPosition((x + 1), y).setTraversed();
                    this.stats.moveTaken();
                    if (this.board.getPosition((x + 1), y).hasGold()) {
                        this.stats.goldAcquired();
                        this.board.getPosition((x + 1), y).removeGold();
                    }
                } else {this.isGameOver = true; this.stats.foundHurdle();}
            } else {System.out.println("You can't move down");}
        } else if (Direction.LEFT.matches(command)) {
            if (y != 0) {
                if (!this.board.getPosition(x, (y - 1)).hasHurdles()) {
                    this.player.setyPosition(y - 1);
                    this.board.getPosition(x, y).setPlayer(false);
                    this.board.getPosition(x, (y - 1)).setPlayer(true);
                    this.board.getPosition(x, (y - 1)).setTraversed();
                    this.stats.moveTaken();
                    if (this.board.getPosition(x, (y - 1)).hasGold()) {
                        this.stats.goldAcquired();
                        this.board.getPosition(x, (y - 1)).removeGold();
                    }
                } else {this.isGameOver = true; this.stats.foundHurdle();}
            } else {System.out.println("You can't move left");}
        } else if (Direction.RIGHT.matches(command)) {
            if (y != this.board.getBoardSize()-1) {
                if (!this.board.getPosition(x, (y + 1)).hasHurdles()) {
                    this.player.setyPosition(y + 1);
                    this.board.getPosition(x, y).setPlayer(false);
                    this.board.getPosition(x, (y + 1)).setPlayer(true);
                    this.board.getPosition(x, (y + 1)).setTraversed();
                    this.stats.moveTaken();
                    if (this.board.getPosition(x, (y + 1)).hasGold()) {
                        this.stats.goldAcquired();
                        this.board.getPosition(x, (y + 1)).removeGold();
                    }
                } else {this.isGameOver = true; this.stats.foundHurdle();}
            } else {System.out.println("You can't move right");}
        } else if (GameCommands.QUIT.matches(command)) {
            System.out.println("Are you sure you want to quit? (y/n/enter)");
            String input = GameInput.getNormalInput().toLowerCase();
            switch (input) {
                case "y", "\r", "\n", "":
                    System.out.println("Game Over!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } else if (GameCommands.HELP.matches(command)) {
            System.out.println("Hints:");
            System.out.println("Number of dangers left:");
            System.out.println("Number of gold left:");
            this.stats.hintUsed();
        } else {
            System.out.println("Invalid command");
        }
    }
}
