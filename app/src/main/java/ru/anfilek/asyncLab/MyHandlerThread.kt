package ru.anfilek.asyncLab

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.util.*
import kotlin.concurrent.thread

class MyHandlerThread : HandlerThread(TAG) {

    companion object {
        private const val TAG = "MyHandlerThread"
        private const val SLEEP_TIME = 7000L
    }

    init {
        start()
        myHandlerPrepare()
    }

    lateinit var handler: Handler
        private set

    private fun myHandlerPrepare() {
        handler = Handler(looper)
    }

    private fun post() {
        handler.sendEmptyMessage(Random(10).nextInt())
    }

    private fun postHandler(r: Runnable) {
        handler.post(r)
    }

    fun doWork(sleepTime: Long = SLEEP_TIME, msg: String = "It works!") {
        thread {
            post()
            postHandler {
                Log.d(TAG, "handler message: $msg. Thread: ${Thread.currentThread().name}")
            }
            Thread.sleep(sleepTime)

        }
    }
}