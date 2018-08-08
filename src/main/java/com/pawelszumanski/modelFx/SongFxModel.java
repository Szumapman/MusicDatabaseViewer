/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.database.dao.SongsDao;
import com.pawelszumanski.database.models.Songs;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.converters.ConvertSong;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SongFxModel {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<SongsFx> songsFxObservableList = FXCollections.observableArrayList();

    private List<SongsFx> songsFxList = new ArrayList<>();

    public void init() throws ApplicationExceptions {
        songsFxList.clear();

        SongsDao songsDao = new SongsDao();
        List<Songs> songs = songsDao.queryForAll(Songs.class);
        songs.forEach(song -> {
            this.songsFxList.add(ConvertSong.convertToSongsFx(song));
        });
        this.songsFxObservableList.setAll(songsFxList);
    }

    public ObservableList<SongsFx> getSongsFxObservableList() {
        return songsFxObservableList;
    }
}
