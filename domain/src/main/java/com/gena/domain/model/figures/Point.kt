package com.gena.domain.model.figures

import java.io.Serializable

data class Point(var x: Int, var y: Int) : Serializable {
    companion object {
        private const val serialVersionUID = 7677110902455678949L
    }
}