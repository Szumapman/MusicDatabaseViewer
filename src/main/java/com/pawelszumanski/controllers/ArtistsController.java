/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class ArtistsController {

    @FXML
    private TreeView<String> artistsTreeView;

    @FXML
    private TextField artistTextField;

    @FXML
    private Button saveArtistButton;

    @FXML
    private ComboBox<ArtistsFx> artistsComboBox;

    @FXML
    private Button editArtistButton;

    @FXML
    private Button deleteArtistButton;


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

        this.artistsComboBox.setItems(this.artistFxModel.getArtistsList());

        bindings();

    }

    private void bindings() {
        this.saveArtistButton.disableProperty().bind(artistTextField.textProperty().isEmpty());
        this.editArtistButton.disableProperty().bind(this.artistFxModel.artistProperty().isNull());
        this.deleteArtistButton.disableProperty().bind(this.artistFxModel.artistProperty().isNull());
    }


    @FXML
    private void saveArtistOnAction() {
        try {
            artistFxModel.saveArtistInDataBase(artistTextField.getText());
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        artistTextField.clear();
    }

    @FXML
    private void artistsComboBoxOnAction() {
        this.artistFxModel.setArtist(this.artistsComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editArtistOnAction() {
        String newArtistName = DialogsUtils.editDialog(this.artistFxModel.getArtist().getName());
        if(newArtistName != null){
            this.artistFxModel.getArtist().setName(newArtistName);
            try {
                this.artistFxModel.updateArtistInDataBase();
            } catch (ApplicationExceptions applicationExceptions) {
                DialogsUtils.errorDialog(applicationExceptions.getMessage());
            }
        }
    }

    @FXML
    private void deleteArtistOnAction() {
        String artistToDelete = this.artistFxModel.getArtist().getName();
        boolean deleteArtist = DialogsUtils.deleteArtistConfirmationDialog(artistToDelete);
        if(deleteArtist){
            try {
                this.artistFxModel.deleteArtistById();
            } catch (ApplicationExceptions applicationExceptions) {
                DialogsUtils.errorDialog(applicationExceptions.getMessage());
            }
        }
    }
}
