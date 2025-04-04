/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author jp570
 */
public class DifficultyScreenController implements Initializable {
    
    @FXML
    private RadioButton rbEasy, rbMedium, rbHard;
    
    @FXML
    private TextField enterName;
    String name;
    String difficult;
    
    boolean rbConfirmed, nameConfirmed;
    
    @FXML
    private Button confirmButton;
    
    private ToggleGroup rbGroup; 
    
    @FXML
    private Button backDifficulty, saveName;
    
    @FXML
    private void backToMain() throws IOException{
        App.setRoot("menuScreen");
    }
    
    @FXML
    private void confirmSelection() throws IOException {
        name = enterName.getText();
        
        if (rbEasy.isSelected()) {
            difficult = "FACIL";
        } else if (rbMedium.isSelected()) {
            difficult = "MEDIO";
        } else if (rbHard.isSelected()) {
            difficult = "DIFICIL";
        }

        GameState.setPlayerName(name);
        GameState.setDifficulty(difficult);
        
        int boardSize = getBoardSize(difficult);
        GameTableController.setBoardSize(boardSize);
        App.setRoot("gameTable");
    }

    private int getBoardSize(String difficulty) {
        switch (difficulty) {
            case "FACIL":
                return 12;
            case "MEDIO":
                return 16;
            case "DIFICIL":
                return 20;
            default:
                return 12;
        }
    }
    
    public void updateButtonState(String field, ToggleGroup rbGroup, Button confirmButton) {
        Runnable updateState = () -> {
            boolean nameConfirmed = !field.isEmpty();
            boolean rbConfirmed = rbGroup.getSelectedToggle() != null;

            confirmButton.setDisable(!(nameConfirmed && rbConfirmed)); 
        };
        updateState.run();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbGroup = new ToggleGroup();
        rbEasy.setToggleGroup(rbGroup);
        rbMedium.setToggleGroup(rbGroup);
        rbHard.setToggleGroup(rbGroup);
        
        confirmButton.setDisable(true);
        
        ChangeListener<Object> updateListener = (observable, oldValue, newValue) -> {
            updateButtonState(enterName.getText(), rbGroup, confirmButton);
        };

        enterName.textProperty().addListener(updateListener);
        rbGroup.selectedToggleProperty().addListener(updateListener);

    }

}
