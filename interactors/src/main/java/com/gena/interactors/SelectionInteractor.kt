package com.gena.interactors

import com.gena.domain.model.figures.Point
import com.gena.domain.model.interfaces.ISelectionUseCase
import com.gena.domain.usecases.interfaces.ISelectionInteractor
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

/**
 * Created by Gena Kuchergin on 06.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class SelectionInteractor(private val useCase: ISelectionUseCase) : ISelectionInteractor {

    private val mCommandActor = actor<() -> Unit>(capacity = Channel.UNLIMITED) {
        for (action in channel) action()
    }

    override fun moveSelectedTo(point: Point) {
        mCommandActor.offer {
            useCase.moveSelectedTo(point)
        }
    }

    override fun movementFinished() {
        mCommandActor.offer {
            useCase.movementFinished()
        }
    }

    override fun findSelection(point: Point) {
        mCommandActor.offer {
            useCase.findSelection(point)
        }
    }

}