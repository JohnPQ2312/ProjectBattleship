/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author jp570
 */
public class CPUPlayer {
    public enum Difficulty { FACIL, MEDIO, DIFICIL }

    private final Difficulty difficulty;
    private final int boardSize;
    private final Random rand = new Random();
    private final List<Point> availableShots = new ArrayList<>(); //Array for saving all available coords


    private boolean huntMode = false;
    private Point lastHit = null;
    private final Deque<Point> huntTargets = new ArrayDeque<>(); //Array Deque for saving neighbor cells of the hit one

    public CPUPlayer(String diffStr, int boardSize) { //CPU constructor
        this.difficulty = Difficulty.valueOf(diffStr.toUpperCase());
        this.boardSize = boardSize;

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                availableShots.add(new Point(r, c));
            }
        }
    }

    public void placeAllShips() { //Placing ships on logic board
        int[] counts = {4, 3, 2, 1}; //number of ships (submarines, destructors, cruisers, battleship)
        String[] orientations = {"HORIZONTAL", "REVERSE_HORIZONTAL", "VERTICAL", "REVERSE_VERTICAL"};
        for (int size = 1; size <= counts.length; size++) {
            int qty = counts[size - 1]; //qty = ship size
            for (int i = 0; i < qty; i++) { //Ship placement on random coordinate
                boolean placed = false;
                while (!placed) {
                    int r = rand.nextInt(boardSize);
                    int c = rand.nextInt(boardSize);
                    String dir = orientations[rand.nextInt(orientations.length)];
                    if (canPlace(r, c, size, dir)) {
                        GameState.placeShip(r, c, size, dir, 2);
                        placed = true;
                    }
                }
            }
        }
    }
    
    public int[] makeMove() { //CPU shotting method
        if (availableShots.isEmpty()) return new int[]{-1, -1};
        Point shot;
        switch (difficulty) {
            case FACIL:
                shot = randomShot(); //Random shot
                break;
            case MEDIO:
                shot = mediumShot(); //Random shot, if hit, search neighbor cells
                break;
            case DIFICIL:
                shot = hardShot(); // "Chess pattern" + hunting mode
                break;
            default:
                shot = randomShot();
        }
        availableShots.remove(shot);

        int result = GameState.attack(2, shot.x, shot.y);
        if (result == -2) { //If hit, activates huntMode and calls enqueueNeighbors on shot coordinate
            huntMode = true;
            lastHit = shot;
            enqueueNeighbors(shot);
        }
        return new int[]{shot.x, shot.y};
    }

    private Point randomShot() { //Completely random shot
        return availableShots.get(rand.nextInt(availableShots.size()));
    }

    private Point mediumShot() {//Random shot, if hit, search neighbor cells
        if (huntMode && !huntTargets.isEmpty()) {
            Point tgt = huntTargets.poll();
            if (availableShots.contains(tgt)) return tgt;
            else return mediumShot(); //If target is already shotted, it searchs another target on the array
        }
        return randomShot();
    }

    private Point hardShot() {// "Chess pattern" + hunting mode
        if (huntMode) {
            return mediumShot();
        }
        for (Point p : new ArrayList<>(availableShots)) {
            if ((p.x + p.y) % 2 == 0) {
                return p;
            }
        }
        return randomShot();
    }

    private void enqueueNeighbors(Point p) { //Search neighbor cells of the assigned coordinate and saves it on a dedicated Array
        int r = p.x, c = p.y;
        if (r > 0) huntTargets.add(new Point(r - 1, c));
        if (r < boardSize - 1) huntTargets.add(new Point(r + 1, c));
        if (c > 0) huntTargets.add(new Point(r, c - 1));
        if (c < boardSize - 1) huntTargets.add(new Point(r, c + 1));
    }

    private boolean canPlace(int row, int col, int size, String orientation) { //It verifies if the ship selected can be placed on the coordinate (or coordinates)
        for (int i = 0; i < size; i++) {
            int r = row;
            int c = col;
            switch (orientation) {
                case "HORIZONTAL": c += i; break;
                case "REVERSE_HORIZONTAL": c -= i; break;
                case "VERTICAL": r += i; break;
                case "REVERSE_VERTICAL": r -= i; break;
            }
            if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false; //Can't be placed outside the board limits
            if (GameState.getBoardCell(2, r, c) != 0) return false; // Can't be placed if one of the cells is occupied
        }
        return true;
    }
}


