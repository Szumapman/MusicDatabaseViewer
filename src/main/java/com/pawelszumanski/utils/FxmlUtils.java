/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ResourceBundle;

public class FxmlUtils extends Task {

    public static final String BUNDLES_MESSAGES = "bundles.messages";


    private String fxmlPath;

    public static Pane fxmlLoader(String fxmlPath){

        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        try {
            return loader.load();
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        return null;
    }

    public static FXMLLoader getLoader(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        return loader;
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(BUNDLES_MESSAGES);
    }

    @Override
    protected Pane call() throws Exception {
        return fxmlLoader(fxmlPath);
    }

    public void setFxmlPath(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
}
