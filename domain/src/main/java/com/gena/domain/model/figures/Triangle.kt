package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Triangle(data: ShapeData = ShapeData(
        type = ShapeType.TRIANGLE,
        xValues = ArrayList(DEFAULT_X_VALUES),
        yValues = ArrayList(DEFAULT_Y_VALUES)
)) : Shape(data) {

    override val numberOfPoints: Int = 3

    companion object {
        private val DEFAULT_X_VALUES = arrayListOf(0, Constants.DEFAULT_SIZE / 2, Constants.DEFAULT_SIZE)
        private val DEFAULT_Y_VALUES = arrayListOf(Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
        private const val serialVersionUID = 7647815328629665556L
    }

}