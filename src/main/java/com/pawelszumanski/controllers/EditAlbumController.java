/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditAlbumController {
    @FXML
    private TextField albumTitleTextField;

    @FXML
    private ComboBox<ArtistsFx> artistsComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private AlbumFxModel albumFxModel;
    private ArtistFxModel artistFxModel;



    @FXML
    public void initialize(){
        this.albumFxModel = new AlbumFxModel();
        this.artistFxModel = new ArtistFxModel();
        try {
            albumFxModel.init();
            artistFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }


    }

    protected void binding() {
        this.albumTitleTextField.textProperty().bindBidirectional(this.albumFxModel.getAlbumsFxObjectProperty().nameProperty());
        this.artistsComboBox.setItems(this.artistFxModel.getArtistsList());
        System.out.println();
        this.artistsComboBox.valueProperty().bindBidirectional(this.artistFxModel.artistsFxObjectPropertyProperty());
        this.saveButton.disableProperty().bind(this.albumTitleTextField.textProperty().isEmpty());
    }

    @FXML
    private void artistsComboBoxOnAction(ActionEvent event) {

    }

    @FXML
    private void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    private void saveButtonOnAction(ActionEvent event) {

    }

    public AlbumFxModel getAlbumFxModel() {
        return albumFxModel;
    }

    public ArtistFxModel getArtistFxModel() {
        return artistFxModel;
    }
}
