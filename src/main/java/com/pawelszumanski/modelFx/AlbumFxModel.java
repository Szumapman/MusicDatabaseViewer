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
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_16_PNG;
import static com.pawelszumanski.utils.PathUtils.ICONS_SONG_PNG;

public class AlbumFxModel {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<AlbumsFx> albumsList = FXCollections.observableArrayList();
    private ObservableList<ArtistsFx> artistsFxObservableList = FXCollections.observableArrayList();
    private ObservableList<SongsFx> songsFxObservableList = FXCollections.observableArrayList();
    private ObjectProperty<AlbumsFx> albumsFxObjectProperty = new SimpleObjectProperty<>(new AlbumsFx());
    private TreeItem<String> root = new TreeItem<>();

    public void init() throws ApplicationExceptions {
        initArtistsFxObservableList();
        AlbumsDao albumsDao = new AlbumsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        initAlbumsList(albums);
        initRoot(albums);
    }

    private void initArtistsFxObservableList() throws ApplicationExceptions {
        artistsFxObservableList.clear();
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artistsList = artistsDao.queryForAll(Artists.class);
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
                System.out.println(songsFx);
            }

            songsFxObservableList.sort((songsFx1, songsFx2) -> songsFx1.getTrack() -  songsFx2.getTrack());
        });

    }

    public void saveAlbumInDataBase(String name) throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        ArtistsDao artistsDao = new ArtistsDao();
        Artists artist = artistsDao.findByID(Artists.class, this.getAlbumsFxObjectProperty().getArtistFx().getId());
        Albums album = new Albums();
        album.setName(name);
        album.setArtist(artist);
        albumsDao.createOrUpdate(album);
        init();
    }

    public void updateAlbumInDataBase() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        Albums tempAlbum = albumsDao.findByID(Albums.class, getAlbumsFxObjectProperty().getId());
        tempAlbum.setName(getAlbumsFxObjectProperty().getName());
        ArtistsDao artistsDao = new ArtistsDao();
        Artists tempArtist = artistsDao.findByID(Artists.class, getAlbumsFxObjectProperty().getArtistFx().getId());
        tempAlbum.setArtist(tempArtist);
        albumsDao.createOrUpdate(tempAlbum);
        init();
    }

    public void deleteAlbumById() throws ApplicationExceptions {
        AlbumsDao albumsDao = new AlbumsDao();
        albumsDao.deleteById(Albums.class, this.getAlbumsFxObjectProperty().getId());
        /*
        Uzupełnić usuwanie piosenek.
         */
        init();
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
//            if(!albumsFx.getName().equals(resourceBundle.getString("unknown.album"))){
                albumsList.add(albumsFx);
//            }
//            System.out.println(albumsFx.getArtistFx() + " " +albumsFx.getArtistFx().getId());
        });
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
