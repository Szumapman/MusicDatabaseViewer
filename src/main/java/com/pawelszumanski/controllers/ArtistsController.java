/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class ArtistsController {

    @FXML
    private TreeView<String> artistsTreeView;

    @FXML
    private TextField artistTextField;

    @FXML
    private Button saveArtistButton;

    private ArtistFxModel artistFxModel;


    @FXML
    public void initialize(){
        this.artistFxModel = new ArtistFxModel();
        try {
            artistFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        this.artistsTreeView.setRoot(this.artistFxModel.getRoot());
    }

    @FXML
    private void saveArtistOnAction(ActionEvent actionEvent) {
        try {
            artistFxModel.saveArtistInDataBase(artistTextField.getText());
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        artistTextField.clear();
    }
}
