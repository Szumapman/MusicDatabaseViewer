/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumsFx;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.modelFx.SongFxModel;
import com.pawelszumanski.modelFx.SongsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SongsController extends SuperWaitWindow{
    @FXML
    private TableView<SongsFx> songsTableView;

    @FXML
    private TableColumn<SongsFx, String> songColunm;

    @FXML
    private TableColumn<SongsFx, AlbumsFx> albumColumn;

    @FXML
    private TableColumn<SongsFx, ArtistsFx> artistColumn;

    @FXML
    private Button addSongButton;

    private SongFxModel songFxModel;

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
    }

    private void bindings() {
        this.songsTableView.setItems(this.songFxModel.getSongsFxObservableList());
        this.songColunm.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        this.albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumsFxProperty());
        this.artistColumn.setCellValueFactory(cellData -> cellData.getValue().getAlbumsFx().artistFxProperty());
    }

    @FXML
    void addSongButtonOnAction() {

    }
}
