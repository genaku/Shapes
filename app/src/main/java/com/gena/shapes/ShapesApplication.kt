package com.gena.shapes

import android.app.Application
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.gena.domain.usecases.interfaces.IRepository
import com.genaku.repository.Repository
import com.squareup.leakcanary.LeakCanary
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        PLog.init(PLogConfig.Builder()
                .forceConcatGlobalTag(true)
                .needLineNumber(true)
                .useAutoTag(true)
                .needThreadInfo(true)
                .globalTag("IS")
                .build())
        PLog.prepare(DebugPrinter(BuildConfig.DEBUG)) //all logs will be automatically disabled on release version
        repository = Repository(this, getDefaultPictureSize())
    }

    private fun getDefaultPictureSize(): Int {
        val wm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return Math.min(size.x, size.y) / 3
    }

    companion object {
        lateinit var repository: IRepository
    }

}