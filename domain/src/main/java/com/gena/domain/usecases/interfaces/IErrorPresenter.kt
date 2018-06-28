package com.gena.domain.usecases.interfaces

import com.gena.domain.consts.ShapeError

/**
 * Created by Gena Kuchergin on 02.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IErrorPresenter {
    fun showError(error: ShapeError)
}