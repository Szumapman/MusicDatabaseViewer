/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {


    @FXML
    private BorderPane mainWindow;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;


    @FXML
    private void initialize(){
        topMenuButtonsController.setMainController(this);
    }


    public void setCenter(String fxmlPath){
        DialogsUtils.operationInProgressShow();
        mainWindow.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
        DialogsUtils.operationInProgressClose();
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
    private void setModenaStyleOnAction(ActionEvent actionEvent) {
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
