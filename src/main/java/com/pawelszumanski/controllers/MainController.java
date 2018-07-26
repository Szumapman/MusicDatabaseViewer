package com.pawelszumanski.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainWindow;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;


    @FXML
    private void initialize(){
        topMenuButtonsController.setMainController(this);
    }
}
