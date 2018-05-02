package com.gena.domain.model.selector

import com.gena.domain.model.figures.Oval
import com.gena.domain.model.figures.Rectangle
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.figures.Triangle

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
fun Shape.contains(x: Int, y: Int): Boolean = when (this) {
    is Rectangle -> contains(x, y)
    is Oval -> contains(x, y)
    is Triangle -> contains(x, y)
    else -> false
}

fun Rectangle.contains(x: Int, y: Int): Boolean =
        (x in left..right && y in top..bottom)

fun Oval.contains(x: Int, y: Int): Boolean {
    // get oval center
    val x0 = (right + left) / 2.0
    val y0 = (top + bottom) / 2.0
    // get oval radius
    val r1 = Math.abs(right - left) / 2.0
    val r2 = Math.abs(top - bottom) / 2.0

    return Math.pow((x - x0) / r1, 2.0) + Math.pow((y - y0) / r2, 2.0) <= 1
}

fun Triangle.contains(x: Int, y: Int): Boolean {
    // get triangle point A
    val aAx = getPointX(0)
    val aAy = getPointY(0)

    // get triangle point B
    val aBx = getPointX(1)
    val aBy = getPointY(1)

    // get triangle point C
    val aCx = getPointX(2)
    val aCy = getPointY(2)

    // move triangle to position with À(0;0).
    val bx = aBx - aAx
    val by = aBy - aAy
    val cx = aCx - aAx
    val cy = aCy - aAy
    val px = x - aAx
    val py = y - aAy

    val myu = (px * by - bx * py).toFloat() / (cx * by - bx * cy).toFloat()

    if (myu in 0f..1f) {
        val lambda = (px - myu * cx) / bx
        if (lambda >= 0f && myu + lambda <= 1f) {
            return true
        }
    }

    return false
}
