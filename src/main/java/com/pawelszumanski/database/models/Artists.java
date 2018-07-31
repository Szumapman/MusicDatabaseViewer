/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.database.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "artists")
public class Artists implements BaseModel {

    public Artists(){}

    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(columnName = "name", canBeNull = false, unique = true)
    private String name;

    @ForeignCollectionField(columnName = "albums_id")
    private ForeignCollection<Albums> albums;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollection<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(ForeignCollection<Albums> albums) {
        this.albums = albums;
    }
}
