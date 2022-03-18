package com.linjunhao.netdemo.exoplayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.linjunhao.netdemo.R
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/20
 * @desc:
 */
class VideoPlayerAdapter(context: Context) : RecyclerView.Adapter<VideoPlayerAdapter.VideoHolder>() {

    val cacheDataSourceFactory: DataSource.Factory by lazy {
        val cacheFold = File(context.cacheDir, "media")
        val evictor = LeastRecentlyUsedCacheEvictor(100L * 1024 * 1024)
        val cache = SimpleCache(cacheFold, evictor, ExoDatabaseProvider(context))
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        CacheDataSource.Factory ()
            .setCache(cache)
            .setUpstreamDataSourceFactory(dataSourceFactory)
    }

    class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun loadVideo(adapter: VideoPlayerAdapter, index: Int, videoUri: String) {
            Log.d("ljh", "loadVideo $index")
            if (VideoPlayerEngine.mPlayerMap.containsKey(index)) {
                VideoPlayerEngine.mPlayerMap[index]?.release()
                VideoPlayerEngine.mPlayerMap.remove(index)
            }
            val player = SimpleExoPlayer.Builder(itemView.context).build()
            val playerView = itemView.findViewById<PlayerView>(R.id.player_view)
            val ivPause = itemView.findViewById<ImageView>(R.id.iv_pause)
            playerView.player = player
//            val proxy = HttpProxyCacheUtil.getVideoProxy(itemView.context)
//            val url = proxy.getProxyUrl(videoUri)
//            val uri = Uri.parse(url)
            val mediaItem = MediaItem.fromUri(videoUri)
            player.setMediaSource(ProgressiveMediaSource.Factory(adapter.cacheDataSourceFactory)
                .createMediaSource(mediaItem))
            player.repeatMode = Player.REPEAT_MODE_OFF
            player.prepare()
            VideoPlayerEngine.mPlayerMap[index] = player
            itemView.setOnClickListener {
                if (player.playWhenReady) {
                    // 通知业务暂停播放视频
                    VideoPlayerEngine.getVideoListListener()?.onPause()
                    VideoPlayerEngine.isUserClickPause = true
                    player.playWhenReady = false
                } else {
                    // 通知业务开始播放视频
                    VideoPlayerEngine.getVideoListListener()?.onPlay()
                    player.playWhenReady = true
                }
            }
            // 监听播放情况，根据情况展示相应的UI
            player.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        ivPause.visibility = View.GONE
                    } else {
                        ivPause.visibility = View.VISIBLE
                    }
                }
            })
        }
    }



    private val mVideUrls: MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_player_view, parent, false)
        return VideoHolder(rootView)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.loadVideo(this, position, mVideUrls[position])
    }

    override fun getItemCount(): Int = mVideUrls.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<String>) {
        mVideUrls.clear()
        mVideUrls.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<String>) {
        val index = mVideUrls.size
        mVideUrls.addAll(list)
        notifyItemRangeInserted(index, list.size)
    }

    override fun onViewRecycled(holder: VideoHolder) {
        super.onViewRecycled(holder)
        val position = holder.layoutPosition
        if (VideoPlayerEngine.mPlayerMap.containsKey(position)) {
            VideoPlayerEngine.mPlayerMap[position]?.release()
            VideoPlayerEngine.mPlayerMap.remove(position)
        }
    }
}