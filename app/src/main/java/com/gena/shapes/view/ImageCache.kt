package com.gena.shapes.view

import android.graphics.Bitmap
import android.support.v4.util.LruCache

/**
 * Created by Gena Kuchergin on 06.06.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class ImageCache {

    companion object {

        private val lruCache: LruCache<Int, ImageData>

        private data class ImageData(val bitmap: Bitmap, val width: Int, val height: Int)

        init {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            val cacheSize = maxMemory / 8
            lruCache = object : LruCache<Int, ImageData>(cacheSize) {
                override fun sizeOf(key: Int, imageData: ImageData): Int {
                    return imageData.bitmap.rowBytes * imageData.bitmap.height / 1024 + 4
                }
            }
        }

        fun getFromCache(key: Int, width: Int, height: Int): Bitmap? {
            try {
                val data = lruCache[key] ?: return null
                return if (data.width != width || data.height != height) {
                    null
                } else {
                    data.bitmap
                }
            } catch (e: Exception) {
                return null
            }
        }

        fun addToCache(key: Int, bitmap: Bitmap) {
            val data = lruCache[key]
            if (data == null || data.width != bitmap.width || data.height != bitmap.height) {
                lruCache.put(key, bitmap.toImageData())
            }
        }

        fun deleteFromCache(key: Int) {
            lruCache.remove(key)
        }

        private fun Bitmap.toImageData() = with(this) {
            ImageData(this, width, height)
        }

    }

}