package com.linjunhao.netdemo.netstat.helper

import android.app.usage.NetworkStatsManager
import android.net.ConnectivityManager

import android.app.usage.NetworkStats
import android.content.Context
import android.content.Context.NETWORK_STATS_SERVICE
import android.os.Build
import android.os.RemoteException
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.linjunhao.netdemo.netstat.NetByte
import com.linjunhao.netdemo.netstat.getTimesMonthMorning
import com.linjunhao.netdemo.netstat.getTimesMorning


/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc: Android 6.0 及以上获取流量信息的类
 */
@RequiresApi(Build.VERSION_CODES.M)
class Helper: BaseHelper {

    companion object {
        const val WITHIN_A_DAY = 1
        const val WITHIN_A_MONTH = 2
    }

    private var networkStatsManager: NetworkStatsManager? = null

    @WorkerThread
    override fun getAllBytes(context: Context): NetByte {
        if (networkStatsManager == null) {
            networkStatsManager = context.getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager
        }
        val dayWifiByte = getBytesByType(ConnectivityManager.TYPE_WIFI, WITHIN_A_DAY)
        val dayMobileByte = getBytesByType(ConnectivityManager.TYPE_MOBILE, WITHIN_A_DAY)
        val monthWifiByte = getBytesByType(ConnectivityManager.TYPE_WIFI, WITHIN_A_MONTH)
        val monthMobileByte = getBytesByType(ConnectivityManager.TYPE_MOBILE, WITHIN_A_MONTH)
        return NetByte(
            dayWifiByte + dayMobileByte,
            monthWifiByte + monthMobileByte
        )
    }

    private fun getBytesByType(type: Int, range: Int): Long {
        var result = 0L
        val bucket: NetworkStats.Bucket? = try {
            networkStatsManager?.querySummaryForDevice(
                type,
                null,
                if (range == WITHIN_A_DAY) getTimesMorning() else getTimesMonthMorning(),
                System.currentTimeMillis()
            )
        } catch (e: RemoteException) {
            return 0
        }
        bucket?.let { result = it.txBytes + it.rxBytes }
        return result
    }

}