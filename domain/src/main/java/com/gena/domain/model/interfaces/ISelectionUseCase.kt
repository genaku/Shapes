package com.gena.domain.model.interfaces

import com.gena.domain.model.figures.Point

interface ISelectionUseCase {
    fun findSelection(point: Point)
    fun moveSelectedTo(point: Point)
    fun movementFinished()
}