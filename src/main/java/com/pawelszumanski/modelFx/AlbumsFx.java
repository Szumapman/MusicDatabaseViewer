/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.modelFx;

import javafx.beans.property.*;

public class AlbumsFx {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<ArtistsFx> artistFx = new SimpleObjectProperty<>();

    public ArtistsFx getArtistFx() {
        return artistFx.get();
    }

    public ObjectProperty<ArtistsFx> artistFxProperty() {
        return artistFx;
    }

    public void setArtistFx(ArtistsFx artistFx) {
        this.artistFx.set(artistFx);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
