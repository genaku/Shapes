package com.gena.domain.model.history

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandCreate(
        private val shapeType: ShapeType
) : Command() {

    private var mKey: KeyData = NO_SELECTED

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mKey = model.add(shapeType)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.delete(mKey)
    }

}
