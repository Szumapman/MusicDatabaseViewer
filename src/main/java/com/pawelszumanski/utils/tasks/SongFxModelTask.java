/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.tasks;

import com.pawelszumanski.modelFx.SongFxModel;
import javafx.concurrent.Task;

public class SongFxModelTask extends Task<SongFxModel> {
    @Override
    protected SongFxModel call() throws Exception {
        SongFxModel tempSongFxModel = new SongFxModel();
        tempSongFxModel.init();
        return tempSongFxModel;
    }
}
