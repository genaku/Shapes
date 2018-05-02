package com.genaku.repository

import com.gena.domain.consts.ShapeType
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.history.Command
import com.gena.domain.model.history.CommandCreate
import com.gena.domain.model.history.CommandDelete
import com.gena.domain.model.history.CommandHistory
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpek
import pl.mareklangiewicz.uspek.USpek.o
import pl.mareklangiewicz.uspek.USpekJUnitRunner
import pl.mareklangiewicz.uspek.eq

/**
 * Created by Gena Kuchergin on 02.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
@RunWith(USpekJUnitRunner::class)
class CommandHistoryStoreTest {
    init {
        USpek.uspek("test storage") {
            "test history serialize/deserialize" o {
                val history = CommandHistory(ShapesModel())
                history.saveCommand(CommandCreate(ShapeType.RECTANGLE, 0, 0))
                history.saveCommand(CommandDelete(1))
                val serialized = ObjectSerializer.serialize(history.historyList)
                @Suppress("UNCHECKED_CAST")
                val deserialized = ObjectSerializer.deserialize(serialized) as ArrayList<Command>
                deserialized.size eq history.historyList.size
                deserialized[0].javaClass.simpleName eq history.historyList[0].javaClass.simpleName
                deserialized[1].javaClass.simpleName eq history.historyList[1].javaClass.simpleName
            }
        }
    }
}