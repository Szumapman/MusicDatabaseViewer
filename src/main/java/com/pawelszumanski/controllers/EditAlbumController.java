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

public class EditAlbumController extends SuperWaitWindow {

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
    public void initialize(){
        showWaitWindow();
        albumTitleLabel.setVisible(false);
        albumTitleTextField.setVisible(false);
        artistLabel.setVisible(false);
        artistsComboBox.setVisible(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        songsTableView.setVisible(false);
        this.songsTableColumn.setStyle("-fx-alignment: CENTER;");
        this.trackNoTableColumn.setStyle("-fx-alignment: CENTER;");

        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void binding() {
        this.artistsComboBox.setItems(this.albumFxModel.getArtistsFxObservableList());
        this.albumTitleTextField.textProperty().bindBidirectional(this.albumFxModel.getAlbumsFx().nameProperty());
        this.artistsComboBox.valueProperty().bindBidirectional(this.albumFxModel.getAlbumsFx().artistFxProperty());
        this.saveButton.disableProperty().bind(this.albumTitleTextField.textProperty().isEmpty());
        this.songsTableView.setItems(this.albumFxModel.getSongsFxObservableList());
        this.trackNoTableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTrack()));
        this.songsTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    }

    @FXML
    private void artistsComboBoxOnAction() {
        this.albumFxModel.getAlbumsFx().setArtistFx(this.artistsComboBox.getSelectionModel().getSelectedItem());
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

        updateAlbumTask.setOnSucceeded(e -> {
            stage.close();
        });
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
            closeWaitWindow();
            albumTitleLabel.setVisible(true);
            albumTitleTextField.setVisible(true);
            artistLabel.setVisible(true);
            artistsComboBox.setVisible(true);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            songsTableView.setVisible(true);
        });
        executor.execute(initSongsListTask);
    }
}
