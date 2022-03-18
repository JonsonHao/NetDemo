package com.linjunhao.netdemo.netstat

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc: 流量信息类
 * @param aDay 当天内的流量消耗总量
 * @param aMonth 当月内的流量消耗总量
 */
data class NetByte(val aDay: Long, val aMonth: Long)