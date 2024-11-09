package com.ndzumamalate.wumpus;

import java.util.Scanner;

public class Display {
    private Scanner scanner = new Scanner(System.in);

    public String getPlayerInput() {
        return scanner.nextLine().toLowerCase();
    }

    public void landing() {}

    public void game(int size, Board gameBoard) {
        System.out.println(this.header());
        System.out.println(this.stats());
        System.out.println(this.legend());
        System.out.println(this.board(size, gameBoard));
        System.out.println(this.controls());
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

    public String header() {
        return """


██╗    ██╗██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗███████╗███████╗███████╗
██║    ██║██║   ██║████╗ ████║██╔══██╗██║   ██║██╔════╝██╔════╝██╔════╝
██║ █╗ ██║██║   ██║██╔████╔██║██████╔╝██║   ██║███████╗███████╗███████╗
██║███╗██║██║   ██║██║╚██╔╝██║██╔═══╝ ██║   ██║╚════██║╚════██║╚════██║
╚███╔███╔╝╚██████╔╝██║ ╚═╝ ██║██║     ╚██████╔╝███████║███████║███████║
 ╚══╝╚══╝  ╚═════╝ ╚═╝     ╚═╝╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝

""";
    }

    public String stats() {
        return "Stats:\n\nPlayer's Points:\nPlayer's Gold:\n";
    }

    public String legend() {
        return "Legend:\n\n🧍🏽‍♂️ = Player, 🏠 = Start Position\nGld = Gold, b = Breeze, s = Stench\n";
    }

    public String controls() {
        return "Controls:\n\nw = up, s = down, a = left, d = right\nq = quit, e = Hint\n\nNote: Just avoid the arrow keys\n";
    }

    public void gameOver() {}
}
