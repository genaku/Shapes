package com.gena.domain

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Oval
import com.gena.domain.model.figures.Rectangle
import com.gena.domain.model.figures.Triangle
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
class ShapesModelTest {
    init {
        USpek.uspek("test rectangle") {

            val model = ShapesModel()

            "test add rectangle" o {
                model.size eq 0
                model.add(ShapeType.RECTANGLE, 0, 0)
                model.size eq 1
                (model.getItem(0) is Rectangle) eq true
            }

            "test add triangle" o {
                model.size eq 0
                model.add(ShapeType.TRIANGLE, 0, 0)
                model.size eq 1
                (model.getItem(0) is Triangle) eq true
            }

            "test insert shape" o {
                model.size eq 0
                model.add(ShapeType.TRIANGLE, 0, 0)
                model.add(ShapeType.RECTANGLE, 0, 0)
                val oval = Oval()
                model.insert(oval, 1)
                (model.getItem(0) is Triangle) eq true
                (model.getItem(1) is Oval) eq true
                (model.getItem(2) is Rectangle) eq true
            }

            "test delete shape" o {
                model.size eq 0
                model.add(ShapeType.TRIANGLE, 0, 0)
                model.size eq 1
                (model.getItem(0) is Triangle) eq true
                model.delete(0)
                model.size eq 0
            }

            "test shape move" o {
                model.add(ShapeType.RECTANGLE, 0, 0)
                model.move(0, 10, 15, ShapeMoveMode.BODY)
                val shape = model.getItem(0)
                shape.left eq 10
                shape.top eq 15
            }

        }
    }
}