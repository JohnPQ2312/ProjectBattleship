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
import javafx.scene.control.TextField;

public abstract class GameTableController implements Initializable{
    @FXML protected AnchorPane rootPane;
    @FXML protected GridPane gridPane;
    @FXML protected TextField playerNameField, difficultyField;
    
    protected static int boardSize = 8;
    protected abstract void handleCellClick(ActionEvent event);
    
    public static void setBoardSize(int size) {
        boardSize = size;
    }

    protected boolean isCellAvailable(int row, int col) {
        Button cell = getCellButton(row, col);
        return cell != null && cell.getText().isEmpty();
    }

    protected Button getCellButton(int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }
    
    protected void createBoard(int size) {
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
                button.setPrefSize(getCellSize(size), getCellSize(size));

                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);

                configureCellButton(button, row, col);
                
                gridPane.add(button, col, row);
            }
        }
    }
    
    protected int getCellSize(int size) {
        switch (size) {
            case 12: return 51;
            case 16: return 41;
            case 20: return 31;
            default: return 51;
        }
    }
    
    protected void configureCellButton(Button button, int row, int col) {
        button.setOnAction(event -> handleCellClick(event));
    }
    
    protected void centerBoard() {
        double paneWidth = rootPane.getWidth();
        double paneHeight = rootPane.getHeight();
        double gridWidth = gridPane.getBoundsInParent().getWidth();
        double gridHeight = gridPane.getBoundsInParent().getHeight();
        gridPane.setLayoutX((paneWidth - gridWidth) / 2);
        gridPane.setLayoutY((paneHeight - gridHeight) / 2);
    }
    
}
