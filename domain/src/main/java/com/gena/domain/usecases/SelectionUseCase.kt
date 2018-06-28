package com.gena.domain.usecases

import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.figures.Point
import com.gena.domain.model.history.CommandMove
import com.gena.domain.model.interfaces.ISelectionUseCase
import com.gena.domain.model.interfaces.ISelectorCommandsUseCase
import com.gena.domain.model.selector.Selector

/**
 * Created by Gena Kuchergin on 06.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class SelectionUseCase(
        private val selectorCommandsUseCase: ISelectorCommandsUseCase
) : ISelectionUseCase {

    private val mModel = selectorCommandsUseCase.getShapesModel()
    private val mSelector = Selector()

    private var mMoveMode = ShapeMoveMode.NOTHING
    private val mOldPoint = Point(0, 0)
    private val mCurrentPoint = Point(0, 0)
    private val mDelta = Point(0, 0)

    override fun moveSelectedTo(point: Point) {
        val key = mModel.selectedKey
        if (key == NO_SELECTED) // do only when selected
            return
        mCurrentPoint.x = point.x - mDelta.x
        mCurrentPoint.y = point.y - mDelta.y
        if (mCurrentPoint.x != mOldPoint.x && mCurrentPoint.y != mOldPoint.y) {
            selectorCommandsUseCase.execTempCommand(
                    CommandMove(key, mMoveMode, mOldPoint.x, mOldPoint.y, mCurrentPoint.x, mCurrentPoint.y))
        }
    }

    override fun movementFinished() {
        val key = mModel.selectedKey
        if (key == NO_SELECTED) // do only when selected
            return
        if (mCurrentPoint.x != mOldPoint.x && mCurrentPoint.y != mOldPoint.y) {
            selectorCommandsUseCase.saveCommandToHistory(
                    CommandMove(key, mMoveMode, mOldPoint.x, mOldPoint.y, mCurrentPoint.x, mCurrentPoint.y))
        }
    }

    override fun findSelection(point: Point) {
        val selected = mSelector.findSelected(mModel, point)
        val mode = selected.mode
        mModel.selectedKey = selected.key
        if (mode != ShapeMoveMode.NOTHING) {
            mMoveMode = mode
            val item = mModel.getSelectedItem() ?: return
            when (mode) {
                ShapeMoveMode.BODY, ShapeMoveMode.LEFT_UPPER_CORNER -> {
                    mOldPoint.x = item.left
                    mOldPoint.y = item.top
                }
                ShapeMoveMode.LEFT_BOTTOM_CORNER -> {
                    mOldPoint.x = item.left
                    mOldPoint.y = item.bottom
                }
                ShapeMoveMode.RIGHT_UPPER_CORNER -> {
                    mOldPoint.x = item.right
                    mOldPoint.y = item.top
                }
                ShapeMoveMode.RIGHT_BOTTOM_CORNER -> {
                    mOldPoint.x = item.right
                    mOldPoint.y = item.bottom
                }
                ShapeMoveMode.LEFT_SIDE -> {
                    mOldPoint.x = item.left
                    mOldPoint.y = item.top
                }
                ShapeMoveMode.RIGHT_SIDE -> {
                    mOldPoint.x = item.right
                    mOldPoint.y = item.top
                }
                ShapeMoveMode.TOP_SIDE -> {
                    mOldPoint.x = item.left
                    mOldPoint.y = item.top
                }
                ShapeMoveMode.BOTTOM_SIDE -> {
                    mOldPoint.x = item.left
                    mOldPoint.y = item.bottom
                }
                ShapeMoveMode.NOTHING -> {
                }
            }
            mDelta.x = point.x - mOldPoint.x
            mDelta.y = point.y - mOldPoint.y
        }
        selectorCommandsUseCase.invalidate()
    }

}