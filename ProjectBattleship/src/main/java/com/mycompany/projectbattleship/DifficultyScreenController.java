/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    private void confirmSelection() throws IOException{
        if (rbEasy.isSelected()){
            App.setRoot("easyMode");
        }
    }
    
    @FXML
    private void saveName(){
        name = enterName.getText();
        
        if (!name.isEmpty()) {
            saveName.setDisable(true);
            enterName.setDisable(true);
            rbEasy.setDisable(false);
            rbMedium.setDisable(true); //AUN NO USAR
            rbHard.setDisable(true); // AUN NO USAR
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbGroup = new ToggleGroup();
        rbEasy.setToggleGroup(rbGroup);
        rbMedium.setToggleGroup(rbGroup);
        rbHard.setToggleGroup(rbGroup);

        rbEasy.setDisable(true);
        rbMedium.setDisable(true);
        rbHard.setDisable(true);
        confirmButton.setDisable(true);
        saveName.setDisable(true);
        
        enterName.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isNotEmpty = !newValue.trim().isEmpty();
            saveName.setDisable(!isNotEmpty);
        });


        rbGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue == null);
        });
    }    
}
