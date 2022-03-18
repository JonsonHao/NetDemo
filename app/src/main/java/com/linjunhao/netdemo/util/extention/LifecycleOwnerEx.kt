package com.linjunhao.netdemo.util.extention

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
//import com.xmiles.tool.core.bus.LiveDataBus

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/7
 * @desc: LifecycleOwner 扩展类
 */

inline fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<T>,
    crossinline action: (t: T) -> Unit
) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}

//inline fun <reified T> LifecycleOwner.observeDataBus(
//    key: String,
//    crossinline observer: (t: T) -> Unit
//) {
//    when (T::class.java) {
//        java.lang.String::class.java -> LiveDataBus.observeString(key, this) {
//            observer(it as T)
//        }
//        java.lang.Integer::class.java -> LiveDataBus.observeInt(key, this) {
//            observer(it as T)
//        }
//        android.os.Bundle::class.java -> LiveDataBus.observeBundle(key, this) {
//            observer(it as T)
//        }
//    }
//}
//
//fun LifecycleOwner.observeLifecycleByDataBus(
//    observer: Observer<Int>
//) {
//    LiveDataBus.observeLifecycle(this, observer)
//}
//
//fun LifecycleOwner.isLifeOnResume(
//    action: () -> Unit
//) {
//    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
//        action()
//    }
//}