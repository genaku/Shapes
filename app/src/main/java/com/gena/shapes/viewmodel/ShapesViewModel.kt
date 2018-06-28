package com.gena.shapes.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.SelectionUseCase
import com.gena.domain.usecases.ShapesHistoryUseCase
import com.gena.domain.usecases.interfaces.*
import com.gena.interactors.SelectionInteractor
import com.gena.interactors.ShapesHistoryInteractor
import com.gena.presenters.ShapesPresenter
import com.gena.presenters.interfaces.IShapesViewModel

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesViewModel(repository: IRepository, menuPresenter: IMenuPresenter, errorPresenter: IErrorPresenter) : ViewModel(), IShapesViewModel {

    // OBSERVABLES
    val shapesToRefresh = MutableLiveData<ShapesModel>()
    // OBSERVABLES

    val shapesHistoryInteractor: IShapesHistoryInteractor
    val selectionInteractor: ISelectionInteractor

    init {
        val presenter = ShapesPresenter(this)
        val shapesHistoryUseCase = ShapesHistoryUseCase(
                repository = repository,
                shapesPresenter = presenter,
                menuPresenter = menuPresenter,
                errorPresenter = errorPresenter
        )
        shapesHistoryInteractor = ShapesHistoryInteractor(shapesHistoryUseCase)
        val selectionUseCase = SelectionUseCase(shapesHistoryUseCase)
        selectionInteractor = SelectionInteractor(selectionUseCase)
    }

    override fun refreshShapes(shapes: ShapesModel) =
            shapesToRefresh.postValue(shapes)

}