package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType
import java.io.Serializable

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
data class ShapeData(
        val type: ShapeType,
        val xValues: ArrayList<Int>,
        val yValues: ArrayList<Int>
) : Serializable {
    companion object {
        private const val serialVersionUID = 4004118283591821125L
    }
}