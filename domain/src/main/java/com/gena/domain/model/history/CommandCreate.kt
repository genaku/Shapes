package com.gena.domain.model.history

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandCreate(
        private val shapeType: ShapeType,
        private val x: Int,
        private val y: Int
) : Command() {

    private var mIndex: Int = 0

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mIndex = model.add(shapeType, x, y)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.delete(mIndex)
    }

}
