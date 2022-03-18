package com.linjunhao.netdemo.netstat.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
@Database(entities = [NetStat::class], version = 1)
abstract class NetStatDatabase : RoomDatabase() {
    abstract fun netStatDao(): NetStatDao
}