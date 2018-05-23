package com.genaku.repository

import android.content.Context
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Picture
import com.gena.domain.model.figures.PictureData
import com.gena.domain.model.figures.Shape
import com.gena.domain.model.history.CommandHistory
import com.gena.domain.usecases.interfaces.IRepository
import org.mym.plog.PLog
import java.io.Serializable

/**
 * Created by Gena Kuchergin on 01.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class Repository(private val context: Context, private val defaultPictureSize: Int) : IRepository {

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
        deleteObsoleteFiles(model)
        mHistory = CommandHistory(model)
        return model
    }

    private fun deleteObsoleteFiles(model: ShapesModel) {
        val pictureFilenames = ArrayList<String>()
        for (shape in model.items) {
            if (shape is Picture && shape.filename.isNotBlank()) {
                pictureFilenames.add(shape.filename)
            }
        }
        FileRepository.clear(
                context = context,
                excludeFilenames = pictureFilenames
        )
    }

    override fun saveModel(model: ShapesModel) = with(mSettings) {
        shapes = serializeToString(model.items)
        selectedIdx = model.selectedIdx
    }

    private fun serializeToString(value: Serializable): String = try {
        ObjectSerializer.serialize(value)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    override fun loadPictureIntoRepository(filename: String): PictureData {
        return try {
            val bitmap = PictureShrinker.getShrinkedBitmapFromFile(filename, defaultPictureSize, defaultPictureSize)
            val newFilename = FileRepository.saveBitmapToRepository(context, bitmap)
            PictureData(newFilename, bitmap.width, bitmap.height)
        } catch (e: Exception) {
            e.printStackTrace()
            PictureData("", defaultPictureSize, defaultPictureSize)
        }
    }

}