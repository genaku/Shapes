package com.gena.presenters.interfaces

import com.gena.domain.model.ShapesModel

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IShapesViewModel {
    fun refreshShapes(shapes: ShapesModel)
}