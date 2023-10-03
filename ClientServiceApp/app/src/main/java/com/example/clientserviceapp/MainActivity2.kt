package com.example.clientserviceapp


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serverserviceapp.IRandomNumberInterface

class MainActivity2 : ComponentActivity() {
    private var mClientService: IRandomNumberInterface? = null
    private var bound by mutableStateOf(false)
    private var randomNumber by mutableStateOf(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (randomNumber != 0) "Random Number: $randomNumber" else "service not Bound",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    enabled = bound,
                    onClick = {
                        randomNumber = mClientService?.randomNumber ?: 0
                    }
                ) {
                    Text("Get Number")
                }
                Button(
                    enabled = !bound,
                    onClick = {
                        boundService()
                    }) {
                    Text(text = "Bound Service")
                }
                Button(
                    enabled = bound,
                    onClick = {
                        unBoundService()
                    }) {
                    Text(text = "UnBound Service")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        boundService()
    }

    private fun boundService() {
        Intent("com.example.serverserviceapp.RandomNumberService").also { intent ->
            intent.setPackage("com.example.serverserviceapp")
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unBoundService()
    }

    private fun unBoundService() {
        if (bound) {
            unbindService(mConnection)
            bound = false
            randomNumber = 0
        }
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mClientService = IRandomNumberInterface.Stub.asInterface(binder)
            bound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mClientService = null
            bound = false
        }
    }
}