package com.gena.shapes

import android.app.Application
import com.gena.domain.usecases.interfaces.IRepository
import com.genaku.repository.Repository
import org.mym.plog.DebugPrinter
import org.mym.plog.PLog
import org.mym.plog.config.PLogConfig

/**
 * Created by Gena Kuchergin on 29.04.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ShapesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PLog.init(PLogConfig.Builder()
                .forceConcatGlobalTag(true)
                .needLineNumber(true)
                .useAutoTag(true)
                .needThreadInfo(true)
                .globalTag("IS")
                .build())
        PLog.prepare(DebugPrinter(BuildConfig.DEBUG)) //all logs will be automatically disabled on release version
        repository = Repository(this)
    }

    companion object {
        lateinit var repository: IRepository
    }

}