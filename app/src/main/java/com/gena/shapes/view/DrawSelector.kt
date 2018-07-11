package com.gena.shapes.view

import android.graphics.Canvas
import android.graphics.Paint

import com.gena.domain.model.Constants

class DrawSelector : IDrawSelector {

    override fun drawSelector(canvas: Canvas, left: Float, top: Float, right: Float,
                              bottom: Float, color: Int) {
        val radius = Constants.BORDER_WIDTH.toFloat()

        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = Constants.SELECTOR_WIDTH
        canvas.drawRect(left, top, right, bottom, paint)

        paint.style = Paint.Style.FILL
        canvas.drawCircle(left, top, radius, paint)
        canvas.drawCircle(right, top, radius, paint)
        canvas.drawCircle(left, bottom, radius, paint)
        canvas.drawCircle(right, bottom, radius, paint)
    }

}
