package com.gena.interactors

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.interfaces.IShapesHistoryUseCase
import com.gena.domain.usecases.interfaces.IShapesHistoryInteractor
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

/**
 * Created by Gena Kuchergin on 11.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesHistoryInteractor(private val historyUseCase: IShapesHistoryUseCase) : IShapesHistoryInteractor {

    private val mCommandActor = actor<() -> Unit>(capacity = Channel.UNLIMITED) {
        for (action in channel) action()
    }

    override fun startObserving() {
        historyUseCase.startObserving()
    }

    override fun stopObserving() {
        historyUseCase.stopObserving()
    }

    override fun addShape(type: ShapeType) {
        mCommandActor.offer {
            historyUseCase.addShape(type)
        }
    }

    override fun addPicture(filename: String) {
        mCommandActor.offer {
            historyUseCase.addPicture(filename)
        }
    }

    override fun deleteSelected() {
        mCommandActor.offer {
            historyUseCase.deleteSelected()
        }
    }

    override fun undo() {
        mCommandActor.offer {
            historyUseCase.undo()
        }
    }

    override fun redo() {
        mCommandActor.offer {
            historyUseCase.redo()
        }
    }

    override fun saveShapes() {
        mCommandActor.offer {
            historyUseCase.saveShapes()
        }
    }

}