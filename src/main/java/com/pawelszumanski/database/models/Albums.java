/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.database.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "albums")
public class Albums implements BaseModel {
    public static final String ARTISTS_ID = "artists_id";

    public Albums(){}

    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = ARTISTS_ID, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Artists artist;

    @ForeignCollectionField(columnName = "songs_id")
   private ForeignCollection<Songs> songs;

    public static String getArtistsId() {
        return ARTISTS_ID;
    }

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

    public Artists getArtist() {
        return artist;
    }

    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    public ForeignCollection<Songs> getSongs() {
        return songs;
    }

    public void setSongs(ForeignCollection<Songs> songs) {
        this.songs = songs;
    }
}
