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
    private void openSongs( ) {
        mainController.setCenter(SONGS_FXML);

    }

    @FXML
    private void openAlbums( ) {

        mainController.setCenter(ALBUMS_FXML);
    }

    @FXML
    private void openArtists( ) {

        mainController.setCenter(ARTISTS_FXML);
    }

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
