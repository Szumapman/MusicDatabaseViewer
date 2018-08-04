/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_32_PNG;

public class DialogsUtils {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private static Alert operationInProgress;

    public static void dialogAboutApp(){
        Alert aboutAppInformationAlert = new Alert(Alert.AlertType.INFORMATION);
        setIcon(aboutAppInformationAlert);
        aboutAppInformationAlert.setTitle(resourceBundle.getString("about.title"));
        aboutAppInformationAlert.setHeaderText(resourceBundle.getString("about.header"));
        aboutAppInformationAlert.setContentText(resourceBundle.getString("about.content"));
        aboutAppInformationAlert.showAndWait();
    }

    public static boolean closeAppConfirmationDialog(){
        Alert closeAppConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        setIcon(closeAppConfirmationAlert);
        closeAppConfirmationAlert.setTitle(resourceBundle.getString("exit.title"));
        closeAppConfirmationAlert.setHeaderText(resourceBundle.getString("exit.header"));
        Optional<ButtonType> result = closeAppConfirmationAlert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void errorDialog(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        setIcon(errorAlert);
        errorAlert.setTitle(resourceBundle.getString("error.title"));
        errorAlert.setHeaderText(resourceBundle.getString("error.header"));
        errorAlert.setContentText(errorMessage);
        errorAlert.showAndWait();
    }

    private static void setIcon(Dialog alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(DialogsUtils.class.getResource(ICONS_MUSIC_BASE_32_PNG).toString()));
    }

    public static String editDialog(String name) {
        TextInputDialog dialog = new TextInputDialog(name);
        setIcon(dialog);
        dialog.setTitle(resourceBundle.getString("edit.title"));
        dialog.setHeaderText(resourceBundle.getString("edit.header"));
        dialog.setContentText(resourceBundle.getString("edit.content"));
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    public static boolean deleteConfirmationDialog(String dataNameToDelete) {
        Alert deleteConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        setIcon(deleteConfirmationAlert);
        deleteConfirmationAlert.setTitle(resourceBundle.getString("delete.title"));
        deleteConfirmationAlert.setHeaderText(resourceBundle.getString("delete.header") + " " + dataNameToDelete + "?");
        deleteConfirmationAlert.setContentText(resourceBundle.getString("delete.context"));
        Optional<ButtonType> result = deleteConfirmationAlert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void operationInProgressShow(){
        operationInProgress = new Alert(Alert.AlertType.INFORMATION);
        operationInProgress.setTitle(resourceBundle.getString("operation.in.progress"));
        operationInProgress.setHeaderText(resourceBundle.getString("operation.in.progress"));
        operationInProgress.initModality(Modality.APPLICATION_MODAL);
        operationInProgress.show();
    }

    public static void operationInProgressClose() {
        operationInProgress.close();
    }
}
