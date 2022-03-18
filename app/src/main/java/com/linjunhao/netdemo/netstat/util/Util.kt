package com.linjunhao.netdemo.netstat

import android.content.Context
import android.net.TrafficStats
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
/**
 * 获取当天的零点时间
 * @return
 */
fun getTimesMorning(): Long {
    val cal: Calendar = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

/**
 *
 */
fun getTimesMonthMorning(): Long {
    val cal: Calendar = Calendar.getInstance()
    cal.set(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONDAY),
        cal.get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    )
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    return cal.timeInMillis
}

fun getFileSizeDescription(size: Long): String {
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

fun getTotalBytesByTraffic(): Long =
    TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()