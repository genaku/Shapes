package com.gena.domain.model.history

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandMove(
        private val key: KeyData,
        private val shapeMoveMode: ShapeMoveMode,
        private val oldX: Int,
        private val oldY: Int,
        private val newX: Int,
        private val newY: Int
) : Command() {

    @Throws(ShapeException::class)
    override fun doExecute(model: ShapesModel) {
        model.move(key, newX, newY, shapeMoveMode)
        model.setSelected(key)
    }

    @Throws(ShapeException::class)
    override fun undoExecute(model: ShapesModel) {
        model.move(key, oldX, oldY, shapeMoveMode)
        model.setSelected(key)
    }

}
