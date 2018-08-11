/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.AlbumsFx;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.modelFx.SongFxModel;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditSongController {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    @FXML
    private TextField songTitleTextField;

    @FXML
    private ComboBox<AlbumsFx> albumComboBox;

    @FXML
    private Spinner<Integer> trackNoSpinner;

    @FXML
    private ComboBox<ArtistsFx> artistComboBox;


    private SongsController songsController;
    private SongFxModel songFxModel;
    private Executor executor;
    private Stage stage;


    @FXML
    private void initialize(){
        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

   }

    void binding() {
        Integer spinnerInitialValue = this.songFxModel.getSongsFxObjectProperty().trackProperty().getValue();
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, spinnerInitialValue);
        this.trackNoSpinner.setValueFactory(spinnerValueFactory);
        this.albumComboBox.setItems(this.songFxModel.getAlbumsFxObservableList());
        this.artistComboBox.setItems(this.songFxModel.getArtistsFxObservableList());

        this.albumComboBox.valueProperty().bindBidirectional(this.songFxModel.getSongsFxObjectProperty().albumsFxProperty());
        this.artistComboBox.valueProperty().bindBidirectional(this.songFxModel.getSongsFxObjectProperty().getAlbumsFx().artistFxProperty());

        this.songTitleTextField.textProperty().bindBidirectional(this.songFxModel.getSongsFxObjectProperty().titleProperty());

    }

    @FXML
    private void albumUnknownCheckBoxOnAction(ActionEvent event) {
        if(((CheckBox) event.getSource()).isSelected()){
            setUnknownAlbumOnComboBox();
            this.albumComboBox.setDisable(true);
        } else {
            this.albumComboBox.setDisable(false);
        }
    }

    private void setUnknownAlbumOnComboBox() {
        AlbumsFx albumsFx = null;
        for(AlbumsFx albumsFxFromList : this.songFxModel.getAlbumsFxObservableList()){
            if(albumsFxFromList != null && albumsFxFromList.getName().equals(resourceBundle.getString("unknown.album"))){
                albumsFx = albumsFxFromList;
                break;
            }
        }
        this.albumComboBox.setValue(albumsFx);
    }

    @FXML
    private void artistUnknownCheckBoxOnAction(ActionEvent event) {
        if (((CheckBox) event.getSource()).isSelected()) {
            setUnknownArtistOnComboBox();
            this.artistComboBox.setDisable(true);

        } else {
            this.artistComboBox.setDisable(false);
        }
    }

    private void setUnknownArtistOnComboBox() {
        ArtistsFx artistsFx = null;
        for (ArtistsFx artistFxFromList : this.songFxModel.getArtistsFxObservableList()) {
            if (artistFxFromList != null && artistFxFromList.getName().equals(resourceBundle.getString("unknown.artists"))) {
                artistsFx = artistFxFromList;
                break;
            }
        }
        this.artistComboBox.setValue(artistsFx);
    }

    @FXML
    void cancelButton() {
        stage.close();
    }

    @FXML
    void saveButtonOnAction() {
        final SongFxModel taskSongFxModel = this.songFxModel;
        Task<SongFxModel> updateSongTask = new Task<SongFxModel>() {

            @Override
            protected SongFxModel call() {
                try {
                    taskSongFxModel.createOrUpdateSongInDataBase();
                    songsController.reinitFxModel();
                } catch (ApplicationExceptions applicationExceptions) {
                    applicationExceptions.printStackTrace();
                }
                return taskSongFxModel;
            }
        };
        executor.execute(updateSongTask);

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    Stage getStage() {
        return stage;
    }

    void setSongsController(SongsController songsController) {
        this.songsController = songsController;
    }

    void setSongFxModel(SongFxModel songFxModel) {
        this.songFxModel = songFxModel;
    }
}
