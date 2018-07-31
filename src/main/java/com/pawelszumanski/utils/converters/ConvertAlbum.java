/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.converters;

import com.pawelszumanski.database.models.Albums;
import com.pawelszumanski.modelFx.AlbumsFx;

public class ConvertAlbum {
    public static AlbumsFx convertToAlbumFx(Albums album) {
        AlbumsFx albumsFx = new AlbumsFx();
        albumsFx.setId(album.get_id());
        albumsFx.setName(album.getName());
        return albumsFx;
    }
}
