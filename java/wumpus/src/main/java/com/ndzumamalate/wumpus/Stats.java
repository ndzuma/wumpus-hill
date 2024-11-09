package com.ndzumamalate.wumpus;

public class Stats {
    private int points;
    private int goldAcquired;
    private int movesTaken;
    private int hintsUsed;

    public Stats() {
        this.points = 100;
        this.goldAcquired = 0;
        this.movesTaken = 0;
        this.hintsUsed = 0;
    }

    public int getPoints() {
        return points;
    }

    public int getGoldAcquired() {
        return goldAcquired;
    }

    public int getMovesTaken() {
        return movesTaken;
    }


    public int getHintsUsed() {
        return hintsUsed;
    }

    public void goldAcquired() {
        this.points += 1000;
        this.goldAcquired++;
    }

    public void moveTaken() {
        this.movesTaken++;
        this.points--;
    }

    public void hintUsed() {
        this.hintsUsed++;
    }

    public void foundHurdle() {
        this.points -= 10000;
    }
}
