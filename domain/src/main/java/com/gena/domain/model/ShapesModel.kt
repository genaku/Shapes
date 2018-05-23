package com.gena.domain.model

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.figures.Picture
import com.gena.domain.model.figures.PictureData
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.figures.ShapeFactory
import java.util.*

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesModel(
        private val shapes: ArrayList<Shape> = ArrayList(),
        var selectedIdx: Int = -1
) : ObservableModel() {

    val size: Int
        get() = shapes.size

    val items
        get() = shapes

    @Throws(ShapeException::class)
    fun add(type: ShapeType, x: Int, y: Int): Int {
        val shape = ShapeFactory.getShape(type)
        shape.moveShape(
                newX = x - shape.width / 2,
                newY = y - shape.height / 2,
                mode = ShapeMoveMode.BODY
        )
        shapes.add(shape)
        selectedIdx = shapes.indexOf(shape)
        notifyChanged()
        return selectedIdx
    }

    @Throws(ShapeException::class)
    fun addPicture(x: Int, y: Int, pictureData: PictureData): Int {
        val shape = Picture(pictureData)
        shape.moveShape(
                newX = x - shape.width / 2,
                newY = y - shape.height / 2,
                mode = ShapeMoveMode.BODY
        )
        shapes.add(shape)
        selectedIdx = shapes.indexOf(shape)
        notifyChanged()
        return selectedIdx
    }

    @Throws(ShapeException::class)
    fun getItem(idx: Int): Shape {
        checkIndex(idx)
        return shapes[idx]
    }

    @Throws(ShapeException::class)
    private fun checkIndex(idx: Int) {
        if (idx < 0 || idx >= shapes.size)
            throw ShapeException(ShapeExceptionError.INDEX_OUT_OF_MODEL_LIST)
    }

    @Throws(ShapeException::class)
    fun insert(shape: Shape, index: Int) {
        if (index == shapes.size) {
            shapes.add(shape)
            selectedIdx = shapes.indexOf(shape)
        } else {
            checkIndex(index)
            shapes.add(index, shape)
        }
        selectedIdx = index
        notifyChanged()
    }

    @Throws(ShapeException::class)
    fun delete(idx: Int) {
        checkIndex(idx)
        if (selectedIdx == idx)
            selectedIdx = -1
        shapes.removeAt(idx)
        notifyChanged()
    }

    @Throws(ShapeException::class)
    fun move(idx: Int, newX: Int, newY: Int, mode: ShapeMoveMode) {
        checkIndex(idx)
        if (shapes[idx].moveShape(newX, newY, mode))
            notifyChanged()
    }

    @Throws(ShapeException::class)
    fun getSelectedItem(): Shape =
            getItem(selectedIdx)

    fun setSelected(idx: Int) {
        selectedIdx = idx
        notifyChanged()
    }

}