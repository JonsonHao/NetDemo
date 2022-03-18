package com.linjunhao.netdemo.netstat.database

import android.content.Context
import androidx.room.Room

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc:
 */
object NetStatDatabaseHelper {

    private var db: NetStatDatabase? = null

    private const val DB_NAME = "net_stat_db"

    @Synchronized
    fun getNetStatDataBase(context: Context): NetStatDatabase? {
        if (db == null) {
            db = Room.databaseBuilder(
                context,
                NetStatDatabase::class.java, DB_NAME
            ).build()
        }
        return db
    }

}