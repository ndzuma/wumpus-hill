package com.ndzumamalate.wumpus;

import java.util.Scanner;

public class Display {
    public void gameLanding(int[] allowedGridSizes, int selected) {
        System.out.println(this.header());
        System.out.println("Welcome to the Wumpus World Game!\n");
        System.out.println("\033[1mSelect the grid size\033[0m\n");
        System.out.println("Controls:\nEnter = Select, w = up, s = down, q = quit\n");

        for (int size : allowedGridSizes) {
            if (size == allowedGridSizes[selected]) {
                System.out.println("\033[1;33m> " + size + "X" + size + "\033[0m");
            } else {
                System.out.println("> " + size + "X" + size);
            }
        }
    }

    public void game(int size, Board gameBoard, Stats stats, boolean isGameOver) {
        System.out.println(this.header());
        System.out.println(this.stats(stats));
        System.out.println(this.legend());
        System.out.println(this.board(size, gameBoard, isGameOver));
        System.out.println(this.controls());
    }

    public void gameOver(int size, Board gameBoard, Stats stats, boolean isGameOver) {
        System.out.println(this.header());
        System.out.println(this.endStats(stats));
        System.out.println("Last frame:\n");
        System.out.println(this.board(size, gameBoard, isGameOver));
        System.out.println("Game Over!");
    }

    // This one is a bit strange, so ill leave this here for future me (that's if I even come back)
    public String formatChar(Position tile, boolean gameOver) {
        // This section varies and even thought I could have made a simple switch statement
        // I wanted to see if I could make a formater so that the board method looked better :)
        StringBuilder c =new StringBuilder();

        //This is because of the colour "thingies", they change the length
        // This is the simplest way to implement this, also idc if it isn't rn, it's 11:30pm
        int cLength = 0;

        // This section return these specif 3 because it doesn't matter what else is in the tile
        // They are the only thing that need to be displayed

        if (!tile.hasBeenTraversed() && !gameOver) {
            return "|   ?  ";
        } else if (tile.hasPlayer()) {
            c.append("🧍🏽‍♂️");
            cLength++;
        } else if (tile.isStart()) {
            return "|  🏠  ";
        } else if (tile.hasWumpus()) {
            return "|   W  ";
        } else if (tile.hasPit()) {
            return "|  Pi  ";
        }

        if (tile.hasGold()) {c.append("\033[33mGld\033[0m "); cLength+=4;}
        if (tile.hasBreeze()) {c.append("\033[31mb\033[0m"); cLength++;}
        if (tile.hasStench()) {c.append("\033[31ms\033[0m"); cLength++;}
        if (!c.isEmpty()) {
            int freeSpace = (6 - cLength);
            if (freeSpace > 1) {
                int leftPadding = Math.min(freeSpace / 2, freeSpace - (freeSpace / 2));
                int rightPadding = Math.max(freeSpace / 2, freeSpace - (freeSpace / 2));

                if (tile.hasPlayer() && leftPadding > 0) {
                    leftPadding -= 1;
                }

                return "|" + " ".repeat(leftPadding) + c + " ".repeat(rightPadding);
            } else {
                // this repeat is to guarantee the tile is the desired size at the end
                return "|" + c + " ".repeat(freeSpace);
            }
        }
        return"|      ";

    }

    public String board(int size, Board gameBoard, boolean isGameOver) {
        // The separators
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < size; i++) {
            separator.append("+⎯⎯⎯⎯⎯⎯");
        }
        separator.append("+");

        // The tiles
        StringBuilder board = new StringBuilder();
        for (int x = 0; x < size; x++) {
            board.append(separator).append("\n");
            for (int y = 0; y < size; y++) {
                Position tile = gameBoard.getPosition(x, y);
                board.append(formatChar(tile, isGameOver));
            }
            board.append("|\n");
        }
        board.append(separator).append("\n");
        return board.toString();
    }

    public String header() {
        return """
                \033[1;34m

██╗    ██╗██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗███████╗███████╗███████╗
██║    ██║██║   ██║████╗ ████║██╔══██╗██║   ██║██╔════╝██╔════╝██╔════╝
██║ █╗ ██║██║   ██║██╔████╔██║██████╔╝██║   ██║███████╗███████╗███████╗
██║███╗██║██║   ██║██║╚██╔╝██║██╔═══╝ ██║   ██║╚════██║╚════██║╚════██║
╚███╔███╔╝╚██████╔╝██║ ╚═╝ ██║██║     ╚██████╔╝███████║███████║███████║
 ╚══╝╚══╝  ╚═════╝ ╚═╝     ╚═╝╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝
                \033[0m
""";
    }

    public String stats(Stats stats) {
        String points = "Player's Points: " + stats.getPoints() + "\n";
        String gold = "Player's Gold: " + stats.getGoldAcquired() + "\n";
        return "Stats:\n\n" + points + gold;
    }

    public String endStats(Stats stats) {
        String points = "Points: " + stats.getPoints() + "\n";
        String gold = "Gold: " + stats.getGoldAcquired() + "\n";
        String moves = "Moves taken: " + stats.getMovesTaken() + "\n";
        String hints = "Hints used: " + stats.getHintsUsed() + "\n";
        return "Stats:\n\n" + points + gold + moves + hints;
    }

    public String legend() {
        return "Legend:\n\n🧍🏽‍♂️ = Player, 🏠 = Start Position\n\033[33mGld\033[0m = Gold, \033[31mb\033[0m = Breeze, \033[31ms\033[0m = Stench\n";
    }

    public String controls() {
        return "Controls:\n\nw = up, s = down, a = left, d = right\nq = quit, e = Hint\n\nNotes:\n- Just avoid the arrow keys\n- Going home ends the game(which is intended)\n";
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
}
