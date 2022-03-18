package com.linjunhao.netdemo.util

/**
 * @author: linjunhao
 * @email: linjunhao@xmiles.cn
 * @date: 2021/11/5
 * @desc: 对事件进行封装，防止数据倒灌
 */
open class SingleEvent<out T>(private val content: T) {

    // 外部只读变量
    var hasBeenHandled = false
        private set

    /**
     * 返回数据，如果数据已被使用使用过则返回 null
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * 返回内容, 不管数据是否被使用过，都会返回数据
     */
    fun peekContent(): T = content
}