/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.modelFx.SongsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditAlbumController {

    @FXML
    private Label albumTitleLabel;

    @FXML
    private TextField albumTitleTextField;

    @FXML
    private Label artistLabel;

    @FXML
    private ComboBox<ArtistsFx> artistsComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<SongsFx> songsTableView;

    @FXML
    private TableColumn<SongsFx, Object> trackNoTableColumn;
    @FXML
    private TableColumn<SongsFx, String> songsTableColumn;

    private AlbumsController albumsController;
    private AlbumFxModel albumFxModel;
    private Executor executor;
    private Stage stage;

    @FXML
    private void initialize(){
        setComponentsVisibility(false);
        this.songsTableColumn.setStyle("-fx-alignment: CENTER;");
        this.trackNoTableColumn.setStyle("-fx-alignment: CENTER;");

        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void setComponentsVisibility(boolean isVisible) {
        albumTitleLabel.setVisible(isVisible);
        albumTitleTextField.setVisible(isVisible);
        artistLabel.setVisible(isVisible);
        artistsComboBox.setVisible(isVisible);
        saveButton.setVisible(isVisible);
        cancelButton.setVisible(isVisible);
        songsTableView.setVisible(isVisible);
    }

    private void binding() {
        this.artistsComboBox.setItems(this.albumFxModel.getArtistsFxObservableList());
        this.albumTitleTextField.textProperty().bindBidirectional(this.albumFxModel.getAlbumsFxObjectProperty().nameProperty());
        this.artistsComboBox.valueProperty().bindBidirectional(this.albumFxModel.getAlbumsFxObjectProperty().artistFxProperty());
        this.saveButton.disableProperty().bind(this.albumTitleTextField.textProperty().isEmpty());
        this.songsTableView.setItems(this.albumFxModel.getSongsFxObservableList());
        this.trackNoTableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTrack()));
        this.songsTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    }

    @FXML
    private void artistsComboBoxOnAction() {
        this.albumFxModel.getAlbumsFxObjectProperty().setArtistFx(this.artistsComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void saveButtonOnAction() {
        albumsController.showWaitWindow();
        final AlbumFxModel taskAlbumFxModel = this.albumFxModel;
        Task<AlbumFxModel> updateAlbumTask = new Task<AlbumFxModel>() {

            @Override
            protected AlbumFxModel call() {
                try {
                    taskAlbumFxModel.updateAlbumInDataBase();
                    albumsController.reinitFxModels();
                } catch (ApplicationExceptions applicationExceptions) {
                    applicationExceptions.printStackTrace();
                }
                return taskAlbumFxModel;
            }
        };

        updateAlbumTask.setOnSucceeded(e -> stage.close());
        updateAlbumTask.setOnFailed(e -> stage.close());
        executor.execute(updateAlbumTask);
    }

    @FXML
    private void cancelButtonOnAction() {
        stage.close();
    }

    void setAlbumFxModel(AlbumFxModel albumFxModel) {
        this.albumFxModel = albumFxModel;
    }

    Stage getStage() {
        return stage;
    }
    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setAlbumsController(AlbumsController albumsController) {
        this.albumsController = albumsController;
    }

    void initSongsListAndBinding() {
        final AlbumFxModel taskAlbumFxModel = this.albumFxModel;
        Task<AlbumFxModel> initSongsListTask = new Task<AlbumFxModel>() {
            @Override
            protected AlbumFxModel call() {
                try {
                    taskAlbumFxModel.initSongsFxObservableList();
                } catch (ApplicationExceptions applicationExceptions) {
                    DialogsUtils.errorDialog(applicationExceptions.getMessage());
                }
                return taskAlbumFxModel;
            }
        };
        initSongsListTask.setOnSucceeded(e -> {
            this.albumFxModel = initSongsListTask.getValue();
            binding();
//            albumsController.getWaitStage().close();
//            albumsController.closeWaitWindow();
            setComponentsVisibility(true);
        });
        executor.execute(initSongsListTask);
    }
}
