package com.ndzumamalate.wumpus;

import java.lang.Math;
import java.util.Random;

public class Board {
    private Position[][] board;
    private int boardSize;

    public Board(int size, int playerX, int playerY) {
        this.board = new Position[size][size];
        this.boardSize = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = new Position(i, j);
            }
        }

        // add player to board
        this.board[playerX][playerY].setPlayer(true);
        this.board[playerX][playerY].setStart();
        this.board[playerX][playerY].setTraversed();
        generateHazards();
        generateGold();
    }

    public void generateHazards() {
        Random rand = new Random();
        int x, y;
        double maxHurdles = Math.pow(this.boardSize, 2) * 0.25;

        for (int i = 0; i < maxHurdles; i++) {
            do {
                x = rand.nextInt(this.boardSize);
                y = rand.nextInt(this.boardSize);
            } while (this.board[x][y].hasHurdles() || this.board[x][y].hasPlayer());
            Position tile = this.board[x][y];
            if (rand.nextBoolean()) {
                tile.setWumpus(true);
                addStenchAround(x, y);
            } else {
                tile.setPit(true);
                addBreezeAround(x, y);
            }
        }
    }

    private void addStenchAround(int x, int y) {
        if (x > 0) {
            this.getPosition(x - 1, y).setStench(true);
        }
        if (x < this.boardSize - 1) {
            this.getPosition(x + 1, y).setStench(true);
        }
        if (y > 0) {
            this.getPosition(x, y - 1).setStench(true);
        }
        if (y < this.boardSize - 1) {
            this.getPosition(x, y + 1).setStench(true);
        }
    }

    private void addBreezeAround(int x, int y) {
        if (x > 0) {
            this.getPosition(x - 1, y).setBreeze(true);
        }
        if (x < this.boardSize - 1) {
            this.getPosition(x + 1, y).setBreeze(true);
        }
        if (y > 0) {
            this.getPosition(x, y - 1).setBreeze(true);
        }
        if (y < this.boardSize - 1) {
            this.getPosition(x, y + 1).setBreeze(true);
        }
    }

    private void generateGold() {
        Random rand = new Random();
        int x, y;
        double maxGold = Math.pow(this.boardSize, 2) * 0.05;

        for (int i = 0; i < maxGold; i++) {
            do {
                x = rand.nextInt(this.boardSize);
                y = rand.nextInt(this.boardSize);
            } while (this.board[x][y].hasHurdles() || this.board[x][y].hasPlayer());
            this.board[x][y].setGold(true);
        }
    }
    public Position[][] getBoard() {
        return board;
    }

    public void setBoard(Position[][] board) {
        this.board = board;
    }

    public Position getPosition(int x, int y) {
        return board[x][y];
    }

    public int getBoardSize() {
        return boardSize;
    }
}
