package com.genaku.repository

import android.content.Context
import com.genaku.repository.extensions.DelegatesExt

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Settings(context: Context) {

    var shapes: String by DelegatesExt.preference(context, SHAPES, "")
    var selectedIdx: Int by DelegatesExt.preference(context, SELECTED_IDX, -1)

    companion object {
        private const val SHAPES = "SHAPES"
        private const val SELECTED_IDX = "SELECTED_IDX"
    }

}
