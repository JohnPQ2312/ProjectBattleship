/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author jp570
 */

public class GameOverScreenController {
    @FXML private Label resultLabel;
    @FXML private Label statsLabel;

    @FXML
    public void initialize() {
        int winner = GameState.getWinner();
        boolean isPvC = GameState.isPvC();

        String player2Label = isPvC ? "CPU" : GameState.getPlayer2Name();
        String winnerName = (winner == 0) ? "¡Empate!" : "¡Ganador: " +
                (winner == 1 ? GameState.getPlayer1Name() : player2Label) + "!";

        resultLabel.setText(winnerName);

        statsLabel.setText(
            "Disparos " + GameState.getPlayer1Name() + ": " + GameState.getShotsTaken(1) +
            " | Aciertos: " + GameState.getHits(1) +
            " | Precisión: " + GameState.getAccuracy(1) + "\n" +

            "Disparos " + player2Label + ": " + GameState.getShotsTaken(2) +
            " | Aciertos: " + GameState.getHits(2) +
            " | Precisión: " + GameState.getAccuracy(2)
        );
    }

    @FXML
    private void handleNewGame() throws IOException {
        GameState.resetAll();
        App.setRoot("difficultyScreen");
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}