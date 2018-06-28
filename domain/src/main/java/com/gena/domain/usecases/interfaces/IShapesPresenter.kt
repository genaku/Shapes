package com.gena.domain.usecases.interfaces

import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IShapesPresenter {
    fun refreshShapes(shapes: ShapesModel)
}