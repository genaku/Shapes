package com.gena.domain

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
class TriangleTest {
    init {
        USpek.uspek("test triangle") {

            val shape = Triangle()

            "test not selected" o {
                shape.contains(-10, -10) eq false
                shape.contains(10, 10) eq false
            }

            "test selected" o {
                shape.contains(50, 50) eq true
            }
        }
    }
}