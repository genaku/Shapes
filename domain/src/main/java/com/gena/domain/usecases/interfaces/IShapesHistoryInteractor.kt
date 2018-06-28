package com.gena.domain.usecases.interfaces

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IShapesHistoryInteractor {
    fun startObserving()
    fun stopObserving()
    fun addShape(type: ShapeType)
    fun addPicture(filename: String)
    fun deleteSelected()
    fun undo()
    fun redo()
    fun saveShapes()
}