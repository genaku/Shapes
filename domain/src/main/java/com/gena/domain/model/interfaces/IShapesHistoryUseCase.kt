package com.gena.domain.model.interfaces

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 12.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IShapesHistoryUseCase {
    fun startObserving()
    fun stopObserving()
    fun addShape(type: ShapeType)
    fun addPicture(filename: String)
    fun deleteSelected()
    fun saveShapes()
    fun undo()
    fun redo()
}