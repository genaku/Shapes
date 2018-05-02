package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.model.Constants
import java.io.Serializable

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
abstract class Shape(val data: ShapeData) : Serializable {

    abstract val numberOfPoints: Int

    val top
        get() = data.yValues.min() ?: 0

    val bottom
        get() = data.yValues.max() ?: 0

    val left
        get() = data.xValues.min() ?: 0

    val right
        get() = data.xValues.max() ?: 0

    val width
        get() = right - left

    val height
        get() = bottom - top

    fun getPointX(idx: Int): Int {
        return data.xValues[idx]
    }

    fun getPointY(idx: Int): Int {
        return data.yValues[idx]
    }

    protected fun moveX(x: Int): Boolean =
            move(x - left, data.xValues)

    protected fun moveY(y: Int): Boolean =
            move(y - top, data.yValues)

    private fun move(delta: Int, values: ArrayList<Int>): Boolean {
        if (delta == 0) {
            return false
        }
        for (i in 0 until numberOfPoints) {
            values[i] += delta
        }
        return true
    }

    protected fun shiftLeftSide(x: Int) =
            shiftSide(newPos = x, movingPos = left, fixedPos = right, values = data.xValues)

    protected fun shiftRightSide(x: Int) =
            shiftSide(newPos = x, movingPos = right, fixedPos = left, values = data.xValues)

    protected fun shiftTopSide(y: Int) =
            shiftSide(newPos = y, movingPos = top, fixedPos = bottom, values = data.yValues)

    protected fun shiftBottomSide(y: Int): Boolean =
            shiftSide(newPos = y, movingPos = bottom, fixedPos = top, values = data.yValues)

    private fun shiftSide(newPos: Int, movingPos: Int, fixedPos: Int, values: ArrayList<Int>): Boolean {
        val sign = if (movingPos > fixedPos) -1 else 1
        val newWidth = sign * (fixedPos - newPos)

        if (newWidth <= Constants.MIN_SIZE) {
            return false
        }

        val delta = sign * (newPos - movingPos)
        val oldLength = Math.abs(fixedPos - movingPos)
        val k = delta.toFloat() / oldLength
        for (i in 0 until numberOfPoints) {
            values[i] = values[i] + Math.round(k * (fixedPos - values[i]))
        }
        return true
    }

    fun moveShape(newX: Int, newY: Int, mode: ShapeMoveMode): Boolean {
        return when (mode) {
            ShapeMoveMode.BODY ->
                dualMove({ moveX(newX) }, { moveY(newY) })
            ShapeMoveMode.LEFT_UPPER_CORNER ->
                dualMove({ shiftLeftSide(newX) }, { shiftTopSide(newY) })
            ShapeMoveMode.LEFT_BOTTOM_CORNER ->
                dualMove({ shiftLeftSide(newX) }, { shiftBottomSide(newY) })
            ShapeMoveMode.RIGHT_UPPER_CORNER ->
                dualMove({ shiftRightSide(newX) }, { shiftTopSide(newY) })
            ShapeMoveMode.RIGHT_BOTTOM_CORNER ->
                dualMove({ shiftRightSide(newX) }, { shiftBottomSide(newY) })
            ShapeMoveMode.LEFT_SIDE ->
                shiftLeftSide(newX)
            ShapeMoveMode.RIGHT_SIDE ->
                shiftRightSide(newX)
            ShapeMoveMode.TOP_SIDE ->
                shiftTopSide(newY)
            ShapeMoveMode.BOTTOM_SIDE ->
                shiftBottomSide(newY)
            ShapeMoveMode.NOTHING ->
                false
        }
    }

    private fun dualMove(byX: () -> Boolean, byY: () -> Boolean): Boolean {
        val result1 = byX()
        val result2 = byY()
        return result1 || result2
    }

    companion object {
        private const val serialVersionUID = -8858760502335181962L
    }

}
