/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.converters;

import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.modelFx.ArtistsFx;

public class ConvertArtist {
    public static ArtistsFx convertToArtistFx(Artists artist){
        ArtistsFx artistsFx = new ArtistsFx();
        artistsFx.setId(artist.get_id());
        artistsFx.setName(artist.getName());
        return artistsFx;
    }

    public static Artists convertToArtist(ArtistsFx artistsFx) {
        Artists artist = new Artists();
        artist.set_id(artistsFx.getId());
        artist.setName(artistsFx.getName());
        return artist;
    }
}
