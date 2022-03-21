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
import android.view.*
import android.widget.Button
import android.widget.TextView

import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linjunhao.arouter_annotation.ARouter
import com.linjunhao.netdemo.camera.CameraActivity
import com.linjunhao.netdemo.databinding.ActivityCameraBinding
import com.linjunhao.netdemo.databinding.ActivityMainBinding
import com.linjunhao.netdemo.exoplayer.VideoPlayerActivity
import com.linjunhao.netdemo.filescan.FileScan
import com.linjunhao.netdemo.netstat.NetStatsManager
import com.linjunhao.netdemo.netstat.getFileSizeDescription
import com.linjunhao.netdemo.netstat.ui.NetSpeedActivity
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

    companion object {
        private val ITEMS = arrayListOf("语音测试", "摄像头测试", "ExoPlayer Demo", "打开网页", "网速测试")
    }

    private lateinit var viewBinding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.rlvContent.adapter = MainAdapter().apply {
            clickAction = {
                when (it) {
                    0 -> enter(TTSActivity::class.java)
                    1 -> enter(CameraActivity::class.java)
                    2 -> enter(VideoPlayerActivity::class.java)
                    3 -> enter(WebViewActivity::class.java)
                    4 -> enter(NetSpeedActivity::class.java)
                }
            }
        }
        viewBinding.rlvContent.layoutManager = LinearLayoutManager(this)

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

    private fun enter(cls: Class<*>) {
        Intent(this, cls).apply {
            startActivity(this)
        }
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

    private class MainAdapter : RecyclerView.Adapter<ItemViewHolder>() {

        var clickAction : ((Int) -> Unit)? = null

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_main, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.render(position)
            holder.itemView.setOnClickListener {
                clickAction?.invoke(position)
            }
        }

        override fun getItemCount(): Int = ITEMS.size

    }

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun render(position: Int) {
            if (itemView is TextView) {
                itemView.text = ITEMS[position]
            }
        }
    }
}