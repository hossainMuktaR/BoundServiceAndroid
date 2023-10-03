package com.example.serverserviceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import kotlin.random.Random


class RandomNumberService: Service() {
    private val mRandomNumber
        get() = Random.nextInt(100)

    override fun onBind(p0: Intent?): IBinder? {
        return iBinder
    }
    private val iBinder: IRandomNumberInterface.Stub  = object : IRandomNumberInterface.Stub() {
        override fun getRandomNumber(): Int {
            return mRandomNumber
        }
    }
}