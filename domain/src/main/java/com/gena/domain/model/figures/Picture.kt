package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants

/**
 * Created by Gena Kuchergin on 18.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Picture(data: ShapeData = ShapeData(
        type = ShapeType.PICTURE,
        xValues = ArrayList(DEFAULT_COORDS),
        yValues = ArrayList(DEFAULT_COORDS),
        filename = ""
)) : Shape(data) {

    override val numberOfPoints: Int = 2

    val filename
        get() = data.filename

    companion object {
        private val DEFAULT_COORDS = arrayListOf(0, Constants.DEFAULT_SIZE)
        private const val serialVersionUID = -7893724938095347677L
    }

}