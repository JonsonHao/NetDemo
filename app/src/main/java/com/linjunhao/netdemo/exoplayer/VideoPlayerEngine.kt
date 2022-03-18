package com.linjunhao.netdemo.exoplayer

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/20
 * @desc:
 */
object VideoPlayerEngine {

    internal val mPlayerMap: MutableMap<Int, SimpleExoPlayer> = mutableMapOf()
    internal var currentPlayer: Pair<Int, SimpleExoPlayer>? = null

    private var videoListListener: VideoListListener? = null

    // 是否是用户点击暂停
    internal var isUserClickPause = false

    fun setVideoList(fragmentManager: FragmentManager, @IdRes containerViewId: Int) {
        fragmentManager.beginTransaction().apply {
            replace(containerViewId, VideoListFragment())
            commitAllowingStateLoss()
        }
    }

    fun setVideoListListener(videoListListener: VideoListListener) {
        this.videoListListener = videoListListener
    }

    internal fun getVideoListListener() = videoListListener

    internal fun release() {
        for ((key, value) in mPlayerMap) {
            value.release()
        }
    }

    interface VideoListListener {

        /**
         * 开始播放
         */
        fun onPlay()

        /**
         * 暂停播放
         */
        fun onPause()

        /**
         * 播放下一个视频
         */
        fun onNext()
    }
}