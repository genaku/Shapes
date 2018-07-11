package com.gena.domain.model.figures

import java.io.Serializable

/**
 * Created by Gena Kuchergin on 23.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
data class PictureData(
        val filename: String,
        val width: Int,
        val height: Int
) : Serializable {
    companion object {
        private const val serialVersionUID = -5441449920975275747L
    }
}