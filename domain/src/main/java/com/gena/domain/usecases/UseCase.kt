package com.gena.domain.usecases

import android.support.v4.util.ArrayMap
import com.gena.domain.consts.ShapeError
import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.history.*
import com.gena.domain.model.interfaces.IUseCase
import com.gena.domain.model.selector.Selector
import com.gena.domain.usecases.interfaces.IPresenter
import com.gena.domain.usecases.interfaces.IRepository
import java.util.*

/**
 * Created by Gena Kuchergin on 12.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class UseCase(
        private val presenter: IPresenter,
        private val repository: IRepository
) : IUseCase, Observer {

    private val mMenuCommands = ArrayMap<MenuCommand, Boolean>()
    private var mModel: ShapesModel
    private var mHistory: CommandHistory
    private val mSelector = Selector()

    private var mMoveMode = ShapeMoveMode.NOTHING
    private var xOld = 0
    private var yOld = 0
    private var xCurrent = 0
    private var yCurrent = 0
    private var dX = 0
    private var dY = 0

    init {
        MenuCommand.values().forEach {
            mMenuCommands[it] = true
        }
        mModel = repository.loadModel()
        mHistory = repository.history
        refreshShapes()
        updateMenuCommandsAvailability()
    }

    private fun refreshShapes() =
            presenter.refreshShapes(mModel)

    override fun update(observable: Observable?, arg: Any?) {
        when (observable) {
            is ShapesModel -> {
                refreshShapes()
            }
            is CommandHistory -> {
                updateMenuCommandsAvailability()
            }
        }
    }

    private fun updateMenuCommandsAvailability() {
        mMenuCommands[MenuCommand.REDO] = mHistory.canRedo
        mMenuCommands[MenuCommand.UNDO] = mHistory.canUndo
        mMenuCommands[MenuCommand.DELETE] = mModel.selectedIdx >= 0
        presenter.showMenuCommands(mMenuCommands)
    }

    override fun startObserving() {
        mModel.addObserver(this)
        mHistory.addObserver(this)
    }

    override fun stopObserving() {
        mModel.deleteObserver(this)
        mHistory.deleteObserver(this)
    }

    override fun addShape(type: ShapeType, x0: Int, y0: Int) = tryExecCommand({
        mHistory.doCommand(CommandCreate(type, x0, y0), true)
    })

    private fun tryExecCommand(execCommand: () -> Unit, error: ShapeError = ShapeError.CANT_EXEC_COMMAND) = try {
        execCommand()
    } catch (e: Exception) {
        presenter.showError(error)
    }

    override fun addPicture(x0: Int, y0: Int, filename: String) = tryExecCommand({
        val data = repository.loadPictureIntoRepository(filename)
        mHistory.doCommand(CommandCreatePicture(x0, y0, data), true)
    })

    override fun deleteSelected() = tryExecCommand({
        mHistory.doCommand(CommandDelete(mModel.selectedIdx), true)
    })

    override fun undo() = tryExecCommand(
            execCommand = { mHistory.undo() },
            error = ShapeError.CANT_UNDO
    )

    override fun redo() = tryExecCommand(
            execCommand = { mHistory.redo() },
            error = ShapeError.CANT_REDO
    )

    override fun saveModel() {
        repository.saveModel(mModel)
    }

    override fun moveSelected(x: Int, y: Int) {
        val idx = mModel.selectedIdx
        // do only when selected
        if (idx < 0)
            return
        xCurrent = x - dX
        yCurrent = y - dY
        if (xCurrent != xOld && yCurrent != yOld) {
            tryExecCommand({
                mHistory.doCommand(CommandMove(idx, mMoveMode, xOld, yOld,
                        xCurrent, yCurrent), false)
            })
        }
    }

    override fun movementFinished() {
        val idx = mModel.selectedIdx
        // do only when selected
        if (idx < 0)
            return
        if (xCurrent != xOld && yCurrent != yOld) {
            val command = CommandMove(idx, mMoveMode, xOld, yOld,
                    xCurrent, yCurrent)
            command.setExecuted(true)
            tryExecCommand(
                    execCommand = { mHistory.saveCommand(command) },
                    error = ShapeError.CANT_SAVE_TO_HISTORY
            )
        }
    }

    override fun findSelection(x: Int, y: Int) {
        val selected = mSelector.findSelected(mModel, x, y)
        val mode = selected.mode
        mModel.selectedIdx = selected.idx
        if (mode != ShapeMoveMode.NOTHING) {
            mMoveMode = mode
            val item = mModel.getSelectedItem()
            when (mode) {
                ShapeMoveMode.BODY, ShapeMoveMode.LEFT_UPPER_CORNER -> {
                    xOld = item.left
                    yOld = item.top
                }
                ShapeMoveMode.LEFT_BOTTOM_CORNER -> {
                    xOld = item.left
                    yOld = item.bottom
                }
                ShapeMoveMode.RIGHT_UPPER_CORNER -> {
                    xOld = item.right
                    yOld = item.top
                }
                ShapeMoveMode.RIGHT_BOTTOM_CORNER -> {
                    xOld = item.right
                    yOld = item.bottom
                }
                ShapeMoveMode.LEFT_SIDE -> {
                    xOld = item.left
                    yOld = item.top
                }
                ShapeMoveMode.RIGHT_SIDE -> {
                    xOld = item.right
                    yOld = item.top
                }
                ShapeMoveMode.TOP_SIDE -> {
                    xOld = item.left
                    yOld = item.top
                }
                ShapeMoveMode.BOTTOM_SIDE -> {
                    xOld = item.left
                    yOld = item.bottom
                }
                ShapeMoveMode.NOTHING -> {
                }
            }
            dX = x - xOld
            dY = y - yOld
        }
        refreshShapes()
        updateMenuCommandsAvailability()
    }

}