package com.gena.domain.usecases.interfaces

import com.gena.domain.model.figures.Point

/**
 * Created by Gena Kuchergin on 03.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface ISelectionInteractor {
    fun findSelection(point: Point)
    fun moveSelectedTo(point: Point)
    fun movementFinished()
}