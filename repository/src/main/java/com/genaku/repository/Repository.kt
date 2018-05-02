package com.genaku.repository

import android.content.Context
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.history.CommandHistory
import com.gena.domain.usecases.interfaces.IRepository
import org.mym.plog.PLog
import java.io.Serializable
import java.util.*

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Repository(context: Context) : IRepository {

    private val mSettings = Settings(context)
    private lateinit var mHistory: CommandHistory

    override val history: CommandHistory
        get() = mHistory

    override fun loadModel(): ShapesModel {
        val selectedIdx = mSettings.selectedIdx
        val dataList = try {
            PLog.d("load [${mSettings.shapes}]")
            @Suppress("UNCHECKED_CAST")
            ObjectSerializer.deserialize(mSettings.shapes) as ArrayList<Shape>?
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList<Shape>()
        }
        val model = ShapesModel(dataList ?: ArrayList(), selectedIdx)
        PLog.d("load model ${model.size}")
        mHistory = CommandHistory(model)
        return model
    }

    override fun saveModel(model: ShapesModel) = with(mSettings) {
        PLog.d("save model ${model.size}")
        shapes = serializeToString(model.items)
        PLog.d("save $shapes")
        selectedIdx = model.selectedIdx
    }

    private fun serializeToString(value: Serializable): String = try {
        ObjectSerializer.serialize(value)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}