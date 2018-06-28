package com.gena.presenters.interfaces

import com.gena.domain.consts.ShapeError

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IErrorViewModel {
    fun showError(error: ShapeError)
}