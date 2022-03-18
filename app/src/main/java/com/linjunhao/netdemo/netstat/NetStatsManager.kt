package com.linjunhao.netdemo.netstat

import android.app.AppOpsManager
import android.content.Context
import android.net.TrafficStats
import android.os.Build
import android.os.Process
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.linjunhao.netdemo.netstat.helper.BaseHelper
import com.linjunhao.netdemo.netstat.helper.Helper
import com.linjunhao.netdemo.netstat.helper.LessAndroidMHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.IllegalArgumentException

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc: 网络速度和流量消耗量统计
 */
object NetStatsManager {

    private var helper: BaseHelper? = null

    private var lastTimeTxBytes = 0L
    private var lastTimeRxBytes = 0L

    private var launcher: ActivityResultLauncher<String>? = null

    fun initHelper(activity: FragmentActivity) {
        helper = if (hasUsagePermission(activity) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Helper()
            } else {
                LessAndroidMHelper()
            }
        launcher = activity.registerForActivityResult(UsageResultContract(activity)) {
            helper = if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Helper()
            } else {
                LessAndroidMHelper()
            }
        }
    }

    /**
     * 获取当天和当月的流量使用数据
     */
    fun getDayAndMonthBytes(activity: FragmentActivity): Flow<NetByte> {
        if (helper != null) {
            return getBytesWorkFlow(activity.applicationContext)
        } else {
            throw IllegalArgumentException("helper must not be null, please call initHelper() before call getDayAndMonthBytes()")
        }
    }

    /**
     * 获取当天和当月的流量使用数据
     */
    fun getDayAndMonthBytes(fragment: Fragment): Flow<NetByte> {
        if (helper != null) {
            return getBytesWorkFlow(fragment.requireActivity().applicationContext)
        } else {
            throw IllegalArgumentException("helper must not be null, please call initHelper() before call getDayAndMonthBytes()")
        }
    }

    private fun getBytesWorkFlow(context: Context) = flow {
        while (true) {
            val netByte = helper!!.getAllBytes(context)
            emit(netByte)
            delay(10000)
        }
    }.flowOn(Dispatchers.IO)

    private val speedFlow: Flow<NetSpeed> by lazy {
        flow {
            while (true) {
                val netSpeed = NetSpeed(getRxNetDifferent(), getTxNetDifferent())
                emit(netSpeed)
                delay(1000)
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取网络速度
     */
    fun getNetSpeedFlow() = speedFlow

    fun getNetSpeed() = NetSpeed(getRxNetDifferent(), getTxNetDifferent())

    private fun getTxNetDifferent(): Long {
        val totalBytes = TrafficStats.getTotalTxBytes()
        val result = if (lastTimeTxBytes != 0L) {
            totalBytes - lastTimeTxBytes
        } else {
            0L
        }
        lastTimeTxBytes = totalBytes
        return result
    }

    private fun getRxNetDifferent(): Long {
        val totalBytes = TrafficStats.getTotalRxBytes()
        val result = if (lastTimeRxBytes != 0L) {
            totalBytes - lastTimeRxBytes
        } else {
            0L
        }
        lastTimeRxBytes = totalBytes
        return result
    }

    /**
     * 申请使用权限
     */
    fun requestUsagePermission() {
        launcher?.launch("")
    }

    private fun hasUsagePermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val packageName = context.packageName
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

}