/*
package com.linjunhao.netdemo.util.extention

import com.ship.point.wall.common.utils.KeyValueUtils
import kotlin.reflect.KProperty

*/
/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/10
 * @desc:
 *//*

class KeyValueDelegate<T>(val key: String, private val defaultValue: T) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getKeyValue(key, defaultValue) as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        saveKeyValue(key, value)
    }

    private fun saveKeyValue(key: String, value: T) {
        when (value) {
            is Int -> KeyValueUtils.setInt(key, value)
            is Long -> KeyValueUtils.setLong(key, value)
            is String -> KeyValueUtils.setString(key, value)
            is Float -> KeyValueUtils.setFloat(key, value)
            is Double -> KeyValueUtils.setDouble(key, value)
            is Boolean -> KeyValueUtils.setBoolean(key, value)
            else -> throw IllegalArgumentException("Intent extra $value has wrong type")
        }
    }

    private fun getKeyValue(key: String, defaultValue: T) = when (defaultValue) {
            is Int -> KeyValueUtils.getInt(key, defaultValue)
            is Long -> KeyValueUtils.getLong(key, defaultValue)
            is String -> KeyValueUtils.getString(key, defaultValue)
            is Float -> KeyValueUtils.getFloat(key, defaultValue)
            is Double -> KeyValueUtils.getDouble(key, defaultValue)
            is Boolean -> KeyValueUtils.getBoolean(key, defaultValue)
            else -> throw IllegalArgumentException("Intent extra $defaultValue has wrong type")
        }

}*/
