package com.gena.domain

import android.support.v4.util.ArrayMap
import com.gena.domain.consts.ShapeError
import com.gena.domain.consts.ShapeMoveMode
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.Constants
import com.gena.domain.model.KeyData
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.PictureData
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.history.CommandHistory
import com.gena.domain.model.history.CommandMove
import com.gena.domain.usecases.ShapesHistoryUseCase
import com.gena.domain.usecases.interfaces.IErrorPresenter
import com.gena.domain.usecases.interfaces.IMenuPresenter
import com.gena.domain.usecases.interfaces.IRepository
import com.gena.domain.usecases.interfaces.IShapesPresenter
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpek
import pl.mareklangiewicz.uspek.USpek.o
import pl.mareklangiewicz.uspek.USpekJUnitRunner
import pl.mareklangiewicz.uspek.eq

/**
 * Created by Gena Kuchergin on 14.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
@RunWith(USpekJUnitRunner::class)
class ShapesHistoryUseCaseTest {
    init {
        USpek.uspek("test rectangle") {
            val shapesPresenter = MockShapesPresenter()
            val menuPresenter = MockMenuPresenter()
            val errorPresenter = MockErrorPresenter()
            val repository = MockRepository()

            val useCase = ShapesHistoryUseCase(
                    repository = repository,
                    shapesPresenter = shapesPresenter,
                    menuPresenter = menuPresenter,
                    errorPresenter = errorPresenter
            )
            useCase.startObserving()

            "test initial start" o {
                val menuAvailability = getMenuAvailability()
                menuPresenter.menuAvailability eq menuAvailability
                errorPresenter.error eq null
                shapesPresenter.shapesModel.selectedKey eq Constants.NO_SELECTED
                shapesPresenter.shapesModel.items.size eq 0
            }

            "test add rectangle" o {
                useCase.addShape(ShapeType.RECTANGLE)
                val menuAvailability = getMenuAvailability()
                menuAvailability[MenuCommand.UNDO] = true
                menuAvailability[MenuCommand.DELETE] = true
                menuPresenter.menuAvailability eq menuAvailability
                errorPresenter.error eq null
                shapesPresenter.shapesModel.items.size eq 1
                shapesPresenter.shapesModel.selectedKey eq KeyData(0)
            }

            "test add second shape" o {
                useCase.addShape(ShapeType.RECTANGLE)
                useCase.addShape(ShapeType.OVAL)
                val menuAvailability = getMenuAvailability()
                menuAvailability[MenuCommand.UNDO] = true
                menuAvailability[MenuCommand.DELETE] = true
                menuPresenter.menuAvailability eq menuAvailability
                errorPresenter.error eq null
                shapesPresenter.shapesModel.items.size eq 2
                shapesPresenter.shapesModel.selectedKey eq KeyData(1)
            }

            "test shape move and undo move whole body then redo" o {
                useCase.addShape(ShapeType.RECTANGLE)
                useCase.addShape(ShapeType.OVAL)
                val model = shapesPresenter.shapesModel
                val key = model.selectedKey
                val shape = model.getItem(key)
                val oldX = shape.left
                val oldY = shape.top
                val newX = oldX + 10
                val newY = oldY + 20
                val command = CommandMove(key, ShapeMoveMode.BODY, oldX, oldY, newX, newY)

                useCase.execTempCommand(command)
                useCase.saveCommandToHistory(command)
                val newShape = shapesPresenter.getShape(key)
                newShape.left eq newX
                newShape.top eq newY

                useCase.undo()
                val undoShape = shapesPresenter.getShape(key)
                undoShape.left eq oldX
                undoShape.top eq oldY

                useCase.redo()
                val redoShape = shapesPresenter.getShape(key)
                redoShape.left eq newX
                redoShape.top eq newY
            }

            useCase.stopObserving()
        }
    }

    private fun getMenuAvailability(): ArrayMap<MenuCommand, Boolean> {
        val menuAvailability = ArrayMap<MenuCommand, Boolean>()
        menuAvailability[MenuCommand.ADD_PICTURE] = true
        menuAvailability[MenuCommand.ADD_RECTANGLE] = true
        menuAvailability[MenuCommand.ADD_OVAL] = true
        menuAvailability[MenuCommand.ADD_TRIANGLE] = true
        menuAvailability[MenuCommand.REDO] = false
        menuAvailability[MenuCommand.UNDO] = false
        menuAvailability[MenuCommand.DELETE] = false
        return menuAvailability
    }

    private class MockRepository : IRepository {
        private val model = ShapesModel()
        private val mHistory = CommandHistory(model)
        override val history: CommandHistory
            get() = mHistory

        override fun saveModel(model: ShapesModel) {
        }

        override fun loadModel(): ShapesModel {
            return model
        }

        override fun loadPictureIntoRepository(filename: String): PictureData {
            return PictureData(filename, 100, 100)
        }
    }

    private class MockErrorPresenter : IErrorPresenter {

        private var mError: ShapeError? = null

        val error: ShapeError?
            get() = mError

        override fun showError(error: ShapeError) {
            mError = error
        }
    }

    private class MockShapesPresenter : IShapesPresenter {

        private var mShapesModel = ShapesModel()

        val shapesModel: ShapesModel
            get() = mShapesModel

        override fun refreshShapes(shapes: ShapesModel) {
            mShapesModel = shapes
        }

        fun getShape(keyData: KeyData): Shape =
                mShapesModel.getItem(keyData)
    }

    private class MockMenuPresenter : IMenuPresenter {

        private var mMenuAvailability = ArrayMap<MenuCommand, Boolean>()

        val menuAvailability
            get() = mMenuAvailability

        override fun showMenuCommands(value: ArrayMap<MenuCommand, Boolean>) {
            mMenuAvailability = value
        }
    }

}