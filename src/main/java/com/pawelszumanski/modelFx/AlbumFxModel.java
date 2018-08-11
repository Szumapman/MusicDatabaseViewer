/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.controllers.WaitWindow;
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
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_16_PNG;
import static com.pawelszumanski.utils.PathUtils.ICONS_SONG_PNG;

public class AlbumFxModel implements WaitWindow {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<AlbumsFx> albumsList = FXCollections.observableArrayList();
    private ObservableList<ArtistsFx> artistsFxObservableList = FXCollections.observableArrayList();
    private ObservableList<SongsFx> songsFxObservableList = FXCollections.observableArrayList();
    private ObjectProperty<AlbumsFx> albumsFxObjectProperty = new SimpleObjectProperty<>(new AlbumsFx());
    private TreeItem<String> root = new TreeItem<>();

    public void init() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        albums.sort((album1, album2) -> album1.getName().compareTo(album2.getName()));
        initAlbumsList(albums);
        initRoot(albums);
        initArtistsFxObservableList();
    }

    private void initArtistsFxObservableList() throws ApplicationExceptions {
        artistsFxObservableList.clear();
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artistsList = artistsDao.queryForAll(Artists.class);
        artistsList.sort((artist1, artist2) -> artist1.getName().compareToIgnoreCase(artist2.getName()));
        artistsList.forEach(artist -> {
            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(artist);
            this.artistsFxObservableList.add(artistsFx);
        });

    }

    public void initSongsFxObservableList() throws ApplicationExceptions {
        songsFxObservableList.clear();
        SongsDao songsDao = new SongsDao();
        List<Songs> songsList = songsDao.queryForAll(Songs.class);
        songsList.forEach(song -> {
            if(song.getAlbum().get_id() == albumsFxObjectProperty.get().getId()){
                SongsFx songsFx = ConvertSong.convertToSongsFx(song);
                songsFxObservableList.add(songsFx);
            }
            songsFxObservableList.sort(Comparator.comparingInt(SongsFx::getTrack));
        });
    }

    public void saveAlbumInDataBase(String name, int artistId) throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        ArtistsDao artistsDao = new ArtistsDao();
        Artists artist = artistsDao.findByID(Artists.class, artistId);
        Albums album = new Albums();
        album.setName(name);
        album.setArtist(artist);
        albumsDao.createOrUpdate(album);
    }

    public void updateAlbumInDataBase() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        Albums tempAlbum = albumsDao.findByID(Albums.class, getAlbumsFxObjectProperty().getId());
        tempAlbum.setName(getAlbumsFxObjectProperty().getName());
        ArtistsDao artistsDao = new ArtistsDao();
        Artists tempArtist = artistsDao.findByID(Artists.class, getAlbumsFxObjectProperty().getArtistFx().getId());
        tempAlbum.setArtist(tempArtist);
        albumsDao.createOrUpdate(tempAlbum);
    }

    public void deleteAlbumById() throws ApplicationExceptions, SQLException {
        if(this.getAlbumsFxObjectProperty().getName().equals(resourceBundle.getString("unknown.album")) &&
            this.getAlbumsFxObjectProperty().getArtistFx().getName().equals(resourceBundle.getString("unknown.artists"))){
            deleteSongs();
        } else {
            AlbumsDao albumsDao = new AlbumsDao();
            albumsDao.deleteById(Albums.class, this.getAlbumsFxObjectProperty().getId());
            deleteSongs();
        }
    }

    private void deleteSongs() throws ApplicationExceptions, SQLException {
        SongsDao songsDao = new SongsDao();
        songsDao.deleteByColumnName(Songs.class, Songs.ALBUM_ID, this.getAlbumsFxObjectProperty().getId());
    }


    private void initRoot(List<Albums> albums) {
        this.root.getChildren().clear();
        albums.forEach(a->{
            TreeItem<String> albumItem = new TreeItem<>(a.getName());
            albumItem.setGraphic(new ImageView(this.getClass().getResource(ICONS_MUSIC_BASE_16_PNG).toString()));
            a.getSongs().forEach(songs -> {
                TreeItem<String> songItem = new TreeItem<>(songs.getTitle());
                songItem.setGraphic(new ImageView(this.getClass().getResource(ICONS_SONG_PNG).toString()));
                albumItem.getChildren().add(songItem);
            });
            root.getChildren().add(albumItem);
        });
    }

    private void initAlbumsList(List<Albums> albums) {
        this.albumsList.clear();
        albums.forEach(a->{
            AlbumsFx albumsFx = ConvertAlbum.convertToAlbumFx(a);
                albumsList.add(albumsFx);
        });
    }

    public AlbumsFx setUnknownAlbum() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        this.setAlbumsFxObjectProperty(null);
        for (Albums album : albums) {
            if (album.getName().equals(resourceBundle.getString("unknown.album"))) {
                this.setAlbumsFxObjectProperty(ConvertAlbum.convertToAlbumFx(album));
                break;
            }
        }
        if (albumsFxObjectProperty == null) {
            Albums unknownAlbum = new Albums();
            unknownAlbum.setName(resourceBundle.getString("unknown.album"));
            albumsDao.createOrUpdate(unknownAlbum);
            setAlbumsFxObjectProperty(ConvertAlbum.convertToAlbumFx(unknownAlbum));
        }

//        this.init();
        return this.getAlbumsFxObjectProperty();
    }


    public ObservableList<AlbumsFx> getAlbumsList() {
        return albumsList;
    }

    public void setAlbumsList(ObservableList<AlbumsFx> albumsList) {
        this.albumsList = albumsList;
    }

    public AlbumsFx getAlbumsFxObjectProperty() {
        return albumsFxObjectProperty.get();
    }

    public ObjectProperty<AlbumsFx> albumsFxObjectPropertyProperty() {
        return albumsFxObjectProperty;
    }

    public ObservableList<ArtistsFx> getArtistsFxObservableList() {
        return artistsFxObservableList;
    }

    public void setArtistsFxObservableList(ObservableList<ArtistsFx> artistsFxObservableList) {
        this.artistsFxObservableList = artistsFxObservableList;
    }

    public void setAlbumsFxObjectProperty(AlbumsFx albumsFxObjectProperty) {
        this.albumsFxObjectProperty.set(albumsFxObjectProperty);
    }

    public ObservableList<SongsFx> getSongsFxObservableList() {
        return songsFxObservableList;
    }

    public void setSongsFxObservableList(ObservableList<SongsFx> songsFxObservableList) {
        this.songsFxObservableList = songsFxObservableList;
    }

    public TreeItem<String> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }



}


