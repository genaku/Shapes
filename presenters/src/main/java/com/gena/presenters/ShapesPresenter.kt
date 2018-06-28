package com.gena.presenters

import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.interfaces.IShapesPresenter
import com.gena.presenters.interfaces.IShapesViewModel

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesPresenter(private val viewModel: IShapesViewModel) : IShapesPresenter {

    override fun refreshShapes(shapes: ShapesModel) =
            viewModel.refreshShapes(shapes)

}