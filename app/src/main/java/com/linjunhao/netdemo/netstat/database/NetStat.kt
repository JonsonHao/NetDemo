package com.linjunhao.netdemo.netstat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
@Entity(tableName = "net_stat")
data class NetStat(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "bytes") val bytes: Long
)