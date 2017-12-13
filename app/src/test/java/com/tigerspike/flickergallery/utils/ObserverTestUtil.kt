package com.tigerspike.flickergallery.utils

import io.reactivex.Single
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class ObserverTestUtil {

    companion object {
        @JvmStatic
        @Throws(InterruptedException::class)
        fun <T> getValue(observer: Single<T>): T {
            val data = arrayOfNulls<Any>(1)
            val latch = CountDownLatch(1)
            observer.subscribe { s ->
                data[0] = s
                latch.countDown()
            }
            latch.await(2, TimeUnit.SECONDS)
            return data[0] as T
        }
    }
}