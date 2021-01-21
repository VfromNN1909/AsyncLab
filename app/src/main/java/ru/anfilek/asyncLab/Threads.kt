package ru.anfilek.asyncLab

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import kotlin.random.Random

object ThreadTags {
    const val FIRST_THREAD_TAG = "FIRST_THREAD"
    const val SECOND_THREAD_TAG = "SECOND_THREAD"
    const val STATUS_WORK = "WORK"
    const val STATUS_DONE = "DONE"
    const val STATUS_FAIL = "FAIL"
}

class HandlerFirst(private val otherHandler: Handler.Callback) :
        android.os.HandlerThread(ThreadTags.FIRST_THREAD_TAG), Handler.Callback {

    init {
        start()
        prepareHandlerFirst()
    }

    lateinit var handler: Handler
        private set

    private fun prepareHandlerFirst() {
        handler = Handler(looper)
    }

    override fun handleMessage(msg: Message): Boolean {
        val firstName = this.name
        handleMessages(firstName, msg, otherHandler)
        return true
    }

    fun startThreadGame() {
        val m = Message()
        m.data.apply {
            putString("status", ThreadTags.STATUS_WORK)
            putInt("value", 0)
        }
        Log.d(this.name, "start")
        otherHandler.handleMessage(m)
    }
}

class HandlerSecond :
        android.os.HandlerThread(ThreadTags.SECOND_THREAD_TAG),
        Handler.Callback {

    init {
        start()
        prepareHandlerSecond()
    }

    lateinit var handler: Handler
        private set

    lateinit var otherHandler: Handler.Callback

    private fun prepareHandlerSecond() {
        handler = Handler(Looper.getMainLooper())
    }

    override fun handleMessage(msg: Message): Boolean {
        val secondName = this.name
        handleMessages(secondName, msg, otherHandler)
        return true
    }
}

fun handleMessages(tag: String, message: Message, anotherHandler: Handler.Callback) {
    Log.d(tag, message.data.getString("status") ?: ThreadTags.STATUS_FAIL)
    Log.d(tag, "${message.data.getInt("value")}")
    val status: String
    val value: Int

    if ((message.data.getString("status") != ThreadTags.STATUS_DONE)
            && (message.data != null)) {
        value = message.data.getInt("value") + Random.nextInt(1, 5)

        status = if (value < 100) {
            ThreadTags.STATUS_WORK
        } else ThreadTags.STATUS_DONE

        message.data.apply {
            putString("status", status)
            putInt("value", value)
        }
        anotherHandler.handleMessage(message)
    }
}

fun testSharedResources() {
    val thread2 = HandlerSecond()
    val thread1 = HandlerFirst(thread2)
    thread2.otherHandler = thread1
    thread1.startThreadGame()
}