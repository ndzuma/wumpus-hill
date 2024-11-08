package com.ndzumamalate.wumpus;

public class Player {
    private int xPosition;
    private int yPosition;
    private String icon = "🧍🏽‍♂️";

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getIcon() {
        return icon;
    }
}
