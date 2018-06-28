package com.gena.domain.model.history

import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.ShapeData
import com.gena.domain.model.figures.ShapeFactory

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandDelete(private val key: KeyData) : Command() {

    private lateinit var mShapeData: ShapeData

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        mShapeData = model.getItem(key).data
        model.delete(key)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        val shape = ShapeFactory.createShape(mShapeData)
        model.insert(shape, key)
        model.setSelected(key)
    }

}
