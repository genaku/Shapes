package com.gena.domain.usecases.interfaces

import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand

interface IMenuPresenter {
    fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>)
}