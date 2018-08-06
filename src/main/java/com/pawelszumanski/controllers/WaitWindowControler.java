/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class WaitWindowControler {

    @FXML
    private ProgressBar progressBar;

    public void initialize(){
        progressBar = new ProgressBar();
    }

}
