package com.example.serviceapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlin.random.Random

class RandomNumberService: Service() {

    private val iBinder = LocalBinder()
    val mRandomNumber
        get() = Random.nextInt(100)

    override fun onBind(p0: Intent?): IBinder? {
        return iBinder
    }
    inner class LocalBinder: Binder() {
        fun getService(): RandomNumberService = this@RandomNumberService
    }
}