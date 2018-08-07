/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class WaitWindowController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize(){
        progressBar.setVisible(true);
    }
}
