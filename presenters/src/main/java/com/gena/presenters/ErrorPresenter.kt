package com.gena.presenters

import com.gena.domain.consts.ShapeError
import com.gena.domain.usecases.interfaces.IErrorPresenter
import com.gena.presenters.interfaces.IErrorViewModel

/**
 * Created by Fenix on 02.06.2018.
 * © 2018 Fenix. All Rights Reserved.
 */
class ErrorPresenter(private val viewModel: IErrorViewModel) : IErrorPresenter {
    override fun showError(error: ShapeError) =
            viewModel.showError(error)
}