/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectbattleship;

/**
 *
 * @author jp570
 */
public class GameState {
    private static String playerName;
    private static String difficulty;

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        GameState.playerName = playerName;
    }

    public static String getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(String difficulty) {
        GameState.difficulty = difficulty;
    }
}
