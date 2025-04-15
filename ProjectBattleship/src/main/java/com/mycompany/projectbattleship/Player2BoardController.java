/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author jp570
 */

public class Player2BoardController extends GameTableController {
    
    @FXML private RadioButton rbSubmarine, rbDestroyer, rbCruiser, rbBattleship;
    @FXML private Label orientation, subCount, destCount, cruisCount, b_shipCount;
    @FXML private Button btnRotate, btnChangeView, btnChangePlayer, btnPrintTableTest;
    private ToggleGroup shipGroup;
    private Ship selectedShip;
    private String selectedOrientation = "HORIZONTAL";
        
    private int remainingSubmarines = 4;
    private int remainingDestroyers = 3;
    private int remainingCruisers = 2;
    private int remainingBattleships = 1;
    private int currentPlayer = 2;
    
    public void printTableTest(){ //Este metodo es para probar si el tablero se guarda logicamente
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                
                System.out.print(GameState.getBoardCell(currentPlayer, row, col) + " ");
                        
            }
            System.out.println();
        }
        System.out.println();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameState.setCurrentPlayer(currentPlayer);

        playerNameField.setText(GameState.getPlayer2Name());
        playerNameField.setEditable(false);
        difficultyField.setText(GameState.getDifficulty());
        difficultyField.setEditable(false);

        setupShipButtons();
        updateShipLabels();
        
        createBoard(boardSize);
        Platform.runLater(this::centerBoard);
        
    }    
    
    private void setupShipButtons() {
        shipGroup = new ToggleGroup();
        rbSubmarine.setToggleGroup(shipGroup);
        rbDestroyer.setToggleGroup(shipGroup);
        rbCruiser.setToggleGroup(shipGroup);
        rbBattleship.setToggleGroup(shipGroup);

        shipGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbSubmarine) selectedShip = new Ship("Submarine", 1);
            else if (newVal == rbDestroyer) selectedShip = new Ship("Destroyer", 2);
            else if (newVal == rbCruiser) selectedShip = new Ship("Cruiser", 3);
            else if (newVal == rbBattleship) selectedShip = new Ship("Battleship", 4);
        });
    }
    
    private void updateShipLabels() {
        subCount.setText("Submarinos: " + remainingSubmarines);
        destCount.setText("Destructores: " + remainingDestroyers);
        cruisCount.setText("Cruceros: " + remainingCruisers);
        b_shipCount.setText("Acorazados: " + remainingBattleships);
        orientation.setText("Orientación: " + selectedOrientation);
    }
    
    @FXML
    private void rotateShip() {
        if (selectedShip != null) {
            selectedShip.rotar();
            selectedOrientation = selectedShip.isHorizontal() ? "HORIZONTAL" : "VERTICAL";
            orientation.setText("Orientación: " + selectedOrientation);
        }
    }
    
    @FXML
    protected void handleCellClick(ActionEvent event) {
        if (selectedShip == null || !hasRemainingShip(selectedShip)) return;

        Button btn = (Button) event.getSource();
        String[] coords = btn.getId().split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        if (canPlaceShip(row, col, selectedShip, selectedOrientation)) {
            placeShip(row, col, selectedShip, selectedOrientation);
            GameState.placeShip(row, col, selectedShip.getSize(), selectedOrientation, currentPlayer);
            updateShipCount(selectedShip);
            return;
        }
        
        String oppositeOrientation = (selectedOrientation.equals("HORIZONTAL")) ? "REVERSE_HORIZONTAL" : "REVERSE_VERTICAL";
        if (canPlaceShip(row, col, selectedShip, oppositeOrientation)) {
            placeShip(row, col, selectedShip, oppositeOrientation);
            GameState.placeShip(row, col, selectedShip.getSize(), oppositeOrientation, currentPlayer);
            updateShipCount(selectedShip);            
        }
    }
    
    private boolean hasRemainingShip(Ship ship) {
        switch(ship.getName()) {
            case "Submarine": return remainingSubmarines > 0;
            case "Destroyer": return remainingDestroyers > 0;
            case "Cruiser":   return remainingCruisers > 0;
            case "Battleship": return remainingBattleships > 0;
            default: return false;
        }
    }

    private void updateShipCount(Ship ship) {
        switch(ship.getName()) {
            case "Submarine":
                remainingSubmarines--;
                subCount.setText("Submarinos: " + remainingSubmarines);
                if(remainingSubmarines <= 0) rbSubmarine.setDisable(true);
                break;
            case "Destroyer":
                remainingDestroyers--;
                destCount.setText("Destructores: " + remainingDestroyers);
                if(remainingDestroyers <= 0) rbDestroyer.setDisable(true);
                break;
            case "Cruiser":
                remainingCruisers--;
                cruisCount.setText("Cruceros: " + remainingCruisers);
                if(remainingCruisers <= 0) rbCruiser.setDisable(true);
                break;
            case "Battleship":
                remainingBattleships--;
                b_shipCount.setText("Acorazados: " + remainingBattleships);
                if(remainingBattleships <= 0) rbBattleship.setDisable(true);
                break;
        }
    }

    private boolean canPlaceShip(int row, int col, Ship ship, String orientation) {
        int size = ship.getSize();
        switch (orientation) {
            case "HORIZONTAL":
                if (col + size > boardSize) return false;
                for (int i = 0; i < size; i++) {
                    if (!isCellAvailable(row, col + i)) return false;
                }   break;
            case "REVERSE_HORIZONTAL":
                if (col - size + 1 < 0) return false;
                for (int i = 0; i < size; i++) {
                    if (!isCellAvailable(row, col - i)) return false;
                }   break;
            case "VERTICAL":
                if (row + size > boardSize) return false;
                for (int i = 0; i < size; i++) {
                    if (!isCellAvailable(row + i, col)) return false;
                }   break;
            case "REVERSE_VERTICAL":
                if (row - size + 1 < 0) return false;
                for (int i = 0; i < size; i++) {
                    if (!isCellAvailable(row - i, col)) return false;
                }   break;
            default:
                break;
        }
        return true;
    }

    private void placeShip(int row, int col, Ship ship, String orientation) {
        int size = ship.getSize();
        String shipValue = String.valueOf(size);
        String shipColor = getShipColor(ship);

        for (int i = 0; i < size; i++) {
            int r = row, c = col;
            if (orientation.equals("HORIZONTAL")) c += i;
            else if (orientation.equals("REVERSE_HORIZONTAL")) c -= i;
            else if (orientation.equals("VERTICAL")) r += i;
            else if (orientation.equals("REVERSE_VERTICAL")) r -= i;

            Button cell = getCellButton(r, c);
            if (cell != null) {
                cell.setText(shipValue);
                cell.setStyle("-fx-background-color: " + shipColor + "; -fx-text-fill: transparent;");
            }
        }
    }

    private String getShipColor(Ship ship) {
        switch (ship.getSize()) {
            case 1: return "#ff6666"; // Rojo para submarino
            case 2: return "#66b3ff"; // Azul para destructor
            case 3: return "#99ff99"; // Verde para crucero
            case 4: return "#ffcc66"; // Amarillo para acorazado
            default: return "#ffffff"; // Blanco por defecto
        }
    }    
}