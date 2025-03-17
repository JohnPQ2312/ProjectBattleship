/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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
    private Button confirmButton;
    
    private ToggleGroup rbGroup; 
    
    @FXML
    private Button backDifficulty;
    
    @FXML
    private void backToMain() throws IOException{
        App.setRoot("menuScreen");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbGroup = new ToggleGroup();
        rbEasy.setToggleGroup(rbGroup);
        rbMedium.setToggleGroup(rbGroup);
        rbHard.setToggleGroup(rbGroup);
        
        rbGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                confirmButton.setDisable(false);
            }
        });
    }    
    
}
