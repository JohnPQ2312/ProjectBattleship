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

public abstract class GameTableController implements Initializable{ //Father class for board related methods (Visuals)
    @FXML protected AnchorPane rootPane;
    @FXML protected GridPane gridPane;
    @FXML protected TextField playerNameField, difficultyField;
    
    protected static int boardSize = 8;
    protected abstract void handleCellClick(ActionEvent event);
    
    
    
    public static void setBoardSize(int size) {
        boardSize = size;
    }

    protected boolean isCellAvailable(int row, int col) { //Checks if the cell selected is available for use
        Button cell = getCellButton(row, col);
        return cell != null && cell.getText().isEmpty();
    }

    protected Button getCellButton(int row, int col) { //Getter of selected button on gridpane
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }
    
    protected void createBoard(int size) { //Creates a gridpane using the size established earlier on the difficulty screen
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        for (int i = 0; i < size; i++) { //Row and col sizes
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
            for (int col = 0; col < size; col++) { //Puts a button on each cell
                Button button = new Button();
                button.setId(row + "," + col);
                button.setPrefSize(getCellSize(size), getCellSize(size));

                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);

                configureCellButton(button, row, col); //Configures each button
                
                gridPane.add(button, col, row);
            }
        }
    }
    
    protected int getCellSize(int size) { //Sets cell size for better visualization of the board
        switch (size) {
            case 12: return 51;
            case 16: return 41;
            case 20: return 31;
            default: return 51;
        }
    }
    
    protected void configureCellButton(Button button, int row, int col) { //Sets on each button a action by clicking on them
        button.setOnAction(event -> handleCellClick(event));
    }
    
    protected void centerBoard() { //Method for centering the board (sometimes it doesn't work properly)
        double paneWidth = rootPane.getWidth();
        double paneHeight = rootPane.getHeight();
        double gridWidth = gridPane.getBoundsInParent().getWidth();
        double gridHeight = gridPane.getBoundsInParent().getHeight();
        gridPane.setLayoutX((paneWidth - gridWidth) / 2);
        gridPane.setLayoutY((paneHeight - gridHeight) / 2);
    }
    
}
