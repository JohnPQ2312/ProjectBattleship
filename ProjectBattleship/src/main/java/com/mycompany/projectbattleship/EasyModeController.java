/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectbattleship;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class EasyModeController implements Initializable {


    @FXML
    private GridPane boardGrid; 
    
    @FXML
    private Button button;
    
    @FXML
    private void highlightCell(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #87CEFA;");
    }

    @FXML
    private void resetCell(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #cccccc;");
    }
    
    @FXML
    private void handleButtonClick() {
        button.setStyle("water");
        button.setOpacity(1.0);
        button.setDisable(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof Button) {
                Button cell = (Button) node;
                cell.setOnMouseEntered(this::highlightCell);
                cell.setOnMouseExited(this::resetCell);
            }
        }
        
    }
}
