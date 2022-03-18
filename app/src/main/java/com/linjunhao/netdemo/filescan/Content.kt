package com.linjunhao.netdemo.filescan

import android.os.Environment

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/24
 * @desc:
 */

internal val SD_ROOT: String
    get() {
        return Environment.getExternalStorageDirectory().path
    }