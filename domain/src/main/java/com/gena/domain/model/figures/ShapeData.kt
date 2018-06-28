package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants
import java.io.Serializable

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
data class ShapeData(
        val type: ShapeType,
        val topLeft: Point = Point(0, 0),
        val bottomRight: Point = Point(Constants.DEFAULT_SIZE, Constants.DEFAULT_SIZE),
        val filename: String = ""
) : Serializable {
    companion object {
        private const val serialVersionUID = 4004118283591821125L
    }
}