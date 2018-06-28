package com.gena.domain.model.history

import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.PictureData

/**
 * Created by Gena Kuchergin on 20.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandCreatePicture(
        private val pictureData: PictureData
) : Command() {

    private var mKey: KeyData = NO_SELECTED

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mKey = model.addPicture(pictureData)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.delete(mKey)
    }

}