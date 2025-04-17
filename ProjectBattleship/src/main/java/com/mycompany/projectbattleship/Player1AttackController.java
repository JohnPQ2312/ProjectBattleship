/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * FXML Controller class
 *
 * @author jp570
 */
public class Player1AttackController extends GameTableController {

    @FXML private Button btnChangePlayer, btnChangeScreen;
    @FXML private TextField playerNameField, difficultyField;
    @FXML private Label attackingLabel;

    private final int currentPlayer = 1;
    private final int enemyPlayer = 2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameState.setCurrentPlayer(currentPlayer);
        GameState.setCurrentPhase(GameState.Phase.ATTACK_P1);
        GameState.resetShotsForTurn(GameState.getDifficulty());
        
        playerNameField.setText(GameState.getPlayer1Name());
        playerNameField.setEditable(false);        
        difficultyField.setText(GameState.getDifficulty());
        difficultyField.setEditable(false);
        attackingLabel.setText("Atacando a: " + GameState.getPlayer2Name());
        
        btnChangePlayer.setDisable(GameState.getRemainingShots() > 0 || GameState.isPvC());
        
        
        createAttackBoard(boardSize);
        Platform.runLater(() -> {
            rootPane.widthProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            centerBoard();
        });
    }

    private void createAttackBoard(int size) {
        
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < size; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / size);
            col.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / size);
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button cell = new Button();
                cell.setId(row + "," + col);

                cell.setPrefSize(getCellSize(size), getCellSize(size));   

                int value = GameState.getBoardCell(enemyPlayer, row, col);
                switch (value) {
                    case -2:
                        cell.setStyle("-fx-background-color: red;");
                        cell.setDisable(true);
                        break;
                    case -1: 
                        cell.setStyle("-fx-background-color: lightblue;");
                        cell.setDisable(true);
                        break;
                    default:
                        cell.setStyle("-fx-background-color: #cccccc;");
                        int finalRow = row;
                        int finalCol = col;
                        cell.setOnAction(e -> handleAttackClick(cell, finalRow, finalCol));
                        break;
                }

                GridPane.setHalignment(cell, HPos.CENTER);
                GridPane.setValignment(cell, VPos.CENTER);
                gridPane.add(cell, col, row);
            }
        }
    }
    
    private void handleAttackClick(Button btn, int row, int col) {
        if (GameState.getRemainingShots() <= 0) return;

        int result = GameState.attack(currentPlayer, row, col);

        switch (result) {
            case -2: btn.setStyle("-fx-background-color: red;"); break;
            case -1: btn.setStyle("-fx-background-color: lightblue;"); break;
            case 0: return;
        }

        btn.setDisable(true);
        GameState.useShot();

        if (GameState.getRemainingShots() == 0) {
            if (GameState.isPvC()) {
                runCpuTurn();
            } else {
                btnChangePlayer.setDisable(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Turno finalizado");
                alert.setHeaderText(null);
                alert.setContentText("Has usado todos tus disparos. Cambia de jugador.");
                alert.showAndWait();
            }
        }
    }
    
    private void runCpuTurn() {
        CPUPlayer cpu = GameState.getCpuPlayer();
        int shots = GameState.getRemainingShots();

        for (int i = 0; i < shots; i++) {
            int[] move = cpu.makeMove();
            GameState.attack(2, move[0], move[1]);
        }

        GameState.setCurrentPlayer(1);
        GameState.setCurrentPhase(GameState.Phase.ATTACK_P1);
        GameState.resetShotFlag();
        GameState.resetShotsForTurn(GameState.getDifficulty());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Turno del CPU");
        alert.setHeaderText(null);
        alert.setContentText("El CPU ha realizado sus disparos. Tu turno.");
        alert.showAndWait();

        try {
            App.setRoot("player1Attack");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void handleChangePlayer() throws IOException {
        GameState.setCurrentPlayer(enemyPlayer);
        GameState.setCurrentPhase(GameState.Phase.ATTACK_P2);
        GameState.resetShotFlag();
        GameState.resetShotsForTurn(GameState.getDifficulty());
        App.setRoot("player2Attack");
    }

    @FXML
    private void handleChangeScreen() throws IOException {
        App.setRoot("player1Board");
    }    
    
    @Override
    protected void handleCellClick(ActionEvent event) {
        
    }
}
