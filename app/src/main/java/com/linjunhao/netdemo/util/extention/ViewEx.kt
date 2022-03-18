package com.linjunhao.netdemo.util.extention

import android.view.View

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/25
 * @desc: View 相关的扩展方法
 */

/**
 * 点击事件防抖
 * @param onClick 点击事件执行
 * @param delayMillis 防抖时间
 */
inline fun View.singleClick(crossinline onClick: () -> Unit, delayMillis: Long = 1000L) {
    setOnClickListener {
        isClickable = false
        onClick()
        postDelayed({
            isClickable = true
        }, delayMillis)
    }
}

val View.isGone: Boolean
    get() = visibility == View.GONE

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

val View.isInVisible: Boolean
    get() = visibility == View.INVISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/**
 * 切换 View 的可见性
 */
fun View.toggleVisible() {
    if (isVisible) {
        gone()
    } else {
        visible()
    }
}

