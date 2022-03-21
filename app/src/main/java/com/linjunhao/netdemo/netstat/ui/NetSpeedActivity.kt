package com.linjunhao.netdemo.netstat.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.linjunhao.netdemo.R
import com.linjunhao.netdemo.netstat.NetStatsManager
import com.linjunhao.netdemo.netstat.getFileSizeDescription
import com.linjunhao.netdemo.util.extention.singleClick
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/3/21
 * @desc:
 */
class NetSpeedActivity : AppCompatActivity() {

    private val speedJob: Job by lazy {
        lifecycleScope.launch {
            NetStatsManager.getNetSpeedFlow().collect {
                val str =
                    "down: ${getFileSizeDescription(it.rx)}/s, up: ${getFileSizeDescription(it.tx)}/s"
                tvSpeed.text = str
            }
        }
    }

    private lateinit var tvSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_netspeed)
        val tvNet = findViewById<TextView>(R.id.tv_net)
        tvSpeed = findViewById(R.id.tv_speed)
        NetStatsManager.initHelper(this)
        lifecycleScope.launch {
            NetStatsManager.getDayAndMonthBytes(this@NetSpeedActivity).collect {
                val str =
                    "day: ${getFileSizeDescription(it.aDay)} month: ${getFileSizeDescription(it.aMonth)}"
                tvNet.text = str
            }
        }
        speedJob.start()
        findViewById<Button>(R.id.btn_permission).singleClick({
            NetStatsManager.requestUsagePermission()
        })
    }

}