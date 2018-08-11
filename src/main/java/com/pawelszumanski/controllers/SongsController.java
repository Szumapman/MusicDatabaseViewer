/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.*;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import com.pawelszumanski.utils.tasks.SongFxModelTask;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.pawelszumanski.utils.PathUtils.EDIT_SONG_FXML;

public class SongsController {
    @FXML
    private TableView<SongsFx> songsTableView;

    @FXML
    private TableColumn<SongsFx, String> songColunm;

    @FXML
    private TableColumn<SongsFx, AlbumsFx> albumColumn;

    @FXML
    private TableColumn<SongsFx, ArtistsFx> artistColumn;

    @FXML
    private MenuItem editMenuItem;

    private SongFxModel songFxModel;
    private Executor executor;
    private EditSongController editSongController;

    @FXML
    void initialize(){
        songFxModel = new SongFxModel();
        try {
            songFxModel.init();
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
        this.songColunm.setStyle("-fx-alignment: CENTER;");
        this.albumColumn.setStyle("-fx-alignment: CENTER;");
        this.artistColumn.setStyle("-fx-alignment: CENTER;");
        bindings();

        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void bindings() {
        this.songsTableView.setItems(this.songFxModel.getSongsFxObservableList());
        this.songColunm.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        this.albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumsFxProperty());
        this.artistColumn.setCellValueFactory(cellData -> cellData.getValue().getAlbumsFx().artistFxProperty());
        this.editMenuItem.disableProperty().bind(this.songsTableView.getSelectionModel().selectedItemProperty().isNull());
        this.songsTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> this.songFxModel.setSongsFxObjectProperty(newValue)));
    }

    @FXML
    private void addSongButtonOnAction() {
        AlbumFxModel albumFxModel = new AlbumFxModel();
        albumFxModel.setAlbumsFxObjectProperty(setAlbumAndArtistAsUnknown());
        this.songFxModel.getSongsFxObjectProperty().setAlbumsFx(albumFxModel.getAlbumsFxObjectProperty());
        EditSongController editSongController = getEditSongController();
        editSongController.getStage().showAndWait();
    }

    private AlbumsFx setAlbumAndArtistAsUnknown(){
        AlbumsFx albumsFx = new AlbumsFx();
        albumsFx.setId(1);
        albumsFx.setName("Album nieznany");
        ArtistsFx artistsFx = new ArtistsFx();
        artistsFx.setId(1);
        artistsFx.setName("Artysta nieznany");
        albumsFx.setArtistFx(artistsFx);
        return albumsFx;
    }

    private EditSongController getEditSongController() {
        FXMLLoader loader = FxmlUtils.getLoader(EDIT_SONG_FXML);
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
        editSongController = loader.getController();
        editSongController.setStage(stage);
        editSongController.setSongsController(this);
        editSongController.setSongFxModel(this.songFxModel);
        editSongController.binding();
        return editSongController;
    }

    @FXML
    private void editMenuItemOnAction() {
        EditSongController editSongController = getEditSongController();
        editSongController.getStage().showAndWait();
    }


    void reinitFxModel() {
        Task<SongFxModel> createSongFxModel = new SongFxModelTask();
        createSongFxModel.setOnSucceeded(e -> {
            this.songFxModel = createSongFxModel.getValue();
            bindings();
            editSongController.getStage().close();
        });
        executor.execute(createSongFxModel);
    }
}
