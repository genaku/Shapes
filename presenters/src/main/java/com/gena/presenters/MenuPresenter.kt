package com.gena.presenters

import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand
import com.gena.domain.usecases.interfaces.IMenuPresenter
import com.gena.presenters.interfaces.IMenuViewModel

class MenuPresenter(private val viewModel: IMenuViewModel) : IMenuPresenter {
    override fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>) =
            viewModel.showMenuCommands(value)
}