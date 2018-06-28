package com.gena.domain

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.model.Constants
import com.gena.domain.model.figures.Point
import com.gena.domain.model.figures.Rectangle
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpek
import pl.mareklangiewicz.uspek.USpek.o
import pl.mareklangiewicz.uspek.USpekJUnitRunner
import pl.mareklangiewicz.uspek.eq

/**
 * Created by Gena Kuchergin on 02.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
@RunWith(USpekJUnitRunner::class)
class ShapeTest {
    init {
        USpek.uspek("test basic shape") {

            val shape = Rectangle()

            "test boundaries" o {
                shape.equal(0, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test move by x to right" o {
                shape.moveShape(Point(10, 0), ShapeMoveMode.BODY) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 0, Constants.DEFAULT_SIZE)
            }

            "test move by x to left" o {
                shape.moveShape(Point(-10, 0), ShapeMoveMode.BODY) eq true
                shape.equal(-10, Constants.DEFAULT_SIZE - 10, 0, Constants.DEFAULT_SIZE)
            }

            "test move by y down" o {
                shape.moveShape(Point(0, 10), ShapeMoveMode.BODY) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 10, Constants.DEFAULT_SIZE + 10)
            }

            "test move by y up" o {
                shape.moveShape(Point(0, -10), ShapeMoveMode.BODY) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, -10, Constants.DEFAULT_SIZE - 10)
            }

            "test move by x and y right down" o {
                shape.moveShape(Point(10, 15), ShapeMoveMode.BODY) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 15, Constants.DEFAULT_SIZE + 15)
            }

            "test shift left side" o {
                shape.moveShape(Point(10, 15), ShapeMoveMode.LEFT_SIDE) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test shift right side" o {
                shape.moveShape(Point(90, 15), ShapeMoveMode.RIGHT_SIDE) eq true
                shape.equal(0, 90, 0, Constants.DEFAULT_SIZE)
            }

            "test shift top side" o {
                shape.moveShape(Point(10, 15), ShapeMoveMode.TOP_SIDE) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 15, Constants.DEFAULT_SIZE)
            }

            "test shift bottom side" o {
                shape.moveShape(Point(90, 85), ShapeMoveMode.BOTTOM_SIDE) eq true
                shape.equal(0, Constants.DEFAULT_SIZE, 0, 85)
            }

            "test move shift left upper corner" o {
                shape.moveShape(Point(10, 15), ShapeMoveMode.LEFT_UPPER_CORNER) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 15, Constants.DEFAULT_SIZE)
            }

            "test move shift right upper corner" o {
                shape.moveShape(Point(90, 15), ShapeMoveMode.RIGHT_UPPER_CORNER) eq true
                shape.equal(0, 90, 15, Constants.DEFAULT_SIZE)
            }

            "test move shift left bottom corner" o {
                shape.moveShape(Point(10, 85), ShapeMoveMode.LEFT_BOTTOM_CORNER) eq true
                shape.equal(10, Constants.DEFAULT_SIZE, 0, 85)
            }

            "test move shift right bottom corner" o {
                shape.moveShape(Point(90, 85), ShapeMoveMode.RIGHT_BOTTOM_CORNER) eq true
                shape.equal(0, 90, 0, 85)
            }

            "test shift left side to min" o {
                shape.moveShape(Point(EXTREME_SIZE_SHIFT, 15), ShapeMoveMode.LEFT_SIDE) eq false
                shape.equal(0, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

            "test shift right side to min" o {
                shape.moveShape(Point(Constants.DEFAULT_SIZE - EXTREME_SIZE_SHIFT, 15), ShapeMoveMode.RIGHT_SIDE) eq false
                shape.equal(0, Constants.DEFAULT_SIZE, 0, Constants.DEFAULT_SIZE)
            }

        }

    }

    private fun Rectangle.equal(left: Int, right: Int, top: Int, bottom: Int) {
        this.left eq left
        this.right eq right
        this.top eq top
        this.bottom eq bottom
    }

    companion object {
        private const val EXTREME_SIZE_SHIFT = Constants.DEFAULT_SIZE - Constants.MIN_SIZE + 1
    }
}