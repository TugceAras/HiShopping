package com.hms.referenceapp.hishopping.core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Mustafa Kemal Özdemir on 3/28/2022.
 */
class SingleLiveData<T>: MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    fun singleObserve(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner) {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    @MainThread
    fun call() {
        setValue(null)
    }

}