package com.linjunhao.netdemo.netstat

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/21
 * @desc: 网络速度数据类
 * @param rx 下载速度
 * @param tx 上传速度
 */
data class NetSpeed(val rx: Long, val tx: Long)