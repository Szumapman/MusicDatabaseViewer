/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.tasks;

import com.pawelszumanski.modelFx.ArtistFxModel;
import javafx.concurrent.Task;

public class ArtistFxModelTask extends Task<ArtistFxModel> {

    @Override
    protected ArtistFxModel call() throws Exception {
        ArtistFxModel tempArtistFxModel = new ArtistFxModel();
        tempArtistFxModel.init();
        return tempArtistFxModel;
    }
}