package com.gena.domain.usecases

import android.support.v4.util.ArrayMap
import com.gena.domain.consts.ShapeError
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants.Companion.NO_SELECTED
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.history.*
import com.gena.domain.model.interfaces.ISelectorCommandsUseCase
import com.gena.domain.model.interfaces.IShapesHistoryUseCase
import com.gena.domain.usecases.interfaces.IErrorPresenter
import com.gena.domain.usecases.interfaces.IMenuPresenter
import com.gena.domain.usecases.interfaces.IRepository
import com.gena.domain.usecases.interfaces.IShapesPresenter
import java.util.*

/**
 * Created by Gena Kuchergin on 12.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesHistoryUseCase(
        private val repository: IRepository,
        private val shapesPresenter: IShapesPresenter,
        private val menuPresenter: IMenuPresenter,
        private val errorPresenter: IErrorPresenter
) : IShapesHistoryUseCase, ISelectorCommandsUseCase, Observer {

    private val mMenuCommands = ArrayMap<MenuCommand, Boolean>()
    private var mModel: ShapesModel
    private var mHistory: CommandHistory

    init {
        MenuCommand.values().forEach {
            mMenuCommands[it] = true
        }
        mModel = repository.loadModel()
        mHistory = repository.history
        invalidate()
    }

    private fun refreshShapes() =
            shapesPresenter.refreshShapes(mModel)

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
        mMenuCommands[MenuCommand.DELETE] = mModel.selectedKey != NO_SELECTED
        menuPresenter.showMenuCommands(mMenuCommands)
    }

    override fun startObserving() {
        mModel.addObserver(this)
        mHistory.addObserver(this)
    }

    override fun stopObserving() {
        mModel.deleteObserver(this)
        mHistory.deleteObserver(this)
    }

    override fun addShape(type: ShapeType) = tryExecCommand({
        mHistory.doCommand(CommandCreate(type), true)
    })

    override fun execTempCommand(command: Command) {
        tryExecCommand({
            mHistory.doCommand(command, false)
        })
    }

    override fun saveCommandToHistory(command: Command) {
        tryExecCommand(
                execCommand = {
                    command.setExecuted(true)
                    mHistory.saveCommand(command)
                },
                error = ShapeError.CANT_SAVE_TO_HISTORY
        )
    }

    private fun tryExecCommand(execCommand: () -> Unit, error: ShapeError = ShapeError.CANT_EXEC_COMMAND) = try {
        execCommand()
    } catch (e: Exception) {
        errorPresenter.showError(error)
    }

    override fun addPicture(filename: String) = tryExecCommand({
        val data = repository.loadPictureIntoRepository(filename)
        mHistory.doCommand(CommandCreatePicture(data), true)
    })

    override fun deleteSelected() = tryExecCommand({
        mHistory.doCommand(CommandDelete(mModel.selectedKey), true)
    })

    override fun undo() = tryExecCommand(
            execCommand = { mHistory.undo() },
            error = ShapeError.CANT_UNDO
    )

    override fun redo() = tryExecCommand(
            execCommand = { mHistory.redo() },
            error = ShapeError.CANT_REDO
    )

    override fun saveShapes() {
        repository.saveModel(mModel)
    }

    override fun getShapesModel(): ShapesModel {
        return mModel
    }

    override fun invalidate() {
        refreshShapes()
        updateMenuCommandsAvailability()
    }

}