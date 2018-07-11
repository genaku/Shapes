package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.extensions.shiftBy
import com.gena.domain.model.Constants
import java.io.Serializable

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
sealed class Shape(val data: ShapeData) : Serializable {

    val top
        get() = data.topLeft.y

    val bottom
        get() = data.bottomRight.y

    val left
        get() = data.topLeft.x

    val right
        get() = data.bottomRight.x

    val width
        get() = right - left

    val height
        get() = bottom - top

    fun moveShape(newPoint: Point, mode: ShapeMoveMode): Boolean = when (mode) {
        ShapeMoveMode.BODY ->
            if (newPoint != data.topLeft) {
                val dx = newPoint.x - left
                val dy = newPoint.y - top
                data.topLeft.shiftBy(dx, dy)
                data.bottomRight.shiftBy(dx, dy)
                true
            } else {
                false
            }
        ShapeMoveMode.LEFT_SIDE ->
            if (newPoint.x == left || leftSideLess(newPoint)) {
                false
            } else {
                data.topLeft.x = newPoint.x
                true
            }
        ShapeMoveMode.RIGHT_SIDE ->
            if (newPoint.x == right || rightSideLess(newPoint)) {
                false
            } else {
                data.bottomRight.x = newPoint.x
                true
            }
        ShapeMoveMode.TOP_SIDE ->
            if (newPoint.y == top || topSideLess(newPoint)) {
                false
            } else {
                data.topLeft.y = newPoint.y
                true
            }
        ShapeMoveMode.BOTTOM_SIDE ->
            if (newPoint.y == bottom || bottomSideLess(newPoint)) {
                false
            } else {
                data.bottomRight.y = newPoint.y
                true
            }
        ShapeMoveMode.LEFT_UPPER_CORNER ->
            if (newPoint == data.topLeft || leftSideLess(newPoint) || topSideLess(newPoint)) {
                false
            } else {
                data.topLeft.x = newPoint.x
                data.topLeft.y = newPoint.y
                true
            }
        ShapeMoveMode.LEFT_BOTTOM_CORNER ->
            if ((newPoint.x == left && newPoint.y == bottom) || leftSideLess(newPoint) || bottomSideLess(newPoint)) {
                false
            } else {
                data.topLeft.x = newPoint.x
                data.bottomRight.y = newPoint.y
                true
            }
        ShapeMoveMode.RIGHT_UPPER_CORNER ->
            if ((newPoint.x == right && newPoint.y == top) || rightSideLess(newPoint) || topSideLess(newPoint)) {
                false
            } else {
                data.bottomRight.x = newPoint.x
                data.topLeft.y = newPoint.y
                true
            }
        ShapeMoveMode.RIGHT_BOTTOM_CORNER ->
            if (newPoint == data.bottomRight || rightSideLess(newPoint) || bottomSideLess(newPoint)) {
                false
            } else {
                data.bottomRight.x = newPoint.x
                data.bottomRight.y = newPoint.y
                true
            }
        ShapeMoveMode.NOTHING ->
            false
    }

    private fun bottomSideLess(newPoint: Point) =
            (newPoint.y - top) < Constants.MIN_SIZE

    private fun topSideLess(newPoint: Point) =
            (bottom - newPoint.y) < Constants.MIN_SIZE

    private fun rightSideLess(newPoint: Point) =
            (newPoint.x - left) < Constants.MIN_SIZE

    private fun leftSideLess(newPoint: Point) =
            (right - newPoint.x) < Constants.MIN_SIZE

    companion object {
        private const val serialVersionUID = -8858760502335181962L
    }
}

class Rectangle(data: ShapeData = ShapeData(
        type = ShapeType.RECTANGLE
)) : Shape(data) {
    companion object {
        private const val serialVersionUID = -7689944340411885614L
    }
}

class Oval(data: ShapeData = ShapeData(
        type = ShapeType.OVAL
)) : Shape(data) {
    companion object {
        private const val serialVersionUID = -6753643919006825079L
    }
}

class Triangle(data: ShapeData = ShapeData(
        type = ShapeType.TRIANGLE
)) : Shape(data) {

    val bottomLeftVertex
        get() = Point(left, bottom)

    val bottomRightVertex
        get() = data.bottomRight

    val topVertex
        get() = Point(left + width / 2, top)

    companion object {
        private const val serialVersionUID = 7647815328629665556L
    }
}

class Picture(private val pictureData: PictureData) : Shape(ShapeData(
        type = ShapeType.PICTURE,
        bottomRight = Point(pictureData.width, pictureData.height))
) {

    val filename
        get() = pictureData.filename

    companion object {
        private const val serialVersionUID = -7893724938095347677L
    }
}