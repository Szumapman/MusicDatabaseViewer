/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SongsController extends SuperWaitWindow{
    @FXML
    private TableView<?> songsTableView;

    @FXML
    private TableColumn<?, ?> songColunm;

    @FXML
    private TableColumn<?, ?> albumColumn;

    @FXML
    private TableColumn<?, ?> artistColumn;

    @FXML
    private Button addSongButton;

    @FXML
    void addSongButtonOnAction() {

    }
}
