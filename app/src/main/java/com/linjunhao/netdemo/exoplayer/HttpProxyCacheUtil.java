package com.linjunhao.netdemo.exoplayer;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/20
 * @desc:
 */
public class HttpProxyCacheUtil {
    private static HttpProxyCacheServer videoProxy;

    public static HttpProxyCacheServer getVideoProxy(Context context) {
        if (videoProxy == null) {
            videoProxy = new HttpProxyCacheServer.Builder(context)
                    .cacheDirectory(CachesUtil.getMediaCacheFile(context, CachesUtil.VIDEO))
                    .maxCacheSize(1024 * 1024 * 1024) // 缓存大小
                    .fileNameGenerator(new CacheFileNameGenerator())
                    .build();
        }
        return videoProxy;
    }
}
