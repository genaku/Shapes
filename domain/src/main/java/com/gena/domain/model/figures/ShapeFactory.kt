package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapeFactory {
    companion object {

        fun getShape(type: ShapeType): Shape = when (type) {
            ShapeType.RECTANGLE -> Rectangle()
            ShapeType.OVAL -> Oval()
            ShapeType.TRIANGLE -> Triangle()
            ShapeType.PICTURE -> Picture()
        }

        fun getShape(shapeData: ShapeData): Shape = when (shapeData.type) {
            ShapeType.RECTANGLE -> Rectangle(shapeData)
            ShapeType.OVAL -> Oval(shapeData)
            ShapeType.TRIANGLE -> Triangle(shapeData)
            ShapeType.PICTURE -> Picture(shapeData)
        }

    }
}
