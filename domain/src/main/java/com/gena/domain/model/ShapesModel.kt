package com.gena.domain.model

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.figures.*
import java.util.*

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesModel(
        private val shapes: ArrayList<Shape> = ArrayList(),
        var selectedKey: KeyData = NO_SELECTED
) : ObservableModel() {

    val size: Int
        get() = shapes.size

    val items
        get() = shapes

    @Throws(ShapeException::class)
    fun add(type: ShapeType): KeyData =
            addShapeToModel(ShapeFactory.createShape(type))

    @Throws(ShapeException::class)
    fun addPicture(pictureData: PictureData): KeyData =
            addShapeToModel(Picture(pictureData))

    @Throws(ShapeException::class)
    private fun addShapeToModel(shape: Shape): KeyData {
        shape.moveShape(
                newPoint = Point(-shape.width / 2, -shape.height / 2),
                mode = ShapeMoveMode.BODY
        )
        shapes.add(shape)
        selectedKey = KeyData(shapes.indexOf(shape))
        notifyChanged()
        return selectedKey
    }

    @Throws(ShapeException::class)
    fun getItem(key: KeyData): Shape {
        checkKey(key)
        return shapes[key.idx]
    }

    @Throws(ShapeException::class)
    private fun checkKey(key: KeyData) {
        if (key.idx !in 0 until shapes.size) {
            throw ShapeException(ShapeExceptionError.INDEX_OUT_OF_MODEL_LIST)
        }
    }

    @Throws(ShapeException::class)
    fun insert(shape: Shape, key: KeyData) {
        selectedKey = if (key.idx in 0 until shapes.size) {
            shapes.add(key.idx, shape)
            key
        } else {
            shapes.add(shape)
            KeyData(shapes.indexOf(shape))
        }
        notifyChanged()
    }

    @Throws(ShapeException::class)
    fun delete(key: KeyData) {
        checkKey(key)
        if (selectedKey == key)
            selectedKey = NO_SELECTED
        shapes.removeAt(key.idx)
        notifyChanged()
    }

    @Throws(ShapeException::class)
    fun move(key: KeyData, newX: Int, newY: Int, mode: ShapeMoveMode) {
        if (getItem(key).moveShape(Point(newX, newY), mode))
            notifyChanged()
    }

    @Throws(ShapeException::class)
    fun getSelectedItem(): Shape? {
        return try {
            getItem(selectedKey)
        } catch (e: Exception) {
            null
        }
    }

    fun setSelected(key: KeyData) {
        selectedKey = key
        notifyChanged()
    }

}