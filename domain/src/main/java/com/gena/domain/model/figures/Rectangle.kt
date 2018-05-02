package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Rectangle(data: ShapeData = ShapeData(
        type = ShapeType.RECTANGLE,
        xValues = ArrayList(DEFAULT_COORDS),
        yValues = ArrayList(DEFAULT_COORDS)
)) : Shape(data) {

    override val numberOfPoints: Int = 2

    companion object {
        private val DEFAULT_COORDS = arrayListOf(0, Constants.DEFAULT_SIZE)
        private const val serialVersionUID = -7689944340411885614L
    }
}