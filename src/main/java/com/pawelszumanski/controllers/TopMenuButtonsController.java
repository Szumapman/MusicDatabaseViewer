package com.pawelszumanski.controllers;

import javafx.fxml.FXML;

public class TopMenuButtonsController {

    private MainController mainController;

    @FXML
    public void openSongs( ) {
        System.out.println("openSongs");
    }

    @FXML
    public void openAlbums( ) {
        System.out.println("openAlbums");
    }

    @FXML
    public void openArtists( ) {
        System.out.println("openArtists");
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
