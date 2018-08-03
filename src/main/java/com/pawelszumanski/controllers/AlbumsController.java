/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.AlbumsFx;
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
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.EDIT_ALBUM_FXML;

public class AlbumsController {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

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

    @FXML
    public void initialize(){
        this.albumFxModel = new AlbumFxModel();
        try {
            albumFxModel.init();

        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        bindings();
        this.albumComboBoxOnAction();
    }

    private void bindings() {
        this.albumsTreeView.setRoot(this.albumFxModel.getRoot());
        this.albumComboBox.setItems(this.albumFxModel.getAlbumsList());
        this.artistComboBox.setItems(this.albumFxModel.getArtistsFxObservableList());
        this.saveAlbumButton.disableProperty().bind(this.albumFxModel.getAlbumsFxObjectProperty().artistFxProperty().isNull().or(this.albumsTextField.textProperty().isEmpty()));
        this.editAlbumButton.disableProperty().bind(this.albumFxModel.albumsFxObjectPropertyProperty().isNull());
        this.deleteAlbumButton.disableProperty().bind(this.albumFxModel.albumsFxObjectPropertyProperty().isNull());
    }

    @FXML
    private void artistComboBoxOnAction() {
        this.albumFxModel.getAlbumsFxObjectProperty().setArtistFx(this.artistComboBox.getSelectionModel().getSelectedItem());

    }

    @FXML
    private void saveAlbumOnAction() {
        try {
            this.albumFxModel.saveAlbumInDataBase(albumsTextField.getText());
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        albumsTextField.clear();

    }

    @FXML
    private void albumComboBoxOnAction() {
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
        try {
            editAlbumController.getAlbumFxModel().initSongsFxObservableList();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        editAlbumController.binding();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setIconified(false);
        stage.setResizable(false);
        Button saveButtonEditAlbumController = editAlbumController.getSaveButton();
        saveButtonEditAlbumController.setOnAction(e -> {
            try {
                editAlbumController.getAlbumFxModel().updateAlbumInDataBase();
            } catch (ApplicationExceptions applicationExceptions) {
                DialogsUtils.errorDialog(applicationExceptions.getMessage());
            }
            try {
                albumFxModel.init();
            } catch (ApplicationExceptions applicationExceptions) {
                applicationExceptions.printStackTrace();
            }
            stage.close();
        });
        Button cancelButtonEditAlbumController = editAlbumController.getCancelButton();
        cancelButtonEditAlbumController.setOnAction(e -> {
            stage.close();
        });
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
            ArtistsFx artistsFx = null;
            for(ArtistsFx artistFXFromList : this.albumFxModel.getArtistsFxObservableList()){
                if(artistFXFromList != null && artistFXFromList.getName().equals(resourceBundle.getString("unknown.artists"))){
                    artistsFx = artistFXFromList;
                    break;
                }
            }
            this.artistComboBox.setValue(artistsFx);
            this.artistComboBox.setDisable(true);

        } else {
            this.artistComboBox.setDisable(false);

        }
    }

    protected AlbumFxModel getAlbumFxModel() {
        return albumFxModel;
    }


}
