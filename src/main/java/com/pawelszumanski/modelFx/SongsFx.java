/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import javafx.beans.property.*;

public class SongsFx {
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty track = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty();
    private ObjectProperty<AlbumsFx> albumsFx = new SimpleObjectProperty<>();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getTrack() {
        return track.get();
    }

    public IntegerProperty trackProperty() {
        return track;
    }

    public void setTrack(int track) {
        this.track.set(track);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public AlbumsFx getAlbumsFx() {
        return albumsFx.get();
    }

    public ObjectProperty<AlbumsFx> albumsFxProperty() {
        return albumsFx;
    }

    public void setAlbumsFx(AlbumsFx albumsFx) {
        this.albumsFx.set(albumsFx);
    }

    @Override
    public String toString() {
        return track.getValue() + ". " + title.getValue() ;
    }
}
