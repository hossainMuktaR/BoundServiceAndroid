package com.example.serviceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class BackgroundService: Service() {
    private val randomNumber
        get() = Random.nextInt(100)

    private var running = AtomicBoolean(false)

    var loggingThread: Thread? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("Service", "sService is running...")
        loggingThread = Thread(logRunnable())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            "START_ACTION" -> {
                loggingThread!!.start()
                running.set(true)
            }
            "STOP_ACTION" ->{
                loggingThread!!.interrupt()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun logRunnable(): Runnable {
        return Runnable {
            while (running.get()) {
                Log.e("sService", "Random Number: $randomNumber")
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            Log.e("sService", "thread stopped")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        running.set(false)
        Log.e("sService", "Service Destroy.")
    }

}