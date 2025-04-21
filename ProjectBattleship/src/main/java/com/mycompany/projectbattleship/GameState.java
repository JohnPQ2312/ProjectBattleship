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
        PLACE_P1, PLACE_P2, ATTACK_P1, ATTACK_P2, EXTRA_TURN, GAME_OVER
    }

    private static CPUPlayer cpuPlayer;
    private static String player1Name;
    private static String player2Name;
    private static String difficulty;
    private static String gameMode;
    private static int currentPlayer = 1;
    private static Phase currentPhase = Phase.PLACE_P1;
    
    private static int hitsP1 = 0, hitsP2 = 0;
    private static int shotsTakenP1 = 0, shotsTakenP2 = 0;
    private static int shotLimit = 0;
    private static boolean extraShotPhase = false;
    private static int firstMover = 1;
    private static int winner;
    
    private static final int TOTAL_SHIP_CELLS = 20; //(total ships * ship cells = 4*1 + 3*2 + 2*3 + 1*4 = 20 cells)

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
        int result;

        int value = enemyBoard[row][col];
        if (value > 0) {
            enemyBoard[row][col] = -2;
            result = -2;
        } else if (value == 0) {
            enemyBoard[row][col] = -1;
            result = -1;
        } else {
            result = 0;
        }

        if (attacker == 1) shotsTakenP1++;
        else               shotsTakenP2++;

        if (result == -2) {
            if (attacker == 1) hitsP1++;
            else               hitsP2++;
        }

        evaluateEndGame(attacker);

        return result;
    }

    private static void evaluateEndGame(int attacker){
        if (shotLimit == 0) {
        shotLimit = calculateShotLimit();
        }    
        
        int myHits   = (attacker == 1) ? hitsP1   : hitsP2;
        int oppHits  = (attacker == 1) ? hitsP2   : hitsP1;
        int myShots  = (attacker == 1) ? shotsTakenP1 : shotsTakenP2;
        int oppShots = (attacker == 1) ? shotsTakenP2 : shotsTakenP1;
        int other    = (attacker == 1) ? 2 : 1;
        
        if (myHits >= TOTAL_SHIP_CELLS){
            if (attacker == firstMover && !extraShotPhase){
                extraShotPhase = true;
                currentPlayer = other;
                currentPhase = Phase.EXTRA_TURN; 
            } else{  
                if (hitsP1 == hitsP2) {
                    currentPhase = Phase.GAME_OVER;
                    winner = 0;
                } else {
                    currentPhase = Phase.GAME_OVER;
                    winner = (hitsP1 > hitsP2) ? 1 : 2;
                }
            }
            
            return;
        }
        
        if (shotLimit > 0
         && shotsTakenP1 >= shotLimit
         && shotsTakenP2 >= shotLimit) {
            if (hitsP1 == hitsP2) {
                currentPhase = Phase.GAME_OVER;
                winner = 0;
            } else {
                currentPhase = Phase.GAME_OVER;
                winner = (hitsP1 > hitsP2) ? 1 : 2;
            }
        }
    }
    
    private static int calculateShotLimit() {
        switch (difficulty.toUpperCase()) {
            case "FACIL": return 50;
            case "MEDIO": return 40;
            case "DIFICIL": return 30;
            default: return 0;
        }
    }

    public static void useShot(){
        if (remainingShots > 0) remainingShots--;
    }
    
    public static int manualGetShots(String difficulty){
        switch (difficulty) {
            case "FACIL":
                return 3;
            case "MEDIO":
                return 2;
            default:
                return 1;
        }
    }
    
    public static void resetShotsForTurn(String difficulty){
        if (shotsAssignedThisTurn) return;
        
        if (currentPhase == Phase.EXTRA_TURN) {
            remainingShots = 1;
            return;
        }
        
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
    
    public static String getAccuracy(int player) {
        int shots = getShotsTaken(player);
        int hits = getHits(player);
        if (shots == 0) return "0.0%";
        double accuracy = (hits * 100.0) / shots;
        return String.format("%.1f%%", accuracy);
    }
    
    public static void resetAll() {
        player1Name = "";
        player2Name = "";
        difficulty = "";
        gameMode = "";
        currentPlayer = 1;
        currentPhase = Phase.PLACE_P1;
        remainingShots = 1;
        shotsAssignedThisTurn = false;

        cpuPlayer = null;
        winner = -1;
        hitsP1 = 0;
        hitsP2 = 0;
        shotsTakenP1 = 0;
        shotsTakenP2 = 0;
        extraShotPhase = false;
        firstMover = 1;

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
    
    public static int getWinner() {
        return winner;
    }

    public static int getShotsTaken(int player) {
        return (player == 1) ? shotsTakenP1 : shotsTakenP2;
    }

    public static int getHits(int player) {
        return (player == 1) ? hitsP1 : hitsP2;
    }

    public static int getShotLimit() {
        return shotLimit;
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
