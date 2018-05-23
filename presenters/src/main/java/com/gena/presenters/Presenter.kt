package com.gena.presenters

import android.support.v4.util.ArrayMap
import com.gena.domain.consts.ShapeError
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.interfaces.IPresenter
import com.gena.presenters.interfaces.IShapesViewModel

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Presenter(private val viewModel: IShapesViewModel) : IPresenter {

    override fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>) =
            viewModel.showMenuCommands(value)

    override fun refreshShapes(shapes: ShapesModel) =
            viewModel.refreshShapes(shapes)

    override fun showError(error: ShapeError) =
            viewModel.showError(error)

}