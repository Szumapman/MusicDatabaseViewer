/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.database.dao.AlbumsDao;
import com.pawelszumanski.database.dao.ArtistsDao;
import com.pawelszumanski.database.dao.SongsDao;
import com.pawelszumanski.database.models.Albums;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.database.models.Songs;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.converters.ConvertAlbum;
import com.pawelszumanski.utils.converters.ConvertArtist;
import com.pawelszumanski.utils.converters.ConvertSong;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SongFxModel {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<SongsFx> songsFxObservableList = FXCollections.observableArrayList();
    private ObjectProperty<SongsFx> songsFxObjectProperty = new SimpleObjectProperty<>(new SongsFx());
    private ObservableList<AlbumsFx> albumsFxObservableList = FXCollections.observableArrayList();
    private ObservableList<ArtistsFx> artistsFxObservableList = FXCollections.observableArrayList();

    private List<SongsFx> songsFxList = new ArrayList<>();

    public void init() throws ApplicationExceptions {
        initSongsList();
        initAlbumsList();
        initArtistsList();
    }

    private void initSongsList() throws ApplicationExceptions {
        songsFxList.clear();
        SongsDao songsDao = new SongsDao();
        List<Songs> songs = songsDao.queryForAll(Songs.class);
        songs.sort((song1, song2) -> song1.getTitle().compareToIgnoreCase(song2.getTitle()));
        songs.forEach(song -> {
            this.songsFxList.add(ConvertSong.convertToSongsFx(song));
        });
        this.songsFxObservableList.setAll(songsFxList);
    }

    private void initAlbumsList() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        albums.forEach(album -> {
            this.albumsFxObservableList.add(ConvertAlbum.convertToAlbumFx(album));
        });
    }

    private void initArtistsList() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artists = artistsDao.queryForAll(Artists.class);
        artists.forEach(artist -> {
            this.artistsFxObservableList.add(ConvertArtist.convertToArtistFx(artist));
        });
    }

    public ObservableList<SongsFx> getSongsFxObservableList() {
        return songsFxObservableList;
    }

    public SongsFx getSongsFxObjectProperty() {
        return songsFxObjectProperty.get();
    }

    public ObjectProperty<SongsFx> songsFxObjectPropertyProperty() {
        return songsFxObjectProperty;
    }

    public void setSongsFxObjectProperty(SongsFx songsFxObjectProperty) {
        this.songsFxObjectProperty.set(songsFxObjectProperty);
    }

    public ObservableList<AlbumsFx> getAlbumsFxObservableList() {
        return albumsFxObservableList;
    }

    public void setAlbumsFxObservableList(ObservableList<AlbumsFx> albumsFxObservableList) {
        this.albumsFxObservableList = albumsFxObservableList;
    }

    public ObservableList<ArtistsFx> getArtistsFxObservableList() {
        return artistsFxObservableList;
    }

    public void setArtistsFxObservableList(ObservableList<ArtistsFx> artistsFxObservableList) {
        this.artistsFxObservableList = artistsFxObservableList;
    }

    public void createOrUpdateSongInDataBase() throws ApplicationExceptions {
        SongsDao songsDao = new SongsDao();
        Songs tempSong = null;
        if(getSongsFxObjectProperty().getId() != 0){
            tempSong = songsDao.findByID(Songs.class, getSongsFxObjectProperty().getId());
        } else {
            tempSong = new Songs();
        }
        tempSong.setTitle(getSongsFxObjectProperty().getTitle());
        tempSong.setTrack(getSongsFxObjectProperty().getTrack());
        AlbumsDao albumsDao = new AlbumsDao();
        Albums tempAlbum = albumsDao.findByID(Albums.class, getSongsFxObjectProperty().albumsFxProperty().get().getId());
        tempAlbum.setArtist(ConvertArtist.convertToArtist(getSongsFxObjectProperty().getAlbumsFx().getArtistFx()));
        albumsDao.createOrUpdate(tempAlbum);
        tempSong.setAlbum(tempAlbum);
        songsDao.createOrUpdate(tempSong);
//        init();
    }

}
