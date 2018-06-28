package com.gena.domain.model.selector

import com.gena.domain.model.figures.*

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
fun Shape.contains(point: Point): Boolean = when (this) {
    is Rectangle -> contains(point)
    is Oval -> contains(point)
    is Triangle -> contains(point)
    is Picture -> contains(point)
}

fun Rectangle.contains(point: Point): Boolean =
        (point.x in left..right && point.y in top..bottom)

fun Oval.contains(point: Point): Boolean {
    // get oval center
    val x0 = (right + left) / 2.0
    val y0 = (top + bottom) / 2.0
    // get oval radius
    val r1 = Math.abs(right - left) / 2.0
    val r2 = Math.abs(top - bottom) / 2.0

    return Math.pow((point.x - x0) / r1, 2.0) + Math.pow((point.y - y0) / r2, 2.0) <= 1
}

fun Triangle.contains(point: Point): Boolean {
    // get triangle point A
    val aAx = bottomLeftVertex.x
    val aAy = bottomLeftVertex.y

    // get triangle point B
    val aBx = topVertex.x
    val aBy = topVertex.y

    // get triangle point C
    val aCx = bottomRightVertex.x
    val aCy = bottomRightVertex.y

    // move triangle to position with À(0;0).
    val bx = aBx - aAx
    val by = aBy - aAy
    val cx = aCx - aAx
    val cy = aCy - aAy
    val px = point.x - aAx
    val py = point.y - aAy

    val myu = (px * by - bx * py).toFloat() / (cx * by - bx * cy).toFloat()

    if (myu in 0f..1f) {
        val lambda = (px - myu * cx) / bx
        if (lambda >= 0f && myu + lambda <= 1f) {
            return true
        }
    }

    return false
}

fun Picture.contains(point: Point): Boolean =
        (point.x in left..right && point.y in top..bottom)

