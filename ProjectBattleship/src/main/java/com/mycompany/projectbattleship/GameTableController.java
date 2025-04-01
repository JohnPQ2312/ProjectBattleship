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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class GameTableController implements Initializable {

    @FXML
    private RadioButton rbSubmarine, rbDestroyer, rbCruiser, rbBattleship;
    private ToggleGroup shipGroup;
    private Ship selectedShip;
    private String selectedOrientation = "HORIZONTAL";

    @FXML
    private AnchorPane rootPane;
    @FXML
    private GridPane gridPane;
    
    private static int boardSize = 5;

    public static void setBoardSize(int size) {
        boardSize = size;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shipGroup = new ToggleGroup();
        rbSubmarine.setToggleGroup(shipGroup);
        rbDestroyer.setToggleGroup(shipGroup);
        rbCruiser.setToggleGroup(shipGroup);
        rbBattleship.setToggleGroup(shipGroup);

        shipGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rbSubmarine) selectedShip = new Ship("Submarine", 1);
            else if (newValue == rbDestroyer) selectedShip = new Ship("Destroyer", 2);
            else if (newValue == rbCruiser) selectedShip = new Ship("Cruiser", 3);
            else if (newValue == rbBattleship) selectedShip = new Ship("Battleship", 4);
        });

        createBoard(boardSize);
        Platform.runLater(() -> centerBoard());
    }

    private void handleCellClick(ActionEvent event) {
        if (selectedShip == null) return;
        Button btn = (Button) event.getSource();
        String[] coords = btn.getId().split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);
        
        if (canPlaceShip(row, col, selectedShip, selectedOrientation)) {
            placeShip(row, col, selectedShip, selectedOrientation);
            return;
        }
        
        String oppositeOrientation = (selectedOrientation.equals("HORIZONTAL")) ? "REVERSE_HORIZONTAL" : "REVERSE_VERTICAL";
        if (canPlaceShip(row, col, selectedShip, oppositeOrientation)) {
            placeShip(row, col, selectedShip, oppositeOrientation);
        }
    }

    private boolean canPlaceShip(int row, int col, Ship ship, String orientation) {
        int size = ship.getSize();
        if (orientation.equals("HORIZONTAL")) {
            if (col + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row, col + i)) return false;
            }
        } else if (orientation.equals("REVERSE_HORIZONTAL")) {
            if (col - size + 1 < 0) return false;
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row, col - i)) return false;
            }
        } else if (orientation.equals("VERTICAL")) {
            if (row + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row + i, col)) return false;
            }
        } else if (orientation.equals("REVERSE_VERTICAL")) {
            if (row - size + 1 < 0) return false;
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row - i, col)) return false;
            }
        }
        return true;
    }

    private void placeShip(int row, int col, Ship ship, String orientation) {
        int size = ship.getSize();
        String shipValue = String.valueOf(size);

        for (int i = 0; i < size; i++) {
            int newRow = row;
            int newCol = col;

            if (orientation.equals("HORIZONTAL")) {
                newCol = col + i;
            } else if (orientation.equals("REVERSE_HORIZONTAL")) {
                newCol = col - i;
            } else if (orientation.equals("VERTICAL")) {
                newRow = row + i;
            } else if (orientation.equals("REVERSE_VERTICAL")) {
                newRow = row - i;
            }

            Button cell = getCellButton(newRow, newCol);
            if (cell != null) {
                cell.setText(shipValue);
            }
        }
    }

    private boolean isCellAvailable(int row, int col) {
        Button cell = getCellButton(row, col);
        return cell != null && cell.getText().isEmpty();
    }

    private Button getCellButton(int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }

    private void createBoard(int size) {
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        for (int i = 0; i < size; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / size);
            colConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / size);
            rowConstraints.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button button = new Button();
                button.setId(row + "," + col);

                if (size == 8) {
                    button.setPrefSize(60, 60);
                } else if (size == 12) {
                    button.setPrefSize(50, 50);
                } else if (size == 16) {
                    button.setPrefSize(40, 40);
                }

                gridPane.add(button, col, row);
                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);
                button.setOnAction(event -> handleCellClick(event));
            }
        }
    }

    private void centerBoard() {
        double paneWidth = rootPane.getWidth();
        double paneHeight = rootPane.getHeight();
        double gridWidth = gridPane.getBoundsInParent().getWidth();
        double gridHeight = gridPane.getBoundsInParent().getHeight();
        gridPane.setLayoutX((paneWidth - gridWidth) / 2);
        gridPane.setLayoutY((paneHeight - gridHeight) / 2);
    }
}
