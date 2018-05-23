package com.gena.domain.model.history

import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.PictureData

/**
 * Created by Gena Kuchergin on 20.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandCreatePicture(
        private val x: Int,
        private val y: Int,
        private val pictureData: PictureData
) : Command() {

    private var mIndex: Int = 0

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mIndex = model.addPicture(x, y, pictureData)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.delete(mIndex)
    }

}