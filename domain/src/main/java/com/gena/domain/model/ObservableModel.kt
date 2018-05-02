package com.gena.domain.model

import java.util.*

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
open class ObservableModel : Observable() {
    protected fun notifyChanged() {
        setChanged()
        notifyObservers()
    }
}