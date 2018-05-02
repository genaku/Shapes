package com.gena.domain.model.interfaces

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 12.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IUseCase {
    fun startObserving()
    fun stopObserving()
    fun addShape(type: ShapeType, x0: Int, y0: Int)
    fun deleteSelected()
    fun undo()
    fun redo()
    fun saveModel()
    fun moveSelected(x: Int, y: Int)
    fun movementFinished()
    fun findSelection(x: Int, y: Int)
}