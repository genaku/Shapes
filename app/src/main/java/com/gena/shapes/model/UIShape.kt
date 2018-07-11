package com.gena.shapes.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Path
import com.gena.domain.model.figures.*
import com.gena.shapes.view.ImageCache

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
        moveTo(centerX + triangle.bottomLeftVertex.x, centerY + triangle.bottomLeftVertex.y)
        lineTo(centerX + triangle.topVertex.x, centerY + triangle.topVertex.y)
        lineTo(centerX + triangle.bottomRightVertex.x, centerY + triangle.bottomRightVertex.y)
        close()
    }
}

class UIPicture(
        private val picture: Picture,
        centerX: Float,
        centerY: Float,
        color: Int
) : UIShape(picture, centerX, centerY, color) {

    val key = picture.filename.hashCode()

    val bitmap = loadBitmap()

    fun onDelete() {
        ImageCache.deleteFromCache(key)
    }

    private fun loadBitmap(): Bitmap? {
        var bitmap = ImageCache.getFromCache(key, picture.width, picture.height)
        if (bitmap == null) {
            bitmap = tryToCreateBmp()
            if (bitmap != null) {
                ImageCache.addToCache(key, bitmap)
            }
        }
        return bitmap
    }

    private fun tryToCreateBmp(): Bitmap? {
        if (picture.filename.isBlank())
            return null
        try {
            val bmp = BitmapFactory.decodeFile(picture.filename) ?: return null
            return Bitmap.createScaledBitmap(bmp, picture.width, picture.height, true)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}

