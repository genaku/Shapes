package com.genaku.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Gena Kuchergin on 23.05.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class FileRepository {

    companion object {

        fun clear(context: Context, excludeFilenames: ArrayList<String>) {
            val dir = File(getPictureFileStoragePath(context))
            val files = dir.listFiles() ?: return
            for (file in files) {
                val filename = file.name
                if (filename !in excludeFilenames) {
                    File(filename).delete()
                }
            }
        }

        fun saveBitmapToRepository(context: Context, bitmap: Bitmap): String {
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            val newFilename = getNewFilename(context)
            val file = File(newFilename)
            file.delete()
            val fos = FileOutputStream(file)
            try {
                fos.write(bitmapData)
            } finally {
                fos.flush()
                fos.close()
            }
            return newFilename
        }

        private fun getNewFilename(context: Context): String {
            val uniqueID = UUID.randomUUID()
            return getPictureFileStoragePath(context) + File.separator + "$uniqueID.jpg"
        }

        private fun getPictureFileStoragePath(context: Context): String {
            val dir = if (isExternalStorageWritable(context))
                context.getExternalFilesDir(null)
            else
                context.filesDir
            val path = if (dir != null)
                dir.path
            else
                getOldFileStoragePath(context)
            val picturePath = path + File.separator + "pictures"
            File(picturePath).mkdirs()
            return picturePath
        }

        /* Checks if external storage is available for read and write */
        private fun isExternalStorageWritable(context: Context): Boolean {
            val state = Environment.getExternalStorageState()
            var result = (Environment.MEDIA_MOUNTED == state)
            if (result) {
                val dir = context.getExternalFilesDir(null) ?: return false
                val tempDir = File("${dir.path}${File.separator}tmp")
                tempDir.mkdirs()
                result = tempDir.exists()
                tempDir.delete()
            }
            return result
        }

        private fun getOldFileStoragePath(context: Context): String =
                android.os.Environment.getExternalStorageDirectory().path + File.separator + context.packageName

    }

}