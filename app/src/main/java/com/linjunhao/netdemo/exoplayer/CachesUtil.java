package com.linjunhao.netdemo.exoplayer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/20
 * @desc:
 */
public class CachesUtil {

    public static String VIDEO = "video";

    /**
     * 获取媒体缓存文件
     *
     * @param child
     * @return
     */
    public static File getMediaCacheFile(Context context, String child) {
        String directoryPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部储存可用
            directoryPath = context.getExternalFilesDir(child).getAbsolutePath();
        } else {
            directoryPath = context.getFilesDir().getAbsolutePath() + File.separator + child;
        }
        File file = new File(directoryPath);
        //判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.d("CachesUtil", "getMediaCacheFile ====> " + directoryPath);
        return file;
    }
}
