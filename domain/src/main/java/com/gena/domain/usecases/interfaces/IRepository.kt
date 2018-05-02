package com.gena.domain.usecases.interfaces

import com.gena.domain.model.ShapesModel
import com.gena.domain.model.history.CommandHistory

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IRepository {
    val history: CommandHistory
    fun saveModel(model: ShapesModel)
    fun loadModel(): ShapesModel
}