package com.ndzumamalate.wumpus;

public class Position {
    private int x;
    private int y;
    private boolean hasPlayer;
    private boolean hasWumpus;
    private boolean hasPit;
    private boolean hasBreeze;
    private boolean hasStench;
    private boolean hasGold;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasPlayer = false;
        this.hasWumpus = false;
        this.hasPit = false;
        this.hasBreeze = false;
        this.hasStench = false;
        this.hasGold = false;
    }

    public boolean hasWumpus() {
        return hasWumpus;
    }

    public void setWumpus(boolean hasWumpus) {
        if (!this.hasPit) {
            this.hasWumpus = hasWumpus;
        }
    }

    public boolean hasPit() {
        return hasPit;
    }

    public void setPit(boolean hasPit) {
        if (!this.hasWumpus) {
            this.hasPit = hasPit;
        }
    }

    public boolean hasBreeze() {
        return hasBreeze;
    }

    public void setBreeze(boolean hasBreeze) {
        this.hasBreeze = hasBreeze;
    }

    public boolean hasStench() {
        return hasStench;
    }

    public void setStench(boolean hasStench) {
        this.hasStench = hasStench;
    }

    public boolean hasGold() {
        return hasGold;
    }

    public void setGold(boolean hasGold) {
        if (!this.hasPit && !this.hasWumpus) {
            this.hasGold = hasGold;
        }
    }

    public boolean hasHurdles() {
        if (this.hasWumpus || this.hasPit) {
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasPlayer() {
        return hasPlayer;
    }

    public void setPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public void resetRoom() {
        hasWumpus = false;
        hasPit = false;
        hasStench = false;
        hasBreeze = false;
        hasGold = false;
        hasPlayer = false;
    }
}
