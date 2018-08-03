/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import javafx.fxml.FXML;

import static com.pawelszumanski.utils.PathUtils.ALBUMS_FXML;
import static com.pawelszumanski.utils.PathUtils.ARTISTS_FXML;
import static com.pawelszumanski.utils.PathUtils.SONGS_FXML;

public class TopMenuButtonsController {

    private MainController mainController;

    @FXML
    public void openSongs( ) {
        mainController.setCenter(SONGS_FXML);
    }

    @FXML
    public void openAlbums( ) {
        mainController.setCenter(ALBUMS_FXML);
    }

    @FXML
    public void openArtists( ) {

        mainController.setCenter(ARTISTS_FXML);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }
}
