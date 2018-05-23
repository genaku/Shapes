package com.gena.domain.model.selector

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.extensions.EnumExt.enumFromInt
import com.gena.domain.model.Constants
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Shape

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Selector {

    data class SelectedItemData(val idx: Int, val mode: ShapeMoveMode)

    fun findSelected(model: ShapesModel, x: Int, y: Int): SelectedItemData {
        val selectedIdx = model.selectedIdx
        for (idx in model.size - 1 downTo 0) {
            val shapeMoveMode = checkItemForSelectionToMove(
                    item = model.getItem(idx),
                    x = x,
                    y = y,
                    selected = (idx == selectedIdx)
            )
            if (shapeMoveMode != ShapeMoveMode.NOTHING) {
                return SelectedItemData(idx, shapeMoveMode)
            }
        }
        return SelectedItemData(-1, ShapeMoveMode.NOTHING)
    }

    private fun checkItemForSelectionToMove(item: Shape, x: Int, y: Int, selected: Boolean): ShapeMoveMode =
            when {
                selected -> getShapeMoveModeForSelectedItem(item, x, y)
                item.contains(x, y) -> ShapeMoveMode.BODY
                else -> ShapeMoveMode.NOTHING
            }

    private fun getShapeMoveModeForSelectedItem(item: Shape, x: Int, y: Int): ShapeMoveMode {
        // outer borders of selection field
        val left = item.left - Constants.BORDER_WIDTH
        val top = item.top - Constants.BORDER_WIDTH
        val right = item.right + Constants.BORDER_WIDTH
        val bottom = item.bottom + Constants.BORDER_WIDTH

        // out of selected item
        if (x !in left..right || y !in top..bottom) {
            return ShapeMoveMode.NOTHING
        }

        // check finger position
        var result = ShapeMoveMode.BODY.value
        if (x < item.left + Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.LEFT_SIDE.value   // left side selected
        }
        if (x > right - 2 * Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.RIGHT_SIDE.value  // right side selected
        }
        if (y < item.top + Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.TOP_SIDE.value    // top side selected
        }
        if (y > bottom - 2 * Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.BOTTOM_SIDE.value // bottom side selected
        }
        return enumFromInt(result) ?: ShapeMoveMode.NOTHING
    }

}
