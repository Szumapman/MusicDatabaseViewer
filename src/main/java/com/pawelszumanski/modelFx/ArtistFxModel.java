/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.database.dao.ArtistsDao;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.utils.FxmlUtils;
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

import static com.pawelszumanski.utils.PathUtils.ICONS_ARTIST_PNG;
import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_16_PNG;

public class ArtistFxModel {

    private static ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    private ObservableList<ArtistsFx> artistsList = FXCollections.observableArrayList();
    private ObjectProperty<ArtistsFx> artistsFxObjectProperty = new SimpleObjectProperty<>();
    private TreeItem<String> root = new TreeItem<>();

    public void init() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artists = artistsDao.queryForAll(Artists.class);
        initArtistList(artists);
        initRoot(artists);
    }

    public void saveArtistInDataBase(String name) throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        Artists arist = new Artists();
        arist.setName(name);
        artistsDao.createOrUpdate(arist);
        init();
    }

    public void updateArtistInDataBase() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        Artists tempArtist = artistsDao.findByID(Artists.class, getArtistsFxObjectProperty().getId());
        tempArtist.setName(getArtistsFxObjectProperty().getName());
        artistsDao.createOrUpdate(tempArtist);
        init();
    }


    public void deleteArtistById() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        artistsDao.deleteById(Artists.class, this.getArtistsFxObjectProperty().getId());
        /*
        Uzupełnić usuwanie albumów i dalej piosenek.
         */
        this.init();
    }

    public void setUnknownArtist() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        List<Artists> artists = artistsDao.queryForAll(Artists.class);
        this.setArtistsFxObjectProperty(null);
        for (Artists a : artists) {
            if (a.getName().equals(resourceBundle.getString("unknown.artists"))) {
                this.setArtistsFxObjectProperty(ConvertArtist.convertToArtistFx(a));
                break;
            }
        }
        if (artistsFxObjectProperty == null) {
            Artists unknownArtist = new Artists();
            unknownArtist.setName(resourceBundle.getString("unknown.artists"));
            artistsDao.createOrUpdate(unknownArtist);
            setArtistsFxObjectProperty(ConvertArtist.convertToArtistFx(unknownArtist));
        }
        this.init();
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

    private void initArtistList(List<Artists> artists) {
        this.artistsList.clear();
        artists.forEach(a -> {
            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(a);
            if(!artistsFx.getName().equals(resourceBundle.getString("unknown.artists"))){
                artistsList.add(artistsFx);
            }
        });
    }

    public ObservableList<ArtistsFx> getArtistsList() {
        return artistsList;
    }

    public void setArtistsList(ObservableList<ArtistsFx> artistsList) {
        this.artistsList = artistsList;
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
