package com.gena.domain.model.figures

import com.gena.domain.consts.ShapeType

/**
 * Created by Gena Kuchergin on 18.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Picture : Shape {

    constructor(data: ShapeData) : super(data)

    constructor(pictureData: PictureData) :
            super(ShapeData(
                    ShapeType.PICTURE,
                    arrayListOf(0, pictureData.width),
                    arrayListOf(0, pictureData.height),
                    pictureData.filename)
            )

    override val numberOfPoints: Int = 2

    val filename
        get() = data.filename

    companion object {
        private const val serialVersionUID = -7893724938095347677L
    }

}