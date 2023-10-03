package com.example.serverserviceapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.example.serverserviceapp.Constants.GET_RANDOM_NUMBER
import kotlin.random.Random

class MessengerService: Service() {

    private val TAG = MessengerService::class.simpleName

    private lateinit var mMessenger: Messenger
    private val mRandomNumber
        get() = Random.nextInt(100)

    inner class IncommingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
        ) : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                GET_RANDOM_NUMBER -> {
                    val messageToRandomNumberSender = Message.obtain(null, GET_RANDOM_NUMBER)
                    messageToRandomNumberSender.arg1 = mRandomNumber
                    try {
                        msg.replyTo.send(messageToRandomNumberSender)
                    }catch (e: RemoteException ) {
                        Log.i(TAG, e.message.toString())
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(applicationContext, "Binding From MessengerService", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncommingHandler(this))
        return mMessenger.binder
    }

}