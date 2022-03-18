package com.linjunhao.netdemo.netstat.helper

import android.content.Context
import androidx.annotation.WorkerThread
import com.linjunhao.netdemo.netstat.NetByte

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
interface BaseHelper {
    @WorkerThread
    fun getAllBytes(context: Context): NetByte
}