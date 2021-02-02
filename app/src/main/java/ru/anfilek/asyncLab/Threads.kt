package ru.anfilek.asyncLab

import android.content.Context
import android.util.Log
import ru.anfilek.asyncLab.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import kotlin.random.Random

// класс, в котором лежат статические поля и синхронизированный метод
class ThreadGame {
    companion object {
        var counter = 0L
        var flag = true
    }

    @Synchronized
    fun incrementRandom() {
        counter += Random.nextLong(1, 5)
    }
}
// логика игры
fun startGame(binding: ActivityMainBinding, activity: MainActivity) {
    var firstThreadLastResult: Long = 0
    var secondThreadLastResult: Long = 0
    // использую executor, чтобы множно было оффнуть сразу все потоки разом через shutdown();
    val executor = Executors.newFixedThreadPool(2)
    // поток
    val thread1 = Runnable {
        while (ThreadGame.counter < 100 && ThreadGame.flag) {
            ThreadGame().incrementRandom()
            firstThreadLastResult = ThreadGame.counter
            if (ThreadGame.counter >= 100 && ThreadGame.flag) {
                ThreadGame.flag = false
                executor.shutdown()
                activity.runOnUiThread {
                    activity.binding.tvGameResult.text = "Thread #2 wins with score - ${ThreadGame.counter}"
                }
            }

        }
    }
    // ещё поток
    val thread2 = Runnable {
        while (ThreadGame.counter < 100 && ThreadGame.flag) {
            ThreadGame().incrementRandom()
            secondThreadLastResult = ThreadGame.counter
            if (ThreadGame.counter >= 100 && ThreadGame.flag) {
                ThreadGame.flag = false
                executor.shutdown()
                activity.runOnUiThread {
                    activity.binding.tvGameResult.text = "Thread #1 wins with score - ${ThreadGame.counter}"
                }
            }

        }
    }
    executor.execute(thread1)
    executor.execute(thread2)
    executor.shutdown()
}

fun testSharedResources(binding: ActivityMainBinding, activity: MainActivity) {
    startGame(binding, activity)
}