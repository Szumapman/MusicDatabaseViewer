/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.database.dao.AlbumsDao;
import com.pawelszumanski.database.dao.ArtistsDao;
import com.pawelszumanski.database.models.Albums;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.converters.ConvertAlbum;
import com.pawelszumanski.utils.converters.ConvertArtist;
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
    private ObjectProperty<AlbumsFx> albumsFxObjectProperty = new SimpleObjectProperty<>();
    private TreeItem<String> root = new TreeItem<>();

    public void init() throws ApplicationExceptions {
        initArtistsFxObservableList();
        AlbumsDao albumsDao = new AlbumsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        initAlbumsList(albums);
        initRoot(albums);
    }

    private void initArtistsFxObservableList() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artistsList = artistsDao.queryForAll(Artists.class);
        artistsList.forEach(artist -> {
            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(artist);
            this.artistsFxObservableList.add(artistsFx);
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
            if(!albumsFx.getName().equals(resourceBundle.getString("unknown.album"))){
                albumsList.add(albumsFx);
            }
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

    public void setAlbumsFxObjectProperty(AlbumsFx albumsFxObjectProperty) {
        this.albumsFxObjectProperty.set(albumsFxObjectProperty);
    }

    public TreeItem<String> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }


}