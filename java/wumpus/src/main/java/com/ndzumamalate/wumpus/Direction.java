package com.ndzumamalate.wumpus;

public enum Direction {
    UP("up", "w"),
    DOWN("down", "s"),
    LEFT("left", "a"),
    RIGHT("right", "d");

    private final String arrowCommand;
    private final String wasdCommand;

    Direction(String arrowCommand, String wasdCommand) {
        this.arrowCommand = arrowCommand;
        this.wasdCommand = wasdCommand;
    }

    public String getArrowCommand() {
        return arrowCommand;
    }

    public String getWasdCommand() {
        return wasdCommand;
    }

    public boolean matches(String command) {
        return command.equalsIgnoreCase(arrowCommand) || command.equalsIgnoreCase(wasdCommand);
    }
}

