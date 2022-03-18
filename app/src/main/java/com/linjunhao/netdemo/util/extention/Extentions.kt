package com.linjunhao.netdemo.util.extention

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
//import com.ship.point.base_network.request.IRequestParam
//import com.ship.point.base_network.response.IResponse
import java.lang.IllegalStateException

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/11/1
 * @desc: Kotlin 扩展函数
 */

/**
 * 将 dimen 资源转成 pixel
 * @param context 上下文，用来获取资源
 */
inline fun <reified T> Int.pixelOffset(context: Context): T = when (T::class) {
    Int::class -> {
        context.resources.getDimensionPixelOffset(this) as T
    }
    Float::class -> {
        context.resources.getDimension(this) as T
    }
    else -> {
        throw IllegalStateException("不支持的类型")
    }
}

/**
 * 将请求转换成协程的形式
 */
//suspend inline fun <reified T> IRequestParam.await(): T = suspendCoroutine { cont ->
//    execute(object : IResponse<T> {
//        override fun onFailure(code: String?, msg: String?) {
//            cont.resumeWithException(Exception(msg))
//        }
//
//        override fun onSuccess(t: T?) {
//            if (t == null) {
//                cont.resumeWithException(Exception("data is null"))
//            } else {
//                cont.resume(t)
//            }
//        }
//    })
//}

/**
 * 将 ViewBinding 绑定到 ViewGroup 中
 * @param inflate ViewBinding 的 inflate 函数
 */
fun <VB : ViewBinding> ViewGroup.binding(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) = inflate(LayoutInflater.from(context), this, false)