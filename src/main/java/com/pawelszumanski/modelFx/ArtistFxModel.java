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
import com.pawelszumanski.utils.converters.ConvertArtist;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static com.pawelszumanski.utils.PathUtils.ICONS_ARTIST_PNG;
import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_16_PNG;

public class ArtistFxModel {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<ArtistsFx> artistsFxObservableList = FXCollections.observableArrayList();
    private ObjectProperty<ArtistsFx> artistsFxObjectProperty = new SimpleObjectProperty<>();
    private TreeItem<String> root = new TreeItem<>();

    public void init() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artists = artistsDao.queryForAll(Artists.class);
        artists.sort(Comparator.comparing(Artists::getName));
        initArtistsFxObservableList(artists);
        initRoot(artists);
    }

    public void saveArtistInDataBase(String name) throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        Artists arist = new Artists();
        arist.setName(name);
        artistsDao.createOrUpdate(arist);
    }

    public void updateArtistInDataBase() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        Artists tempArtist = artistsDao.findByID(Artists.class, getArtistsFxObjectProperty().getId());
        tempArtist.setName(getArtistsFxObjectProperty().getName());
        artistsDao.createOrUpdate(tempArtist);
//        init();
    }


    public void deleteArtistById() throws ApplicationExceptions, SQLException {
        AlbumsDao albumsDao = new AlbumsDao();
        SongsDao songsDao = new SongsDao();
        List<Albums> albums = albumsDao.queryForAll(Albums.class);
        List<Albums> albumsToDelete = new ArrayList<>();
        albums.forEach(album -> {
            if(album.getArtist().get_id() == artistsFxObjectProperty.get().getId()){
                albumsToDelete.add(album);
            }
        });
        for (Albums album : albumsToDelete) {
            if (this.getArtistsFxObjectProperty().getName().equals(resourceBundle.getString("unknown.artists")) ||
                    album.getName().equals(resourceBundle.getString("unknown.album"))) {
                songsDao.deleteByColumnName(Songs.class, Songs.ALBUM_ID, album.get_id());
            } else {
                songsDao.deleteByColumnName(Songs.class, Songs.ALBUM_ID, album.get_id());
                albumsDao.deleteById(Albums.class, album.get_id());
            }
        }
        if(!this.getArtistsFxObjectProperty().getName().equals(resourceBundle.getString("unknown.artists"))){
            ArtistsDao artistsDao = new ArtistsDao();
            artistsDao.deleteById(Artists.class, this.getArtistsFxObjectProperty().getId());
        }
    }

    public ArtistsFx setUnknownArtist() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artists = artistsDao.queryForAll(Artists.class);
        this.setArtistsFxObjectProperty(null);
        for (Artists artist : artists) {
            if (artist.getName().equals(resourceBundle.getString("unknown.artists"))) {
                this.setArtistsFxObjectProperty(ConvertArtist.convertToArtistFx(artist));
                break;
            }
        }
        if (artistsFxObjectProperty == null) {
            Artists unknownArtist = new Artists();
            unknownArtist.setName(resourceBundle.getString("unknown.artists"));
            artistsDao.createOrUpdate(unknownArtist);
            setArtistsFxObjectProperty(ConvertArtist.convertToArtistFx(unknownArtist));
        }
//        this.init();
        return this.getArtistsFxObjectProperty();
    }

    private void initRoot(List<Artists> artists) {
        this.root.getChildren().clear();
        artists.forEach(a -> {
            TreeItem<String> artistItem = new TreeItem<>(a.getName());
            artistItem.setGraphic(new ImageView(this.getClass().getResource(ICONS_ARTIST_PNG).toString()));
            a.getAlbums().forEach(albums -> {
                TreeItem<String> albumItem = new TreeItem<>(albums.getName());
                albumItem.setGraphic(new ImageView(this.getClass().getResource(ICONS_MUSIC_BASE_16_PNG).toString()));
                artistItem.getChildren().add(albumItem);
            });
            root.getChildren().add(artistItem);

        });
    }

    private void initArtistsFxObservableList(List<Artists> artists) {
        this.artistsFxObservableList.clear();
        artists.forEach(a -> {
            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(a);
            if (!artistsFx.getName().equals(resourceBundle.getString("unknown.artists"))) {
                artistsFxObservableList.add(artistsFx);
            }
        });
    }

    public ObservableList<ArtistsFx> getArtistsFxObservableList() {
        return artistsFxObservableList;
    }

    public void setArtistsFxObservableList(ObservableList<ArtistsFx> artistsFxObservableList) {
        this.artistsFxObservableList = artistsFxObservableList;
    }

    public ArtistsFx getArtistsFxObjectProperty() {
        return artistsFxObjectProperty.get();
    }

    public ObjectProperty<ArtistsFx> artistsFxObjectPropertyProperty() {
        return artistsFxObjectProperty;
    }

    public void setArtistsFxObjectProperty(ArtistsFx artistsFxObjectProperty) {
        this.artistsFxObjectProperty.set(artistsFxObjectProperty);
    }


    public TreeItem<String> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }

}
