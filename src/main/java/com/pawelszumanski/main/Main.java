/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.main;

import com.pawelszumanski.database.dao.AlbumsDao;
import com.pawelszumanski.database.dao.ArtistsDao;
import com.pawelszumanski.database.models.Albums;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.modelFx.AlbumFxModel;
import com.pawelszumanski.modelFx.AlbumsFx;
import com.pawelszumanski.modelFx.ArtistFxModel;
import com.pawelszumanski.modelFx.ArtistsFx;
import com.pawelszumanski.utils.DialogsUtils;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.UnknownAlbum;
import com.pawelszumanski.utils.UnkownArtist;
import com.pawelszumanski.utils.converters.ConvertAlbum;
import com.pawelszumanski.utils.converters.ConvertArtist;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

import static com.pawelszumanski.utils.PathUtils.ICONS_MUSIC_BASE_32_PNG;
import static com.pawelszumanski.utils.PathUtils.MAIN_WINDOW_FXML;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Locale.setDefault(new Locale("en"));
        setLocalizedUnknownAlbum();
        setLocalizedUnknownArtist();
        Pane pane = FxmlUtils.fxmlLoader(MAIN_WINDOW_FXML);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResource(ICONS_MUSIC_BASE_32_PNG).toString()));
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.app"));
        primaryStage.setOnCloseRequest(event -> {
            if (!DialogsUtils.closeAppConfirmationDialog()) {
                event.consume();
            }
        });
        primaryStage.show();
    }

    private void setLocalizedUnknownAlbum() {
        AlbumsDao albumsDao = new AlbumsDao();
        try {
            List<Albums> albums = albumsDao.queryForAll(Albums.class);
            UnknownAlbum[] unknownAlbums = UnknownAlbum.values();
            findUnknown:
            for (Albums album : albums) {
                for (UnknownAlbum unknownAlbum : unknownAlbums) {
                    if (album.getName().equals(unknownAlbum.getValue())) {
                        if (!album.getName().equals(FxmlUtils.getResourceBundle().getString("unknown.album"))) {
                            AlbumsFx albumsFx = ConvertAlbum.convertToAlbumFx(album);
                            albumsFx.setName(FxmlUtils.getResourceBundle().getString("unknown.album"));
                            AlbumFxModel albumFxModel = new AlbumFxModel();
                            albumFxModel.setAlbumsFxObjectProperty(albumsFx);
                            albumFxModel.updateAlbumInDataBase();
                            break findUnknown;
                        }

                    }
                }
            }
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }

    }

    private void setLocalizedUnknownArtist() {
        ArtistsDao artistsDao = new ArtistsDao();
        try {
            List<Artists> artists = artistsDao.queryForAll(Artists.class);
            UnkownArtist[] unkownArtists = UnkownArtist.values();
            findUnknown:
            for (Artists artist : artists) {
                for (UnkownArtist unkownArtist : unkownArtists) {
                    if (artist.getName().equals(unkownArtist.getValue())) {
                        if (!artist.getName().equals(FxmlUtils.getResourceBundle().getString("unknown.artists"))) {
                            ArtistsFx artistsFx = ConvertArtist.convertToArtistFx(artist);
                            artistsFx.setName(FxmlUtils.getResourceBundle().getString("unknown.artists"));
                            ArtistFxModel artistFxModel = new ArtistFxModel();
                            artistFxModel.setArtistsFxObjectProperty(artistsFx);
                            artistFxModel.updateArtistInDataBase();
                            break findUnknown;
                        }
                    }
                }
            }
        } catch (ApplicationExceptions applicationExceptions) {
            DialogsUtils.errorDialog(applicationExceptions.getMessage());
        }
    }


}
