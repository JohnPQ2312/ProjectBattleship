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
public class Player2AttackController extends GameTableController { //P2 attack board
    @FXML private Button btnChangePlayer, btnChangeScreen;
    @FXML private TextField playerNameField, difficultyField;
    @FXML private Label attackingLabel;
    
    private final int currentPlayer = 2;
    private final int enemyPlayer = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameState.setCurrentPlayer(currentPlayer);
        GameState.resetShotsForTurn(GameState.getDifficulty());

        playerNameField.setText(GameState.getPlayer2Name());
        playerNameField.setEditable(false);
        difficultyField.setText(GameState.getDifficulty());
        difficultyField.setEditable(false);
        attackingLabel.setText("Atacando a: " + GameState.getPlayer1Name());
        
        if (GameState.getCurrentPhase() == GameState.Phase.EXTRA_TURN){
                difficultyField.setText("Turno Extra");
        } else{
            GameState.setCurrentPhase(GameState.Phase.ATTACK_P2);
        }

        btnChangePlayer.setDisable(GameState.getRemainingShots() > 0);

        createAttackBoard(boardSize);
        Platform.runLater(() -> {
            rootPane.widthProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> centerBoard());
            centerBoard();
        });
    }

    private void createAttackBoard(int size) { //Creates an attack board, based on createBoard method
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
                cell.setId(row + "," + col); //ID coordinate
                cell.setPrefSize(getCellSize(size), getCellSize(size));

                int value = GameState.getBoardCell(enemyPlayer, row, col); //It gets all the enemy ship positions
                switch (value) { //All cells are the same color, it changes if the shot hit or failed
                    case -2:
                        cell.setStyle("-fx-background-color: red;");
                        cell.setDisable(true);
                        break;
                    case -1:
                        cell.setStyle("-fx-background-color: #008cff;");
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

    private void handleAttackClick(Button btn, int row, int col) { //Executes the attack per each cell clicked
        if (GameState.getRemainingShots() <= 0) return;

        int result = GameState.attack(currentPlayer, row, col); //Shows the result of the shot
        switch (result) {
            case -2: btn.setStyle("-fx-background-color: red;"); break;
            case -1: btn.setStyle("-fx-background-color: #008cff;"); break;
            case 0: return;
        }

        if (GameState.getCurrentPhase() == GameState.Phase.EXTRA_TURN){ //Evaluates if the game is on P2's extra turn
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Turno finalizado");
            alert.setHeaderText(null);
            alert.setContentText(GameState.getPlayer2Name() + ", has usado tu turno adicional, ahora se presentaran los resultados de la partida.");
            alert.showAndWait();
            GameState.setCurrentPhase(GameState.Phase.GAME_OVER);
        }        
        
        if (GameState.getCurrentPhase() == GameState.Phase.GAME_OVER) { //Evaluates if the game ended
            try {
                handleGameOverScreen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        
        btn.setDisable(true);
        GameState.useShot();

        if (GameState.getRemainingShots() == 0) { //If the attacker uses all it's shots, it continues to the next turn
            btnChangePlayer.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Turno finalizado");
            alert.setHeaderText(null);
            alert.setContentText("Has usado todos tus disparos. Cambia de jugador.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleChangePlayer() throws IOException { //Change screen and turn to P1
        GameState.setCurrentPlayer(enemyPlayer);
        GameState.setCurrentPhase(GameState.Phase.ATTACK_P1);
        GameState.resetShotFlag();
        GameState.resetShotsForTurn(GameState.getDifficulty());
        App.setRoot("player1Attack");
    }

    @FXML
    private void handleChangeScreen() throws IOException { //Observate board
        App.setRoot("player2Board");
    }
    
    @FXML
    private void handleGameOverScreen() throws IOException { //Change to game over
        App.setRoot("gameOverScreen");
    }

    @Override
    protected void handleCellClick(ActionEvent event) { //Not used
        
    }
}
