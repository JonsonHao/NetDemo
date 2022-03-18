package com.linjunhao.netdemo.exoplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.linjunhao.arouter_annotation.ARouter
import com.linjunhao.netdemo.R

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/19
 * @desc:
 */
@ARouter
class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        VideoPlayerEngine.setVideoListListener(object : VideoPlayerEngine.VideoListListener {

            override fun onPlay() {
                Log.d("ljh", "onPlay")
            }

            override fun onPause() {
                Log.d("ljh", "onPause")
            }

            override fun onNext() {
                Log.d("ljh", "onNext")
            }

        })
        VideoPlayerEngine.setVideoList(supportFragmentManager, R.id.fl_container)
    }

}