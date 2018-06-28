package com.gena.domain.model.interfaces

import com.gena.domain.model.ShapesModel
import com.gena.domain.model.history.Command

interface ISelectorCommandsUseCase {
    fun getShapesModel(): ShapesModel
    fun execTempCommand(command: Command)
    fun saveCommandToHistory(command: Command)
    fun invalidate()
}