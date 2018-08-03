/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.converters;

import com.pawelszumanski.database.models.Songs;
import com.pawelszumanski.modelFx.SongsFx;

public class ConvertSong {
    public static SongsFx convertToSongsFx(Songs song){
        SongsFx songsFx = new SongsFx();
        songsFx.setId(song.get_id());
        songsFx.setTrack(song.getTrack());
        songsFx.setTitle(song.getTitle());
        if(song.getAlbum() != null) {
            songsFx.setAlbumsFx(ConvertAlbum.convertToAlbumFx(song.getAlbum()));
        }
        return songsFx;
    }
}
