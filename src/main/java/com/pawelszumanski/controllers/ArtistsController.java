/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import com.pawelszumanski.utils.tasks.ArtistFxModelTask;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ArtistsController extends SuperWaitWindow {

    @FXML
    private TreeView<String> artistsTreeView;

    @FXML
    private TextField artistTextField;

    @FXML
    private Button saveArtistButton;

    @FXML
    private ComboBox<ArtistsFx> artistsComboBox;

    @FXML
    private Button editArtistButton;

    @FXML
    private Button deleteArtistButton;


    private ArtistFxModel artistFxModel;
    private Executor executor;


    @FXML
    public void initialize(){
        this.artistFxModel = new ArtistFxModel();
        try {
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
        this.saveArtistButton.disableProperty().bind(artistTextField.textProperty().isEmpty());
        this.editArtistButton.disableProperty().bind(this.artistFxModel.artistsFxObjectPropertyProperty().isNull());
        this.deleteArtistButton.disableProperty().bind(this.artistFxModel.artistsFxObjectPropertyProperty().isNull());
        this.artistsComboBox.setItems(this.artistFxModel.getArtistsFxObservableList());
        this.artistsTreeView.setRoot(this.artistFxModel.getRoot());
    }


    @FXML
    private void saveArtistOnAction() {
        showWaitWindow();
        final ArtistFxModel taskArtistFxModel = this.artistFxModel;
        final String newArtistName = artistTextField.getText();
        Task<ArtistFxModel> saveArtistTask = new Task<ArtistFxModel>() {
            @Override
            protected ArtistFxModel call() {
                try {
                    taskArtistFxModel.saveArtistInDataBase(newArtistName);
                } catch (ApplicationExceptions applicationExceptions) {
                    DialogsUtils.errorDialog(applicationExceptions.getMessage());
                }
                return taskArtistFxModel;
            }
        };
        saveArtistTask.setOnSucceeded(e-> reinitArtistFxModel());
        executor.execute(saveArtistTask);
    }

    @FXML
    private void artistsComboBoxOnAction() {
        this.artistFxModel.setArtistsFxObjectProperty(this.artistsComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editArtistOnAction() {

        String newArtistName = DialogsUtils.editDialog(this.artistFxModel.getArtistsFxObjectProperty().getName());
        if(newArtistName != null && (!newArtistName.equals(this.artistFxModel.getArtistsFxObjectProperty().getName()))){
            showWaitWindow();
            this.artistFxModel.getArtistsFxObjectProperty().setName(newArtistName);
            final ArtistFxModel taskArtistFxModel = this.artistFxModel;
            Task<ArtistFxModel> updateArtistTask = new Task<ArtistFxModel>() {
                @Override
                protected ArtistFxModel call() {
                    try {
                        taskArtistFxModel.updateArtistInDataBase();
                    } catch (ApplicationExceptions applicationExceptions) {
                        DialogsUtils.errorDialog(applicationExceptions.getMessage());
                    }
                    return taskArtistFxModel;
                }
            };
            updateArtistTask.setOnSucceeded(e-> reinitArtistFxModel());
            executor.execute(updateArtistTask);
        }
    }

    @FXML
    private void deleteArtistOnAction() {
        String artistToDelete = this.artistFxModel.getArtistsFxObjectProperty().getName();
        boolean deleteArtist = DialogsUtils.deleteConfirmationDialog(artistToDelete);
        if(deleteArtist){
            showWaitWindow();
            final ArtistFxModel taskArtistFxModel = this.artistFxModel;
            Task<ArtistFxModel> deleteArtistTask = new Task<ArtistFxModel>() {
                @Override
                protected ArtistFxModel call()  {
                    try {
                        taskArtistFxModel.deleteArtistById();
                    } catch (ApplicationExceptions applicationExceptions) {
                        DialogsUtils.errorDialog(applicationExceptions.getMessage());
                    }
                    return taskArtistFxModel;
                }
            };
            deleteArtistTask.setOnSucceeded(e-> reinitArtistFxModel());
            executor.execute(deleteArtistTask);
        }
    }

    private void reinitArtistFxModel() {
        Task<ArtistFxModel> createArtistFxModelTask = new ArtistFxModelTask();
        createArtistFxModelTask.setOnSucceeded(e1 -> {
            this.artistFxModel = createArtistFxModelTask.getValue();
            bindings();
            artistTextField.clear();
            closeWaitWindow();
        });
        executor.execute(createArtistFxModelTask);
    }
}
