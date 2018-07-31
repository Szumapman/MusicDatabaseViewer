/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.main;

import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_32_PNG;
import static com.pawelszumanski.utils.PathUtils.MAIN_WINDOW_FXML;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Locale.setDefault(new Locale("en"));
        Pane pane = FxmlUtils.fxmlLoader(MAIN_WINDOW_FXML);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResource(ICONS_MUSIC_BASE_32_PNG).toString()));
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.app"));
        primaryStage.setOnCloseRequest(event -> {
            if(!DialogsUtils.closeAppConfirmationDialog()){
                event.consume();
            }
        });
        primaryStage.show();
    }


}
