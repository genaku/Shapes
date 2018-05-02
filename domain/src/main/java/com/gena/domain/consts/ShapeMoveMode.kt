package com.gena.domain.consts

import com.gena.domain.extensions.EnumExt

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
enum class ShapeMoveMode(override val value: Int) : EnumExt.EnumInt {
    BODY(0),
    LEFT_SIDE(2),
    RIGHT_SIDE(4),
    TOP_SIDE(8),
    BOTTOM_SIDE(16),
    LEFT_UPPER_CORNER(LEFT_SIDE.value + TOP_SIDE.value),
    LEFT_BOTTOM_CORNER(LEFT_SIDE.value + BOTTOM_SIDE.value),
    RIGHT_UPPER_CORNER(RIGHT_SIDE.value + TOP_SIDE.value),
    RIGHT_BOTTOM_CORNER(RIGHT_SIDE.value + BOTTOM_SIDE.value),
    NOTHING(-1)
}
