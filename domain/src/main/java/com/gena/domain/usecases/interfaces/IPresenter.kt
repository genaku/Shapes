package com.gena.domain.usecases.interfaces

import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel

/**
 * Created by Fenix on 29.04.2018.
 * © 2018 Fenix. All Rights Reserved.
 */
interface IPresenter {
    fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>)
    fun refreshShapes(shapes: ShapesModel)
}