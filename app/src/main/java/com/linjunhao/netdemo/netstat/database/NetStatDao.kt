package com.linjunhao.netdemo.netstat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
@Dao
interface NetStatDao {
    @Query("SELECT * FROM net_stat WHERE id >= :from and id <= :end")
    fun getRangBytes(from: Long, end: Long): List<NetStat>

    @Insert
    fun insertBytes(netStat: NetStat)
}