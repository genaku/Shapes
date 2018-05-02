package com.gena.interactors

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.interfaces.IUseCase
import com.gena.domain.usecases.interfaces.IInteractor
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

/**
 * Created by Gena Kuchergin on 11.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Interactor(private val useCase: IUseCase) : IInteractor {

    private val mCommandActor = actor<() -> Unit>(capacity = Channel.UNLIMITED) {
        for (action in channel) action()
    }

    override fun startObserving() {
        useCase.startObserving()
    }

    override fun stopObserving() {
        useCase.stopObserving()
    }

    override fun addShape(type: ShapeType, x0: Int, y0: Int) {
        mCommandActor.offer {
            useCase.addShape(type, x0, y0)
        }
    }

    override fun deleteSelected() {
        mCommandActor.offer {
            useCase.deleteSelected()
        }
    }

    override fun undo() {
        mCommandActor.offer {
            useCase.undo()
        }
    }

    override fun redo() {
        mCommandActor.offer {
            useCase.redo()
        }
    }

    override fun saveShapes() {
        mCommandActor.offer {
            useCase.saveModel()
        }
    }

    override fun moveSelected(x: Int, y: Int) {
        mCommandActor.offer {
            useCase.moveSelected(x, y)
        }
    }

    override fun movementFinished() {
        mCommandActor.offer {
            useCase.movementFinished()
        }
    }

    override fun findSelection(x: Int, y: Int) {
        mCommandActor.offer {
            useCase.findSelection(x, y)
        }
    }
}