package com.gena.shapes.view

import android.graphics.Canvas

interface IDrawSelector {
    fun drawSelector(canvas: Canvas, left: Float, top: Float, right: Float,
                     bottom: Float, color: Int)
}
