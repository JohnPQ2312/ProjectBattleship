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
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author jp570
 */
public class MenuScreenController implements Initializable { //Menu Screen controller
    
    @FXML
    private Button btnExit, btnPlay, btnAbout;

    @FXML
    private void exit() {
        Platform.exit();
    }
    
    @FXML
    private void play() throws IOException{
        App.setRoot("difficultyScreen");
    }
    
    @FXML
    private void about() throws IOException{
        App.setRoot("aboutScreen");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
