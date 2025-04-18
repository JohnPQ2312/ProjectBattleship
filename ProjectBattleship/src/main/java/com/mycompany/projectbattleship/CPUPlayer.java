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
    private final List<Point> availableShots = new ArrayList<>();


    private boolean huntMode = false;
    private Point lastHit = null;
    private final Deque<Point> huntTargets = new ArrayDeque<>();

    public CPUPlayer(String diffStr, int boardSize) {
        this.difficulty = Difficulty.valueOf(diffStr.toUpperCase());
        this.boardSize = boardSize;

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                availableShots.add(new Point(r, c));
            }
        }
    }

    public void placeAllShips() {
        int[] counts = {4, 3, 2, 1};
        String[] orientations = {"HORIZONTAL", "REVERSE_HORIZONTAL", "VERTICAL", "REVERSE_VERTICAL"};
        for (int size = 1; size <= counts.length; size++) {
            int qty = counts[size - 1];
            for (int i = 0; i < qty; i++) {
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
    
    public int[] makeMove() {
        if (availableShots.isEmpty()) return new int[]{-1, -1};
        Point shot;
        switch (difficulty) {
            case FACIL:
                shot = randomShot();
                break;
            case MEDIO:
                shot = mediumShot();
                break;
            case DIFICIL:
                shot = hardShot();
                break;
            default:
                shot = randomShot();
        }
        availableShots.remove(shot);

        int result = GameState.attack(2, shot.x, shot.y);
        if (result == -2) {
            huntMode = true;
            lastHit = shot;
            enqueueNeighbors(shot);
        }
        return new int[]{shot.x, shot.y};
    }

    private Point randomShot() {
        return availableShots.get(rand.nextInt(availableShots.size()));
    }

    private Point mediumShot() {
        if (huntMode && !huntTargets.isEmpty()) {
            Point tgt = huntTargets.poll();
            if (availableShots.contains(tgt)) return tgt;
            else return mediumShot();
        }
        return randomShot();
    }

    private Point hardShot() {
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

    private void enqueueNeighbors(Point p) {
        int r = p.x, c = p.y;
        if (r > 0) huntTargets.add(new Point(r - 1, c));
        if (r < boardSize - 1) huntTargets.add(new Point(r + 1, c));
        if (c > 0) huntTargets.add(new Point(r, c - 1));
        if (c < boardSize - 1) huntTargets.add(new Point(r, c + 1));
    }

    private boolean canPlace(int row, int col, int size, String orientation) {
        for (int i = 0; i < size; i++) {
            int r = row;
            int c = col;
            switch (orientation) {
                case "HORIZONTAL": c += i; break;
                case "REVERSE_HORIZONTAL": c -= i; break;
                case "VERTICAL": r += i; break;
                case "REVERSE_VERTICAL": r -= i; break;
            }
            if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
            if (GameState.getBoardCell(2, r, c) != 0) return false;
        }
        return true;
    }
}


