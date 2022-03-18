package com.linjunhao.netdemo.netstat.helper

import android.content.Context
import android.content.SharedPreferences
import android.net.TrafficStats
import android.util.Log
import com.linjunhao.netdemo.netstat.NetByte
import com.linjunhao.netdemo.netstat.database.NetStat
import com.linjunhao.netdemo.netstat.database.NetStatDatabaseHelper
import com.linjunhao.netdemo.netstat.getTimesMonthMorning
import com.linjunhao.netdemo.netstat.getTimesMorning
import com.linjunhao.netdemo.netstat.getTotalBytesByTraffic

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc: Android 6.0以下获取流量信息的类
 */
class LessAndroidMHelper: BaseHelper {

    companion object {
        private const val SP_FILE_NAME = "net_work_all_bytes"
        private const val KEY_LAST_TIME = "last_time_all_bytes"
    }

    private var sharedPreferences: SharedPreferences? = null

    override fun getAllBytes(context: Context): NetByte {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
        }
        val lastTimeByte = getAllBytesLastTime()
        val totalBytes = getTotalBytesByTraffic()
        var dayBytes = 0L
        var monthBytes = 0L
        if (lastTimeByte != 0L && totalBytes > lastTimeByte) {
            val bytes = totalBytes - lastTimeByte
            NetStatDatabaseHelper.getNetStatDataBase(context)?.netStatDao()
                ?.insertBytes(NetStat(System.currentTimeMillis(), bytes))
        }
        saveAllBytesLastTime(totalBytes)
        val dayList = NetStatDatabaseHelper.getNetStatDataBase(context)?.netStatDao()
            ?.getRangBytes(getTimesMorning(), System.currentTimeMillis())
        dayList?.forEach {
            dayBytes += it.bytes
        }
        val monthList = NetStatDatabaseHelper.getNetStatDataBase(context)?.netStatDao()
            ?.getRangBytes(getTimesMonthMorning(), System.currentTimeMillis())
        monthList?.forEach {
            monthBytes += it.bytes
        }
        return NetByte(dayBytes, monthBytes)
    }

    private fun getAllBytesLastTime(): Long =
        sharedPreferences?.getLong(KEY_LAST_TIME, 0L) ?: 0

    private fun saveAllBytesLastTime(bytes: Long) =
        sharedPreferences?.edit()?.putLong(KEY_LAST_TIME, bytes)?.apply()

}