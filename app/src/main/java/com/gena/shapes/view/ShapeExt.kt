package com.gena.shapes.view

import android.graphics.Canvas
import android.graphics.RectF
import com.gena.shapes.model.UIOval
import com.gena.shapes.model.UIRectangle
import com.gena.shapes.model.UIShape
import com.gena.shapes.model.UITriangle

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
fun UIShape.draw(canvas: Canvas) = when (this) {
    is UIOval -> draw(canvas)
    is UIRectangle -> draw(canvas)
    is UITriangle -> draw(canvas)
}

fun UIOval.draw(canvas: Canvas) = canvas.drawOval(
        RectF(this.left,
                this.top,
                this.right,
                this.bottom),
        this.paint
)

fun UIRectangle.draw(canvas: Canvas) = canvas.drawRect(
        this.left,
        this.top,
        this.right,
        this.bottom,
        this.paint
)

fun UITriangle.draw(canvas: Canvas) = canvas.drawPath(
        this.path,
        this.paint
)
