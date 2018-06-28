package com.gena.domain.model.selector

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.extensions.EnumExt.enumFromInt
import com.gena.domain.model.Constants
import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.KeyData
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Point
import com.gena.domain.model.figures.Shape

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Selector {

    data class SelectedItemData(val key: KeyData, val mode: ShapeMoveMode)

    fun findSelected(model: ShapesModel, point: Point): SelectedItemData {
        val selectedIdx = model.selectedKey.idx
        for (idx in model.size - 1 downTo 0) {
            val shapeMoveMode = checkItemForSelectionToMove(
                    item = model.getItem(KeyData(idx)),
                    point = point,
                    selected = (idx == selectedIdx)
            )
            if (shapeMoveMode != ShapeMoveMode.NOTHING) {
                return SelectedItemData(KeyData(idx), shapeMoveMode)
            }
        }
        return SelectedItemData(NO_SELECTED, ShapeMoveMode.NOTHING)
    }

    private fun checkItemForSelectionToMove(item: Shape, point: Point, selected: Boolean): ShapeMoveMode =
            when {
                selected -> getShapeMoveModeForSelectedItem(item, point)
                item.contains(point) -> ShapeMoveMode.BODY
                else -> ShapeMoveMode.NOTHING
            }

    private fun getShapeMoveModeForSelectedItem(item: Shape, point: Point): ShapeMoveMode {
        // outer borders of selection field
        val left = item.left - Constants.BORDER_WIDTH
        val top = item.top - Constants.BORDER_WIDTH
        val right = item.right + Constants.BORDER_WIDTH
        val bottom = item.bottom + Constants.BORDER_WIDTH

        // out of selected item
        if (point.x !in left..right || point.y !in top..bottom) {
            return ShapeMoveMode.NOTHING
        }

        // check finger position
        var result = ShapeMoveMode.BODY.value
        if (point.x < item.left + Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.LEFT_SIDE.value   // left side selected
        }
        if (point.x > right - 2 * Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.RIGHT_SIDE.value  // right side selected
        }
        if (point.y < item.top + Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.TOP_SIDE.value    // top side selected
        }
        if (point.y > bottom - 2 * Constants.BORDER_WIDTH) {
            result += ShapeMoveMode.BOTTOM_SIDE.value // bottom side selected
        }
        return enumFromInt(result) ?: ShapeMoveMode.NOTHING
    }

}
