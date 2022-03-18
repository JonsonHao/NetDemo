package com.linjunhao.netdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.linjunhao.arouter_annotation.ARouter
import com.linjunhao.netdemo.exoplayer.VideoPlayerActivity
import com.linjunhao.netdemo.filescan.FileScan
import com.linjunhao.netdemo.netstat.NetStatsManager
import com.linjunhao.netdemo.netstat.getFileSizeDescription
import com.linjunhao.netdemo.tts.TTSActivity
import com.linjunhao.netdemo.util.extention.singleClick
import com.linjunhao.netdemo.webview.WebViewActivity
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.UnsupportedEncodingException
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*


const val LANGUAGE = "en"
const val COUNTRY = "US"
const val PATTERN = "###.0"
const val GB = "GB"
const val MB = "MB"
val enLocale =  Locale(LANGUAGE, COUNTRY)
class MainActivity : AppCompatActivity() {

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ljh", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_second).setOnClickListener {
            Intent(this, VideoPlayerActivity::class.java).apply {
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.btn_tts).setOnClickListener {
            Intent(this, TTSActivity::class.java).apply {
                startActivity(this)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,)
                .request { _, _, _ ->
                }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${packageName}")
                startActivityForResult(intent, 10901)
            }
        }

        val tvNet = findViewById<TextView>(R.id.tv_net)
        tvSpeed = findViewById(R.id.tv_speed)
        NetStatsManager.initHelper(this)
        lifecycleScope.launch {
            NetStatsManager.getDayAndMonthBytes(this@MainActivity).collect {
                val str =
                    "day: ${getFileSizeDescription(it.aDay)} month: ${getFileSizeDescription(it.aMonth)}"
                tvNet.text = str
            }
        }
        speedJob.start()
        findViewById<Button>(R.id.btn_permission).singleClick({
            NetStatsManager.requestUsagePermission()
        })
        findViewById<Button>(R.id.btn_webview).singleClick({
            Intent(this, WebViewActivity::class.java).apply {
                startActivity(this)
            }
        })

        Log.d("ljh", getFileSizeMB(1021312413124142))

        findViewById<Button>(R.id.btn_start_scan).setOnClickListener {
            FileScan.startScan(
                this,
                arrayOf(
                    "log",
                    "hprof",
                    "xlog",
                    "livelog",
                    "logs",
                    "temp",
                    "tmp",
                    "tmf",
                    "cache",
                    "caches",
                    "xcrash",
                    "apk",
                    "apk.tp"
                )
            )
        }

        addWindowView()
    }

    fun encodeToString(str: String): String {
        try {
            return Base64.encodeToString(str.toByteArray(charset("UTF-8")), Base64.DEFAULT)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getFileSizeMB(size: Long): String {
        if (size == 0L) return "${size}${MB}"
        val bytes = StringBuffer()
        val format = NumberFormat.getNumberInstance(enLocale)
        format.maximumFractionDigits = 3
        format.roundingMode = RoundingMode.UP
        val i = size / (1024.0 * 1024.0)
        bytes.append(format.format(i)).append(MB)
        return bytes.toString()
    }

    private fun addWindowView() {
        val btn = Button(this)
        btn.text = "NewWindow"
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            0,
            0,
            PixelFormat.TRANSPARENT
        ).apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            gravity = Gravity.START or Gravity.TOP
            x = 100
            y = 100
        }
        windowManager.addView(btn, params)
    }
}