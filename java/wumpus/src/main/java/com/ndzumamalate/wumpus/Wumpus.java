/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ndzumamalate.wumpus;

/**
 *
 * @author malate
 */
public class Wumpus {

    public static void main(String[] args) {
        Game game = new Game();
        Display dis = new Display();
        game.setDisplay(dis);
        game.startGame();
        //System.out.println();
    }
}
