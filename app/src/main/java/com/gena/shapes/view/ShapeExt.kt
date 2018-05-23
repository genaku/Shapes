package com.gena.shapes.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.gena.shapes.model.*

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
fun UIShape.draw(canvas: Canvas) = when (this) {
    is UIOval -> draw(canvas)
    is UIRectangle -> draw(canvas)
    is UITriangle -> draw(canvas)
    is UIPicture -> draw(canvas)
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

fun UIPicture.draw(canvas: Canvas) {
    if (bitmap != null) {
        canvas.drawBitmap(bitmap, left, top, Paint(Paint.ANTI_ALIAS_FLAG))
    } else {
        canvas.drawRect(
                left,
                top,
                right,
                bottom,
                paint
        )
    }
}