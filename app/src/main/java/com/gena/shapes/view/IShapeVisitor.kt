package com.gena.shapes.view

import com.gena.domain.model.figures.Oval
import com.gena.domain.model.figures.Rectangle
import com.gena.domain.model.figures.Triangle

interface IShapeVisitor {
    fun visit(shape: Oval): Boolean
    fun visit(shape: Rectangle): Boolean
    fun visit(shape: Triangle): Boolean
}
