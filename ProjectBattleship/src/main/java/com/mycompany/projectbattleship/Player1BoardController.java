/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import com.mycompany.projectbattleship.GameState.Phase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


/**
 * FXML Controller class
 *
 * @author jp570
 */
public class Player1BoardController extends GameTableController { //P1 Board
    
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
    private int currentPlayer = 1;
    
    public void printTableTest(){ //Print second board on console for logic verification
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                
                System.out.print(GameState.getBoardCell(2, row, col) + " ");
                        
            }
            System.out.println();
        }
        System.out.println();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameState.setCurrentPlayer(currentPlayer);
        Phase phase = GameState.getCurrentPhase();

        playerNameField.setText(GameState.getPlayer1Name());
        playerNameField.setEditable(false);
        difficultyField.setText(GameState.getDifficulty());
        difficultyField.setEditable(false);

        boolean isPlacement = (phase == Phase.PLACE_P1);
        setupShipButtons();
        btnRotate.setDisable(!isPlacement);
        rbSubmarine.setDisable(!isPlacement);
        rbDestroyer.setDisable(!isPlacement);
        rbCruiser.setDisable(!isPlacement);
        rbBattleship.setDisable(!isPlacement);
        
        subCount.setVisible(isPlacement);
        destCount.setVisible(isPlacement);
        cruisCount.setVisible(isPlacement);
        b_shipCount.setVisible(isPlacement);
        orientation.setVisible(isPlacement);  
        
        btnChangeView.setDisable(isPlacement);
        btnPrintTableTest.setDisable(true); //Disable print second board during PvP
              
        
        if (isPlacement) { //Checks if phase is placement or not
            updateShipLabels();
            createBoard(boardSize);
            
            if (GameState.isPvC()) {
               btnChangePlayer.setDisable(true);
            } else {
               btnChangePlayer.setDisable(false);
            }  
        } else {
            createObservationBoard(boardSize, GameState.getCurrentPlayer()); //If it's not placement, it creates an observation board for attack phase
            
            if (GameState.isPvC()) {
               btnChangePlayer.setDisable(true);
               btnPrintTableTest.setDisable(false); //Enables print second board during PvC
            } else {
               btnChangePlayer.setDisable(false);
            }  
        }    
        
        btnChangePlayer.setDisable(true);
        
        Platform.runLater(() -> {
            rootPane.widthProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            centerBoard();
        });
        
    }

    private void createObservationBoard(int size, int player) { //Read-only board showing own ships and shots of the enemy
        //(For bug fix) Hide the change player button
        btnChangePlayer.setVisible(false);
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < size; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/size);
            gridPane.getColumnConstraints().add(cc);
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/size);
            gridPane.getRowConstraints().add(rc);
        }
            
        for (int r = 0; r < size; r++) { //Create visible but disabled buttons, map-type for observating each own board
            for (int c = 0; c < size; c++) {
                Button cell = new Button();
                cell.setId(r + "," + c);
                cell.setPrefSize(getCellSize(size), getCellSize(size));

                int val = GameState.getBoardCell(player, r, c);
                if (val > 0) {
                    Ship dummyShip = new Ship("Temp", val);
                    cell.setText(String.valueOf(val));
                    cell.setStyle("-fx-background-color: " + getShipColor(dummyShip) + ";" + " -fx-text-fill: transparent;");
                } else if (val == -1) {
                    cell.setStyle("-fx-background-color: #1100ff;");
                } else if (val == -2){
                    cell.setStyle("-fx-background-color: #1c1c1c;");
                } else {
                    cell.setStyle("-fx-background-color: lightblue;");
                }
                cell.setDisable(true);
                GridPane.setHalignment(cell, HPos.CENTER);
                GridPane.setValignment(cell, VPos.CENTER);
                gridPane.add(cell, c, r);
            }
        }
    }    
    
    private void setupShipButtons() { //Selected ship radio buttons
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
    
    private void updateShipLabels() { //Update ship lebels for showing remaining ships to be placed
        subCount.setText("Submarinos: " + remainingSubmarines);
        destCount.setText("Destructores: " + remainingDestroyers);
        cruisCount.setText("Cruceros: " + remainingCruisers);
        b_shipCount.setText("Acorazados: " + remainingBattleships);
        orientation.setText("Orientación: " + selectedOrientation);
    }
    
    @FXML
    private void rotateShip() { //Ship rotation on placement
        if (selectedShip != null) {
            selectedShip.rotar();
            selectedOrientation = selectedShip.isHorizontal() ? "HORIZONTAL" : "VERTICAL";
            orientation.setText("Orientación: " + selectedOrientation);
        }
    }
    
    @FXML
    protected void handleCellClick(ActionEvent event) { //Cell click for board placement
        if (selectedShip == null || !hasRemainingShip(selectedShip)) return;

        Button btn = (Button) event.getSource();
        String[] coords = btn.getId().split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        if (canPlaceShip(row, col, selectedShip, selectedOrientation)) { //Try normal placement
            placeShip(row, col, selectedShip, selectedOrientation);
            GameState.placeShip(row, col, selectedShip.getSize(), selectedOrientation, currentPlayer);
            updateShipCount(selectedShip);
            checkIfPlacementFinished(); 
            return;
        }
        
        String oppositeOrientation = (selectedOrientation.equals("HORIZONTAL")) ? "REVERSE_HORIZONTAL" : "REVERSE_VERTICAL"; //Try reversed orientation
        if (canPlaceShip(row, col, selectedShip, oppositeOrientation)) {
            placeShip(row, col, selectedShip, oppositeOrientation);
            GameState.placeShip(row, col, selectedShip.getSize(), oppositeOrientation, currentPlayer);
            updateShipCount(selectedShip);
            checkIfPlacementFinished();            
        }
    }
    
    private void checkIfPlacementFinished() { //Checks if all ships are placed, if it's PvC, it places all CPU ships
                                              //In PvP, it activates change player button to let P2 place ships
       if (remainingSubmarines == 0 && remainingDestroyers == 0 &&
           remainingCruisers == 0 && remainingBattleships == 0) {

           if (GameState.isPvC()) {
               GameState.getCpuPlayer().placeAllShips();
               GameState.setCurrentPhase(GameState.Phase.ATTACK_P1);
               
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Preparación del CPU");
                alert.setHeaderText(null);
                alert.setContentText("El CPU ha colocado sus barcos. Comienza tu turno.");
                alert.showAndWait();
               
               try {
                   App.setRoot("player1Attack");
               } catch (IOException e) {
                   e.printStackTrace();
               }
            } else {
               btnChangePlayer.setDisable(false);
            }
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

    private void updateShipCount(Ship ship) { //Update ship counter for the placed type
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

    private boolean canPlaceShip(int row, int col, Ship ship, String orientation) { //It verifies if the ship selected can be placed on the coordinate (or coordinates)
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

    private void placeShip(int row, int col, Ship ship, String orientation) { //Places the ship on the selected coordinate (or coordinates)
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

    private String getShipColor(Ship ship) { //Ship color
        switch (ship.getSize()) {
            case 1: return "#ff6666";
            case 2: return "#66b3ff";
            case 3: return "#99ff99";
            case 4: return "#ffcc66";
            default: return "#ffffff";
        }
    }   
    
    @FXML
    private void handleChangeScreen() throws IOException {
        App.setRoot("Player1Attack");
    }

    @FXML
    private void handleChangePlayer() throws IOException { //Change screen to P2's board or attack
        GameState.setCurrentPlayer(2);

        if (GameState.getCurrentPhase() == GameState.Phase.PLACE_P1) {
            GameState.setCurrentPhase(GameState.Phase.PLACE_P2);
            App.setRoot("Player2Board");
        } else if (GameState.getCurrentPhase() == GameState.Phase.ATTACK_P1) {
            GameState.setCurrentPhase(GameState.Phase.ATTACK_P2);
            App.setRoot("Player2Board");
        }
    }
}
