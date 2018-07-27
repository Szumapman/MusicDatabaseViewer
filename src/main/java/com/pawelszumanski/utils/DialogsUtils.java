/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_32_PNG;

public class DialogsUtils {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

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

    private static void setIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(DialogsUtils.class.getResource(ICONS_MUSIC_BASE_32_PNG).toString()));
    }
}
