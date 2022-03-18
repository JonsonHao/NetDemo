package com.linjunhao.netdemo.exoplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.linjunhao.netdemo.R

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/20
 * @desc:
 */
class VideoListFragment : Fragment() {

    private val urls = arrayListOf(
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/fe33ea0411_6650538_fhd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/e33c541911_6634892_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/e2ee2a30b1_6214191_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/a2e66aa191_6208878_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/dc0816bcc1_6253186_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/b54b618cf1_6410005_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/c49147ebe1_6294952_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/2edd0f7531_6566988_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/a076d52ac1_6507716_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/8614567991_6325631_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/3182c77f61_6324050_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/a23ceba251_6534855_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/f2c5e98851_6466080_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/a9ebe4e391_5087509_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/426b70ead1_5909815_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/9638446931_6395731_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/fe4cb386c1_6596676_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/7343192111_6397545_hd.mp4",
    )

    private val addUrls = arrayListOf(
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/aa18e9ee-ce52-4f92-9232-5915ce03d6c7.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/3cb39fbce1_6516174_fhd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/f0ce051311_6533990_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/443744f331_6555798_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/ed1b9b8951_6542594_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/d0b05257c1_6547577_hd.mp4",
        "http://xmtool.oss-cn-shanghai.aliyuncs.com/wallpaper/dynamicWallpaper/a83d68db31_6547326_hd.mp4"
    )

    private lateinit var rootView: View

    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoPlayerAdapter

    private var isFirst = true

    private var isFirstAdd = true

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            playIndexThenPausePreviousPlayer(position)
            if (position == videoAdapter.itemCount - 2 && isFirstAdd) {
                // 当看到倒数第二个视频时，加载下一页数据
                isFirstAdd = false
                videoAdapter.addData(addUrls)
            }
        }
    }

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                if (viewPager.currentItem < videoAdapter.itemCount - 1) {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_video_list, container, false)
        viewPager = rootView.findViewById(R.id.view_pager)
        videoAdapter = VideoPlayerAdapter(requireContext())
        videoAdapter.setData(urls)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.adapter = videoAdapter
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        viewPager.offscreenPageLimit = 1
        return rootView
    }

    fun playIndexThenPausePreviousPlayer(index: Int) {
        if (VideoPlayerEngine.mPlayerMap[index]?.playWhenReady == false) {
            if (index == 0 && isFirst) {
                // 通知业务开始播放视频了
                VideoPlayerEngine.getVideoListListener()?.onPlay()
                isFirst = false
            } else {
                // 播放下一个视频时通知业务
                VideoPlayerEngine.getVideoListListener()?.onNext()
            }
            pauseCurrentPlayingVideo()
            val player = VideoPlayerEngine.mPlayerMap[index]
            player?.playWhenReady = true
            player?.seekTo(0)
            player?.addListener(listener)
            VideoPlayerEngine.currentPlayer = Pair(index, VideoPlayerEngine.mPlayerMap[index]!!)
        }
    }

    private fun pauseCurrentPlayingVideo(){
        VideoPlayerEngine.currentPlayer?.let {
            it.second.playWhenReady = false
            it.second.removeListener(listener)
        }
    }

    override fun onResume() {
        super.onResume()
        if (VideoPlayerEngine.currentPlayer?.second?.playWhenReady == false
            && !VideoPlayerEngine.isUserClickPause) {
            // 通知业务开始播放视频了
            VideoPlayerEngine.getVideoListListener()?.onPlay()
            VideoPlayerEngine.currentPlayer?.second?.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (VideoPlayerEngine.currentPlayer?.second?.playWhenReady == true) {
            VideoPlayerEngine.isUserClickPause = false
            // 通知业务暂停播放视频了
            VideoPlayerEngine.getVideoListListener()?.onPause()
            VideoPlayerEngine.currentPlayer?.second?.playWhenReady = false
        }
    }

    override fun onDestroyView() {
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        VideoPlayerEngine.release()
        super.onDestroyView()
    }
}