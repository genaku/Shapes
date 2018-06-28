package com.gena.domain

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.model.Constants
import com.gena.domain.model.figures.Point
import com.gena.domain.model.figures.Triangle
import com.gena.domain.model.selector.contains
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpek
import pl.mareklangiewicz.uspek.USpek.o
import pl.mareklangiewicz.uspek.USpekJUnitRunner
import pl.mareklangiewicz.uspek.eq

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
@RunWith(USpekJUnitRunner::class)
class TestSelectTriangle {
    init {
        USpek.uspek("test triangle") {

            val shape = Triangle()

            "test not selected" o {
                shape.contains(Point(-10, -10)) eq false
                shape.contains(Point(10, 10)) eq false
            }

            "test selected" o {
                shape.contains(Point(50, 50)) eq true
            }

            "test move by x to right" o {
                shape.moveShape(Point(10, 0), ShapeMoveMode.BODY) eq true
                shape.equal(10, Constants.DEFAULT_SIZE + 10, 0, Constants.DEFAULT_SIZE, 60, 0)
            }

            "test move by x to right 2" o {
                shape.moveShape(Point(100, 0), ShapeMoveMode.BODY) eq true
                shape.equal(100, Constants.DEFAULT_SIZE + 100, 0, Constants.DEFAULT_SIZE, 150, 0)
            }
        }
    }

    private fun Triangle.equal(left: Int, right: Int, top: Int, bottom: Int, topX: Int, topY: Int) {
        this.left eq left
        this.right eq right
        this.top eq top
        this.bottom eq bottom
        this.topVertex.x eq topX
        this.topVertex.y eq topY
    }
}