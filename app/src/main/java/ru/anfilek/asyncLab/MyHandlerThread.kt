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

    // инициализируем хэндлер
    private fun myHandlerPrepare() {
        handler = Handler(looper)
    }

    private fun post() {
        handler.sendEmptyMessage(Random(10).nextInt())
    }

    private fun postHandler(r: Runnable) {
        handler.post(r)
    }
    // аргументы по умолчанию,дабы можно было вызывать этот метод как с аргументами, так и без них
    fun doWork(sleepTime: Long = SLEEP_TIME, msg: String = "It works!") {
        thread {
            post()
            postHandler {
                Log.d(TAG, "handler message: $msg. Thread: ${Thread.currentThread().name}")
            }
            // имитируем работу
            Thread.sleep(sleepTime)

        }
    }
}