/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.main;

import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_32_PNG;
import static com.pawelszumanski.utils.PathUtils.MAIN_WINDOW_FXML;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = FxmlUtils.getLoader(MAIN_WINDOW_FXML);
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        loader.setResources(bundle);
        BorderPane pane = loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
//        primaryStage.getIcons().add(new Image(this.getClass().getResource(ICONS_MUSIC_BASE_16_PNG).toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource(ICONS_MUSIC_BASE_32_PNG).toString()));
        primaryStage.setTitle(bundle.getString("title.app"));
        primaryStage.setOnCloseRequest(event -> {
            if(!DialogsUtils.closeAppConfirmationDialog()){
                event.consume();
            }
        });
        primaryStage.show();
    }


}
