/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import com.pawelszumanski.database.dao.ArtistsDao;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.utils.converters.ConvertArtist;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.List;

public class ArtistFxModel {
    private ObservableList<ArtistsFx> artistsList = FXCollections.observableArrayList();
    private ObjectProperty<ArtistsFx> artist = new SimpleObjectProperty<>();
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
        Artists tempArtist = artistsDao.findByID(Artists.class, getArtist().getId());
        tempArtist.setName(getArtist().getName());
        artistsDao.createOrUpdate(tempArtist);
        init();
    }


    public void deleteArtistById() throws ApplicationExceptions {
        ArtistsDao artistsDao = new ArtistsDao();
        artistsDao.deleteById(Artists.class, this.getArtist().getId());
        /*
        Uzupełnić usuwanie albumów i dalej piosenek.
         */
        this.init();
    }

    private void initRoot(List<Artists> artists) {
        this.root.getChildren().clear();
        artists.forEach(a-> {
            TreeItem<String> artistItem = new TreeItem<>(a.getName());
            a.getAlbums().forEach(albums -> {
                artistItem.getChildren().add(new TreeItem<>(albums.getName()));
            });
            root.getChildren().add(artistItem);
        });
    }

    private void initArtistList(List<Artists> artists) {
        this.artistsList.clear();
        artists.forEach(a-> {
            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(a);
            artistsList.add(artistsFx);
        });
    }

    public ObservableList<ArtistsFx> getArtistsList() {
        return artistsList;
    }

    public void setArtistsList(ObservableList<ArtistsFx> artistsList) {
        this.artistsList = artistsList;
    }

    public ArtistsFx getArtist() {
        return artist.get();
    }

    public ObjectProperty<ArtistsFx> artistProperty() {
        return artist;
    }

    public void setArtist(ArtistsFx artist) {
        this.artist.set(artist);
    }

    public TreeItem<String> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }



}
