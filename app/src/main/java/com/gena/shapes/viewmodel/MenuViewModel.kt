package com.gena.shapes.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand
import com.gena.domain.usecases.interfaces.IMenuPresenter
import com.gena.presenters.MenuPresenter
import com.gena.presenters.interfaces.IMenuViewModel

class MenuViewModel : ViewModel(), IMenuViewModel {

    // OBSERVABLES
    val actionsAvailability = MutableLiveData<ArrayMap<MenuCommand, Boolean>>()
    // OBSERVABLES

    val presenter: IMenuPresenter = MenuPresenter(this)

    override fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>) =
            actionsAvailability.postValue(value)

}