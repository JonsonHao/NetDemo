package com.linjunhao.netdemo.filescan

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/24
 * @desc:
 */
object FileScan {

    private var startTime = 0L

    fun startScan(context: Context, filterArray: Array<String>) {
        startTime = System.currentTimeMillis()
        var cursor: Cursor? = null
        var len = 0L
        try {
            val fileUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
            )
            val sb = StringBuilder()
            filterArray.forEach {
                if (it == filterArray[filterArray.size - 1]) {
//                    sb.append(MediaStore.Files.FileColumns.SIZE).append(" == 0.0 OR ")
                    sb.append(MediaStore.Files.FileColumns.DATA).append(" LIKE '%.$it' ")
                } else {
                    sb.append(MediaStore.Files.FileColumns.DATA).append(" LIKE '%.$it' OR ")
                }
            }
            val selection = sb.toString()
            val sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED
            val resolver = context.contentResolver
            cursor = resolver.query(fileUri, projection, selection, null, sortOrder)
            if (cursor?.moveToLast() == true) {
                do {
                    val path =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
                    val size =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE))
                    val date =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED))
                    val name = path.substring(path.lastIndexOf("/") + 1, path.length)
                    len += size
                    Log.d("fileScan", "$name - $path - $size - $date")
                } while (cursor.moveToPrevious())
            }
        } catch (e: Exception) {
            Log.e("fileScan", "根据特定条件 获取文件 ERROR ...")
            e.printStackTrace()
        } finally {
            Log.d("fileScan", "垃圾数量 ${getFileSizeDescription(len)}")
            Log.d("fileScan", "扫描时间 ${System.currentTimeMillis() - startTime} ms")
            cursor?.close()
        }
    }

    private fun getFileSizeDescription(size: Long): String {
        val bytes = StringBuffer()
        val enLocale =  Locale("en", "US")

        val format = NumberFormat.getNumberInstance(enLocale) as DecimalFormat
        format.applyPattern("###.0")
        if (size >= 1024 * 1024 * 1024) {
            val i = size / (1024.0 * 1024.0 * 1024.0)
            bytes.append(format.format(i)).append("GB")
        } else if (size >= 1024 * 1024) {
            val i = size / (1024.0 * 1024.0)
            bytes.append(format.format(i)).append("MB")
        } else if (size >= 1024) {
            val i = size / 1024.0
            bytes.append(format.format(i)).append("KB")
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B")
            } else {
                bytes.append(size.toInt()).append("B")
            }
        }
        return bytes.toString()
    }
}