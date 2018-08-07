/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils.tasks;

import com.pawelszumanski.modelFx.AlbumFxModel;
import javafx.concurrent.Task;

public class AlbumFxModelTask extends Task<AlbumFxModel> {

@Override
protected AlbumFxModel call() throws Exception {
        AlbumFxModel tempAlbumFxModel = new AlbumFxModel();
        tempAlbumFxModel.init();
        return tempAlbumFxModel;
        }
}
