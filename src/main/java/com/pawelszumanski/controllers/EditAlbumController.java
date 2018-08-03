/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.modelFx.SongsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditAlbumController {
    @FXML
    private TextField albumTitleTextField;

    @FXML
    private ComboBox<ArtistsFx> artistsComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<SongsFx> songsTableView;

    @FXML
    private TableColumn<SongsFx, String> songsTableColumn;

    private AlbumFxModel albumFxModel;
    private AlbumsController albumsController;

    @FXML
    public void initialize(){
        this.albumFxModel = new AlbumFxModel();
        try {
            albumFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }

//        binding();


    }

    protected void binding() {
        this.artistsComboBox.setItems(this.albumFxModel.getArtistsFxObservableList());
        this.albumTitleTextField.textProperty().bindBidirectional(this.albumFxModel.getAlbumsFxObjectProperty().nameProperty());
        this.artistsComboBox.valueProperty().bindBidirectional(this.albumFxModel.getAlbumsFxObjectProperty().artistFxProperty());
        this.saveButton.disableProperty().bind(this.albumTitleTextField.textProperty().isEmpty());
    }

    @FXML
    private void artistsComboBoxOnAction() {
//        this.albumFxModel.getAlbumsFxObjectProperty().setArtistFx(this.artistsComboBox.getSelectionModel().getSelectedItem());
    }



    protected AlbumFxModel getAlbumFxModel() {
        return albumFxModel;
    }

    protected Button getSaveButton() {
        return saveButton;
    }

    protected Button getCancelButton() {
        return cancelButton;
    }



}
