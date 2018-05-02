@file:Suppress("UNCHECKED_CAST")

package com.gena.shapes.extensions

import android.arch.lifecycle.*
import android.support.v4.app.FragmentActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

fun <T> MutableLiveData<T>.setObserver(owner: LifecycleOwner, update: (T?) -> Unit) {
    this.observe(owner, Observer { newValue -> update(newValue) })
}

suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return async(CommonPool) { block() }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}
