package com.ndzumamalate.wumpus;

import java.io.IOException;
import java.util.Scanner;

public class GameInput {
    public static String getNormalInput() {
         Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toLowerCase();
    }

    public static String getImmediateInput() {
        String input = "";
        try {
            // Check if there's a character available to read
            if (System.in.available() > 0) {
                input = String.valueOf((char) System.in.read()).toLowerCase();  // Convert to string and make lowercase
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
