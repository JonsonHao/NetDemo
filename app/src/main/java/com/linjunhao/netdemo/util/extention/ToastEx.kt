package com.linjunhao.netdemo.util.extention

import android.content.Context
import android.widget.Toast

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/10
 * @desc:
 */

fun Context.showShortToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}