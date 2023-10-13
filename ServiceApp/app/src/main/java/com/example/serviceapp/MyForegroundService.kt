package com.example.serviceapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.serviceapp.Constants.FOREGROUNDSERVICEID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class MyForegroundService: Service() {
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
    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Action.START.toString() -> {
                loggingThread!!.start()
                running.set(true)
                startForeground(FOREGROUNDSERVICEID, createNotification("Service is Active", "running"))
            }
            Action.STOP.toString() ->{
                loggingThread!!.interrupt()
                stopForeground(STOP_FOREGROUND_REMOVE)
                with(NotificationManagerCompat.from(this)){
                    notify(101, createNotification("Service is ....", "Completed"))
                }
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

    private fun createNotification(title: String, text: String): Notification {
        val intent = Intent(this, ForegroundActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, Constants.SERVICECHANNELID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
    }
}