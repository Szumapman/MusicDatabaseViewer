/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.pawelszumanski.utils.PathUtils.WAIT_WINDOW_FXML;

public interface WaitWindow {
    default Stage getWaitStage(){
        FXMLLoader loader = FxmlUtils.getLoader(WAIT_WINDOW_FXML);
        Scene waitScene = null;
        try {
            waitScene = new Scene(loader.load());
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(waitScene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        return stage;
    }
}
