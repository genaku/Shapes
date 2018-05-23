package com.gena.domain.usecases.interfaces

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IInteractor {
    fun startObserving()
    fun stopObserving()
    fun addShape(type: ShapeType, x0: Int, y0: Int)
    fun addPicture(x0: Int, y0: Int, filename: String)
    fun deleteSelected()
    fun undo()
    fun redo()
    fun saveShapes()
    fun moveSelected(x: Int, y: Int)
    fun movementFinished()
    fun findSelection(x: Int, y: Int)
}