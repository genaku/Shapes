package com.gena.domain.model.history

import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
abstract class Command {

    private var mExecuted: Boolean = false

    init {
        mExecuted = false
    }

    @Throws(ShapeException::class)
    fun execute(model: ShapesModel) {
        if (mExecuted)
            throw ShapeException(ShapeException.COMMAND_EXECUTED)
        doExecute(model)
        mExecuted = true
    }

    @Throws(ShapeException::class)
    fun unexecute(model: ShapesModel) {
        if (!mExecuted)
            throw ShapeException(ShapeException.COMMAND_NOT_EXECUTED)
        undoExecute(model)
        mExecuted = false
    }

    fun setExecuted(value: Boolean) {
        mExecuted = value
    }

    @Throws(ShapeException::class)
    abstract fun doExecute(model: ShapesModel)

    @Throws(ShapeException::class)
    abstract fun undoExecute(model: ShapesModel)

}
