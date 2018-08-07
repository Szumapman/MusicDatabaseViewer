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
import javafx.event.ActionEvent;
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

    @FXML
    private ProgressBar progressBar;

    private AlbumsController albumsController;


    private AlbumFxModel albumFxModel;
    private Executor executor;
    private Stage stage;

    @FXML
    public void initialize(){
        progressBar.setVisible(true);
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

    public void binding() {
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
    private void saveButtonOnAction(ActionEvent actionEvent) {
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

    protected AlbumFxModel getAlbumFxModel() {
        return albumFxModel;
    }

    protected void setAlbumFxModel(AlbumFxModel albumFxModel) {
        this.albumFxModel = albumFxModel;
    }

    protected Stage getStage() {
        return stage;
    }
    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void setAlbumsController(AlbumsController albumsController) {
        this.albumsController = albumsController;
    }

    protected void initSongsListAndBinding() {
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
            albumTitleLabel.setVisible(true);
            albumTitleTextField.setVisible(true);
            artistLabel.setVisible(true);
            artistsComboBox.setVisible(true);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            songsTableView.setVisible(true);
            progressBar.setVisible(false);
        });
        executor.execute(initSongsListTask);
    }
}
