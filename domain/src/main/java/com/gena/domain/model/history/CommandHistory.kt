package com.gena.domain.model.history

import com.gena.domain.model.ObservableModel
import com.gena.domain.model.ShapeException
import com.gena.domain.model.ShapeExceptionError
import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class CommandHistory(
        val model: ShapesModel,
        val historyList: ArrayList<Command> = ArrayList(),
        initialHistoryPos: Int = -1
) : ObservableModel() {

    private var mHistoryPos = initialHistoryPos

    val canUndo
        get() = mHistoryPos in 0 until historyList.size

    val canRedo
        get() = mHistoryPos in -1 until historyList.size - 1

    @Throws(ShapeException::class)
    fun doCommand(command: Command, saveHistory: Boolean) {
        command.execute(model)
        if (saveHistory) {
            saveCommand(command)
        }
    }

    @Throws(ShapeException::class)
    fun saveCommand(command: Command) {
        // clear history after mHistoryPos
        var i = historyList.size - 1
        while (historyList.size > mHistoryPos + 1 && i >= 0) {
            historyList.removeAt(i)
            i--
        }
        // add command
        historyList.add(command)
        mHistoryPos = historyList.size - 1
        notifyChanged()
    }

    @Throws(ShapeException::class)
    fun undo() {
        if (canUndo) {
            historyList[mHistoryPos].unexecute(model)
            mHistoryPos--
            notifyChanged()
        } else {
            throw ShapeException(ShapeExceptionError.UNDO_NOT_AVAILABLE)
        }
    }

    @Throws(ShapeException::class)
    fun redo() {
        if (canRedo) {
            historyList[mHistoryPos + 1].execute(model)
            mHistoryPos++
            notifyChanged()
        } else {
            throw ShapeException(ShapeExceptionError.REDO_NOT_AVAILABLE)
        }
    }

}
