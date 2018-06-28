package com.gena.shapes.viewmodel

import android.arch.lifecycle.ViewModel
import com.gena.domain.consts.ShapeError
import com.gena.domain.usecases.interfaces.IErrorPresenter
import com.gena.presenters.ErrorPresenter
import com.gena.presenters.interfaces.IErrorViewModel
import com.gena.shapes.SingleLiveEvent

/**
 * Created by Gena Kuchergin on 02.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ErrorViewModel : ViewModel(), IErrorViewModel {

    // OBSERVABLES
    val errorEvent = SingleLiveEvent<ShapeError>()
    // OBSERVABLES

    val presenter: IErrorPresenter = ErrorPresenter(this)

    override fun showError(error: ShapeError) =
            errorEvent.postValue(error)

}