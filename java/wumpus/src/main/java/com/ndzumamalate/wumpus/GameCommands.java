package com.ndzumamalate.wumpus;

public enum GameCommands {
    QUIT("q"),
    HELP("e"),
    RESTART("r");

    private final String command;

    GameCommands(String command) {
        this.command = command;
    }

    public boolean matches(String input) {
        return input.equalsIgnoreCase(command);
    }
}
