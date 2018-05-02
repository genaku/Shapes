package com.gena.shapes.model

import android.graphics.Paint
import android.graphics.Path
import com.gena.domain.model.figures.Oval
import com.gena.domain.model.figures.Rectangle
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.figures.Triangle

/**
 * Created by Gena Kuchergin on 02.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
sealed class UIShape(
        shape: Shape,
        centerX: Float,
        centerY: Float,
        color: Int
) {

    val left = centerX + shape.left
    val top = centerY + shape.top
    val right = centerX + shape.right
    val bottom = centerY + shape.bottom

    val paint = Paint().apply {
        strokeWidth = 2f
        this.color = color
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
    }

}

class UIRectangle(
        rectangle: Rectangle,
        centerX: Float,
        centerY: Float,
        color: Int
) : UIShape(rectangle, centerX, centerY, color)

class UIOval(
        oval: Oval,
        centerX: Float,
        centerY: Float,
        color: Int
) : UIShape(oval, centerX, centerY, color)

class UITriangle(
        triangle: Triangle,
        centerX: Float,
        centerY: Float,
        color: Int
) : UIShape(triangle, centerX, centerY, color) {

    val path = Path().apply {
        fillType = Path.FillType.EVEN_ODD
        moveTo(centerX + triangle.getPointX(0), centerY + triangle.getPointY(0))
        lineTo(centerX + triangle.getPointX(1), centerY + triangle.getPointY(1))
        lineTo(centerX + triangle.getPointX(2), centerY + triangle.getPointY(2))
        close()
    }

}