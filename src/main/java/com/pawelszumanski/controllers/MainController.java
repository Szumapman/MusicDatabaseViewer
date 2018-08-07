/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private BorderPane mainWindow;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private VBox centerVBox;

    @FXML
    private Label waitLabel;

    @FXML
    private void initialize(){
        progressIndicator.setVisible(false);
        waitLabel.setVisible(false);
        topMenuButtonsController.setMainController(this);
    }

    private String actualCenterFxmlPath = null;

    void setCenter(String fxmlPath){
        if(!fxmlPath.equals(actualCenterFxmlPath)){
            actualCenterFxmlPath = fxmlPath;
            Task<Pane> task = new Task<Pane>() {
                @Override
                protected Pane call() {
                    return FxmlUtils.fxmlLoader(fxmlPath);
                }
            };

            mainWindow.setCenter(centerVBox);
            progressIndicator.setVisible(true);
            waitLabel.setVisible(true);

            task.setOnSucceeded(e -> {
                progressIndicator.setVisible(false);
                waitLabel.setVisible(false);
                Pane pane = task.getValue();
                mainWindow.setCenter(pane);
            });
            task.setOnFailed(e -> {
                progressIndicator.setVisible(false);
                waitLabel.setVisible(false);
            });

            new Thread(task).start();
        }
    }

    @FXML
    public void closeAppOnAction() {
        if(DialogsUtils.closeAppConfirmationDialog()){
            Platform.exit();
            System.exit(0);
        }
    }
    @FXML
    private void setCaspianStyleOnAction() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    }
    @FXML
    private void setModenaStyleOnAction() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    }
    @FXML
    private void alwaysOnTopOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) mainWindow.getScene().getWindow();
        if (((CheckMenuItem) actionEvent.getSource()).isSelected()){
            stage.setAlwaysOnTop(true);
        } else {
            stage.setAlwaysOnTop(false);
        }
    }
    @FXML
    private void aboutAppOnAction() {
        DialogsUtils.dialogAboutApp();
    }

}
