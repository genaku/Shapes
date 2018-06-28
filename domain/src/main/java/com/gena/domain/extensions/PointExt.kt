package com.gena.domain.extensions

import com.gena.domain.model.figures.Point

fun Point.shiftBy(dx: Int, dy: Int) = this.let {
    x += dx
    y += dy
}