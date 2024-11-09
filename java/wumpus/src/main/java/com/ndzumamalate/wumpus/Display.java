package com.ndzumamalate.wumpus;

import java.util.Scanner;

public class Display {
    private Scanner scanner = new Scanner(System.in);

    public String getPlayerInput() {
        return scanner.nextLine().toLowerCase();
    }

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

    public void game(int size, Board gameBoard, Stats stats) {
        System.out.println(this.header());
        System.out.println(this.stats(stats));
        System.out.println(this.legend());
        System.out.println(this.board(size, gameBoard));
        System.out.println(this.controls());
    }

    public void gameOver(int size, Board gameBoard, Stats stats) {
        System.out.println(this.header());
        System.out.println(this.endStats(stats));
        System.out.println("Last frame:\n");
        System.out.println(this.board(size, gameBoard));
        System.out.println("Game Over!");
    }

    public String board(int size, Board gameBoard) {
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
                if (tile.hasPlayer()) {
                    board.append("|  🧍🏽‍♂️  ");
                } else if (tile.hasWumpus()) {
                    board.append("|   W  ");
                } else if (tile.hasPit()) {
                    board.append("|  Pi  ");
                } else if (tile.hasGold()) {
                    board.append("|  Gld ");
                } else {
                    board.append("|      ");
                }
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
        String gold = "Player's Gold:" + stats.getGoldAcquired() + "\n";
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
        return "Legend:\n\n🧍🏽‍♂️ = Player, 🏠 = Start Position\nGld = Gold, b = Breeze, s = Stench\n";
    }

    public String controls() {
        return "Controls:\n\nw = up, s = down, a = left, d = right\nq = quit, e = Hint\n\nNote: Just avoid the arrow keys\n";
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
