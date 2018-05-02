package com.gena.shapes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.UseCase
import com.gena.domain.usecases.interfaces.IRepository
import com.gena.interactors.Interactor
import com.gena.presenters.Presenter
import com.gena.presenters.interfaces.IShapesViewModel

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesViewModel(app: Application, repository: IRepository) : IShapesViewModel, AndroidViewModel(app) {

    // OBSERVABLES
    val actionsAvailability = MutableLiveData<ArrayMap<MenuCommand, Boolean>>()
    val shapesToRefresh = MutableLiveData<ShapesModel>()
    // OBSERVABLES

    val interactor: Interactor

    init {
        val presenter = Presenter(this)
        val useCase = UseCase(presenter, repository)
        interactor = Interactor(useCase)
    }

    override fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>) =
            actionsAvailability.postValue(value)

    override fun refreshShapes(shapes: ShapesModel) =
            shapesToRefresh.postValue(shapes)

}