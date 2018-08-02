/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.AlbumsFx;
import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.pawelszumanski.utils.PathUtils.EDIT_ALBUM_FXML;

public class AlbumsController {

    @FXML
    private TreeView<String> albumsTreeView;

    @FXML
    private TextField albumsTextField;

    @FXML
    private ComboBox<ArtistsFx> artistComboBox;

    @FXML
    private CheckBox unKnownArtistCheckBox;

    @FXML
    private Button saveAlbumButton;

    @FXML
    private ComboBox<AlbumsFx> albumComboBox;

    @FXML
    private Button editAlbumButton;

    @FXML
    private Button deleteAlbumButton;

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
        this.albumsTreeView.setRoot(this.albumFxModel.getRoot());
        this.albumComboBox.setItems(this.albumFxModel.getAlbumsList());
        this.artistComboBox.setItems(this.artistFxModel.getArtistsList());

        this.saveAlbumButton.disableProperty().bind(this.artistFxModel.artistsFxObjectPropertyProperty().isNull()
                .or(this.albumsTextField.textProperty().isEmpty()));
    }

    @FXML
    private void artistComboBoxOnAction(ActionEvent actionEvent) {
        this.artistFxModel.setArtistsFxObjectProperty(this.artistComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void saveAlbumOnAction(ActionEvent actionEvent) {
        try {
            this.albumFxModel.saveAlbumInDataBase(albumsTextField.getText(), this.artistFxModel.getArtistsFxObjectProperty().getId());
            artistFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        albumsTextField.clear();

    }

    @FXML
    private void albumComboBoxOnAction(ActionEvent actionEvent) {
        this.albumFxModel.setAlbumsFxObjectProperty(this.albumComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editAlbumOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = FxmlUtils.getLoader(EDIT_ALBUM_FXML);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
            e.printStackTrace();
        }
        EditAlbumController editAlbumController = loader.getController();
        editAlbumController.getAlbumFxModel().setAlbumsFxObjectProperty(this.albumFxModel.getAlbumsFxObjectProperty());
        editAlbumController.getArtistFxModel().setArtistsFxObjectProperty(this.artistFxModel.getArtistsFxObjectProperty());
        System.out.println(editAlbumController.getArtistFxModel().artistsFxObjectPropertyProperty());
        editAlbumController.binding();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void deleteAlbumOnAction(ActionEvent actionEvent) {
        String albumToDelete = this.albumFxModel.getAlbumsFxObjectProperty().getName();
        boolean deleteAlbum = DialogsUtils.deleteConfirmationDialog(albumToDelete);
        if(deleteAlbum){
            try {
                this.albumFxModel.deleteAlbumById();
            } catch (ApplicationExceptions applicationExceptions) {
                DialogsUtils.errorDialog(applicationExceptions.getMessage());
            }
        }
    }

    @FXML
    private void unknownArtistOnAction(ActionEvent actionEvent) {
        if(((CheckBox)actionEvent.getSource()).isSelected()){
            this.artistComboBox.setDisable(true);
            try {
                this.artistFxModel.setUnknownArtist();
            } catch (ApplicationExceptions applicationExceptions) {
                DialogsUtils.errorDialog(applicationExceptions.getMessage());
            }
        } else {
            this.artistComboBox.setDisable(false);

        }
    }
}
