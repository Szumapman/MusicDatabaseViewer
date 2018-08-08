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
import com.pawelszumanski.utils.tasks.AlbumFxModelTask;
import com.pawelszumanski.utils.tasks.ArtistFxModelTask;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.pawelszumanski.utils.PathUtils.EDIT_ALBUM_FXML;

public class AlbumsController extends SuperWaitWindow{

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



    private Executor executor;
    private AlbumFxModel albumFxModel;
    private ArtistFxModel artistFxModel;

    @FXML
    public void initialize() {
        this.albumFxModel = new AlbumFxModel();
        this.artistFxModel = new ArtistFxModel();
        try {
            albumFxModel.init();
            artistFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        bindings();

        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

    }

    private void bindings() {
        this.albumsTreeView.setRoot(this.albumFxModel.getRoot());
        this.albumComboBox.setItems(this.albumFxModel.getAlbumsList());
        this.artistComboBox.setItems(this.albumFxModel.getArtistsFxObservableList());
        this.saveAlbumButton.disableProperty().bind(this.albumsTextField.textProperty().isEmpty().or(this.artistFxModel.artistsFxObjectPropertyProperty().isNull()));
        this.editAlbumButton.disableProperty().bind(this.albumFxModel.albumsFxProperty().isNull());
        this.deleteAlbumButton.disableProperty().bind(this.albumFxModel.albumsFxProperty().isNull());
    }


    @FXML
    private void artistComboBoxOnAction() {
        this.artistFxModel.setArtistsFxObjectProperty(this.artistComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void saveAlbumOnAction() {
        showWaitWindow();
        final AlbumFxModel taskAlbumFxModel = this.albumFxModel;
        final ArtistFxModel taskArtistFxModel = this.artistFxModel;
        Task<Void> saveAlbumTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    taskAlbumFxModel.saveAlbumInDataBase(albumsTextField.getText(), taskArtistFxModel.getArtistsFxObjectProperty().getId());
                } catch (ApplicationExceptions applicationExceptions) {
                    DialogsUtils.errorDialog(applicationExceptions.getMessage());
                }
                return null;
            }
        };
        saveAlbumTask.setOnSucceeded(e -> reinitFxModels());
        executor.execute(saveAlbumTask);
    }

    @FXML
    private void albumComboBoxOnAction() {
        this.albumFxModel.setAlbumsFx(this.albumComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editAlbumOnAction() {

        FXMLLoader loader = FxmlUtils.getLoader(EDIT_ALBUM_FXML);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        Stage stage = new Stage();
        stage.setIconified(false);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        EditAlbumController editAlbumController = loader.getController();
        editAlbumController.setStage(stage);
        editAlbumController.getStage().show();
        editAlbumController.setAlbumsController(this);
        editAlbumController.setAlbumFxModel(this.albumFxModel);
        editAlbumController.initSongsListAndBinding();
        editAlbumController.getStage().showAndWait();
    }

    @FXML
    private void deleteAlbumOnAction() {
        String albumToDelete = this.albumFxModel.getAlbumsFx().getName();
        boolean deleteAlbum = DialogsUtils.deleteConfirmationDialog(albumToDelete);
        if (deleteAlbum) {
            showWaitWindow();
            final AlbumFxModel taskAlbumFxModel = this.albumFxModel;
            Task<AlbumFxModel> deleteAlbumTask  = new Task<AlbumFxModel>() {
                @Override
                protected AlbumFxModel call() {
                    try {
                        taskAlbumFxModel.deleteAlbumById();
                    } catch (ApplicationExceptions applicationExceptions) {
                        DialogsUtils.errorDialog(applicationExceptions.getMessage());
                    }
                    return taskAlbumFxModel;
                }
            };
            deleteAlbumTask.setOnSucceeded(e -> reinitFxModels());
            executor.execute(deleteAlbumTask);
        }
    }

    @FXML
    private void unknownArtistOnAction(ActionEvent actionEvent) {
        if (((CheckBox) actionEvent.getSource()).isSelected()) {
            ArtistsFx artistsFx = null;
            for (ArtistsFx artistFXFromList : this.albumFxModel.getArtistsFxObservableList()) {
                if (artistFXFromList != null && artistFXFromList.getName().equals(resourceBundle.getString("unknown.artists"))) {
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

    void reinitFxModels() {
        Task<AlbumFxModel> createAlbumFxmodelTask = new AlbumFxModelTask();
        createAlbumFxmodelTask.setOnSucceeded(e -> {
            this.albumFxModel = createAlbumFxmodelTask.getValue();
            Task<ArtistFxModel> createArtistFxModelTask = new ArtistFxModelTask();
            createArtistFxModelTask.setOnSucceeded(e1 -> {
                this.artistFxModel = createArtistFxModelTask.getValue();
                clearFields();
                bindings();
                closeWaitWindow();
            });
            executor.execute(createArtistFxModelTask);
        });
        executor.execute(createAlbumFxmodelTask);
    }

    private void clearFields() {
        this.artistComboBox.getSelectionModel().clearSelection();
        this.albumComboBox.getSelectionModel().clearSelection();
        this.albumsTextField.clear();
        this.unKnownArtistCheckBox.setSelected(false);
    }
}
