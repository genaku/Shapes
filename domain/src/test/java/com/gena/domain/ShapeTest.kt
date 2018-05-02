package com.gena.domain

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.figures.ShapeData
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpek.o
import pl.mareklangiewicz.uspek.USpek.uspek
import pl.mareklangiewicz.uspek.USpekJUnitRunner
import pl.mareklangiewicz.uspek.eq

/**
 * Created by Gena Kuchergin on 02.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
@RunWith(USpekJUnitRunner::class)
class ShapeTest {
    init {
        uspek("test basic shape") {

            val shape = TestShape()

            "test boundaries" o {
                shape.equal(0, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test move by x (inner)" o {
                shape.moveXTest(0) eq false
                shape.moveXTest(10) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 0, Constants.DEFAULT_SIZE)
            }

            "test move by y (inner)" o {
                shape.moveYTest(0) eq false
                shape.moveYTest(10) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 10, Constants.DEFAULT_SIZE + 10)
            }

            "test shift left side (inner)" o {
                shape.shiftLeftSideTest(10) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test shift right side (inner)" o {
                shape.shiftRightSideTest(90) eq true
                shape.equal(0, Constants.DEFAULT_SIZE - 10, 0, Constants.DEFAULT_SIZE)
            }

            "test shift top side (inner)" o {
                shape.shiftTopSideTest(10) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 10, Constants.DEFAULT_SIZE)
            }

            "test shift bottom side (inner)" o {
                shape.shiftBottomSideTest(90) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE - 10)
            }

            "test move by x to right" o {
                shape.moveShape(10, 0, ShapeMoveMode.BODY) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 0, Constants.DEFAULT_SIZE)
            }

            "test move by x to left" o {
                shape.moveShape(-10, 0, ShapeMoveMode.BODY) eq true
                shape.equal(-10, Constants.DEFAULT_SIZE - 10, 0, Constants.DEFAULT_SIZE)
            }

            "test move by y down" o {
                shape.moveShape(0, 10, ShapeMoveMode.BODY) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 10, Constants.DEFAULT_SIZE + 10)
            }

            "test move by y up" o {
                shape.moveShape(0, -10, ShapeMoveMode.BODY) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, -10, Constants.DEFAULT_SIZE - 10)
            }

            "test move by x and y right down" o {
                shape.moveShape(10, 15, ShapeMoveMode.BODY) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 15, Constants.DEFAULT_SIZE + 15)
            }

            "test shift left side" o {
                shape.moveShape(10, 15, ShapeMoveMode.LEFT_SIDE) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test shift right side" o {
                shape.moveShape(90, 15, ShapeMoveMode.RIGHT_SIDE) eq true
                shape.equal(0, 90, 0, Constants.DEFAULT_SIZE)
            }

            "test shift top side" o {
                shape.moveShape(10, 15, ShapeMoveMode.TOP_SIDE) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 15, Constants.DEFAULT_SIZE)
            }

            "test shift bottom side" o {
                shape.moveShape(90, 85, ShapeMoveMode.BOTTOM_SIDE) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 0, 85)
            }

            "test move shift left upper corner" o {
                shape.moveShape(10, 15, ShapeMoveMode.LEFT_UPPER_CORNER) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 15, Constants.DEFAULT_SIZE)
            }

            "test move shift right upper corner" o {
                shape.moveShape(90, 15, ShapeMoveMode.RIGHT_UPPER_CORNER) eq true
                shape.equal(0, 90, 15, Constants.DEFAULT_SIZE)
            }

            "test move shift left bottom corner" o {
                shape.moveShape(10, 85, ShapeMoveMode.LEFT_BOTTOM_CORNER) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 0, 85)
            }

            "test move shift right bottom corner" o {
                shape.moveShape(90, 85, ShapeMoveMode.RIGHT_BOTTOM_CORNER) eq true
                shape.equal(0, 90, 0, 85)
            }
        }

    }

    class TestShape : Shape(ShapeData(
            type = ShapeType.RECTANGLE,
            xValues = ArrayList(COORDS),
            yValues = ArrayList(COORDS)
    )) {
        override val numberOfPoints: Int = 2

        fun moveXTest(x: Int) = moveX(x)
        fun moveYTest(y: Int) = moveY(y)
        fun shiftLeftSideTest(x: Int) = shiftLeftSide(x)
        fun shiftRightSideTest(x: Int) = shiftRightSide(x)
        fun shiftTopSideTest(y: Int) = shiftTopSide(y)
        fun shiftBottomSideTest(y: Int) = shiftBottomSide(y)

        companion object {
            private val COORDS = arrayListOf(0, Constants.DEFAULT_SIZE)
        }
    }

    private fun TestShape.equal(left: Int, right: Int, top: Int, bottom: Int) {
        this.left eq left
        this.right eq right
        this.top eq top
        this.bottom eq bottom
    }

}