/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "songs")
public class Songs implements BaseModel{
    public static final String ALBUM_ID = "album";

    public Songs(){}

    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(columnName = "track")
    private int track;

    @DatabaseField(columnName = "title", canBeNull = false)
    private String title;

    @DatabaseField(columnName = ALBUM_ID, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Albums album;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Albums getAlbum() {
        return album;
    }

    public void setAlbum(Albums album) {
        this.album = album;
    }

    public static String getAlbumId() {
        return ALBUM_ID;
    }
}
