package com.pawelszumanski.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainWindow.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages");
        loader.setResources(bundle);
        BorderPane pane = loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/music-base-16.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/music-base-32.png").toString()));
        primaryStage.setTitle(bundle.getString("title.app"));
        primaryStage.show();
    }
}
