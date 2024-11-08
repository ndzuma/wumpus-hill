package com.ndzumamalate.wumpus;

import java.util.Scanner;

public class Display {
    private Scanner scanner = new Scanner(System.in);

    public String getPlayerInput() {
        return scanner.nextLine().toLowerCase();
    }

    public void landing() {}

    public String board(int size, Board gameBoard) {
        // The separators
        String separator = "";
        for (int i = 0; i < size; i++) {
            separator += "+⎯⎯⎯⎯⎯⎯";
        }
        separator += "+";

        // The tiles
        String board = "";
        for (int x = 0; x < size; x++) {
            board += separator + "\n";
            for (int y = 0; y < size; y++) {
                Position tile = gameBoard.getPosition(x, y);
                if (tile.hasPlayer()) {
                    board += "|  🧍🏽‍♂️  ";
                } else if (tile.hasWumpus()) {
                    board += "|   W  ";
                } else if (tile.hasPit()) {
                    board += "|  Pi  ";
                } else if (tile.hasGold()) {
                    board += "|  Gld ";
                } else {
                    board += "|      ";
                }
            }
            board += "|\n";
        }
        board += separator;
        return board;
    }

    public void clearTerminal() {
        try {
            // Check the OS name to determine the appropriate command
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // For Unix, Linux, or macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void header() {}

    public void stats() {}

    public void legend() {}

    public void controls() {}

    public void gameOver() {}
}
