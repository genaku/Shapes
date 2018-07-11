package com.gena.domain.model.history

import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Shape

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandDelete(private val key: KeyData) : Command() {

    private lateinit var mDeletedShape: Shape

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mDeletedShape = model.getItem(key)
        model.delete(key)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.insert(mDeletedShape, key)
        model.setSelected(key)
    }

}
