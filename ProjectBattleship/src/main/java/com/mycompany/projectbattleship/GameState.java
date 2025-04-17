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

    public enum Phase {
        PLACE_P1, PLACE_P2, ATTACK_P1, ATTACK_P2, GAME_OVER
    }

    private static CPUPlayer cpuPlayer;
    private static String player1Name;
    private static String player2Name;
    private static String difficulty;
    private static String gameMode;
    private static int currentPlayer = 1;
    private static Phase currentPhase = Phase.PLACE_P1;

    private static int[][] player1Board;
    private static int[][] player2Board;

    private static int boardSize = 8;
    private static int remainingShots = 1;
    
    private static boolean shotsAssignedThisTurn = false;

    public static void initBoards(int size) {
        boardSize = size;
        player1Board = new int[boardSize][boardSize];
        player2Board = new int[boardSize][boardSize];
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                
                player1Board[row][col] = 0;
                player2Board[row][col] = 0;
                
            }
        }
    }

    public static int getBoardSize() {
        return boardSize;
    }

    public static void placeShip(int row, int col, int size, String orientation, int player) {
        int[][] board = (player == 1) ? player1Board : player2Board;
        for (int i = 0; i < size; i++) {
            int r = row, c = col;
            if ("HORIZONTAL".equals(orientation)) c += i;
            else if ("VERTICAL".equals(orientation)) r += i;
            else if ("REVERSE_HORIZONTAL".equals(orientation)) c -= i;
            else if ("REVERSE_VERTICAL".equals(orientation)) r -= i;
            board[r][c] = size;
        }
    } 

    public static int attack(int attacker, int row, int col) {
        int[][] enemyBoard = (attacker == 1) ? player2Board : player1Board;
        int value = enemyBoard[row][col];
        if (value > 0) {
            enemyBoard[row][col] = -2;
            return -2;
        } else if (value == 0) {
            enemyBoard[row][col] = -1;
            return -1;
        }
        return 0;
    }

    public static void useShot(){
        if (remainingShots > 0) remainingShots--;
    }
    
    public static void resetShotsForTurn(String difficulty){
        if (shotsAssignedThisTurn) return;
        
        switch (difficulty) {
            case "FACIL":
                remainingShots = 3;
                break;
            case "MEDIO":
                remainingShots = 2;
                break;
            default:
                remainingShots = 1;
                break;
        }
        
        shotsAssignedThisTurn = true;
    }
    
    public static boolean isPvC() {
        return "Jugador vs CPU".equalsIgnoreCase(gameMode);
    }
    
    public static void resetShotFlag() {
        shotsAssignedThisTurn = false;
    }
    
    public static int getBoardCell(int player, int row, int col) {
        int[][] board = (player == 1) ? player1Board : player2Board;
        return board[row][col];
    }

    public static void setBoardCell(int player, int row, int col, int value) {
        int[][] board = (player == 1) ? player1Board : player2Board;
        board[row][col] = value;
    }

    public static void setCpuPlayer(CPUPlayer cpu){
        cpuPlayer = cpu;
    }
    
    public static CPUPlayer getCpuPlayer() {
        return cpuPlayer;
    }
    
    public static String getPlayer1Name() { return player1Name; }
    public static void setPlayer1Name(String name) { player1Name = name; }

    public static String getPlayer2Name() { return player2Name; }
    public static void setPlayer2Name(String name) { player2Name = name; }

    public static String getDifficulty() { return difficulty; }
    public static void setDifficulty(String diff) { difficulty = diff; }

    public static int getCurrentPlayer() { return currentPlayer; }
    public static void setCurrentPlayer(int player) { currentPlayer = player; }

    public static Phase getCurrentPhase() { return currentPhase; }
    public static void setCurrentPhase(Phase phase) { currentPhase = phase; }

    public static void setGameMode(String mode) { gameMode = mode; }
    public static String getGameMode() { return gameMode; }
    
    public static int getRemainingShots() { return remainingShots; }
    public static void setRemainingShots(int shots) { remainingShots = shots; }
}
