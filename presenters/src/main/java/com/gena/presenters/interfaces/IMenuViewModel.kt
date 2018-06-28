package com.gena.presenters.interfaces

import android.support.v4.util.ArrayMap
import com.gena.domain.model.MenuCommand

/**
 * Created by Gena Kuchergin on 09.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
interface IMenuViewModel {
    fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>)
}