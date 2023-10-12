package com.example.serviceapp

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serviceapp.ui.theme.ServiceAppTheme

class ServicesActivity : ComponentActivity() {
//    private var randomNumber by mutableStateOf(0)
    private var isRunning by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            enabled = !isRunning,
                            onClick = {
                                startBackgroundService()
                            }) {
                            Text(text = "Start Service")
                        }
                        Button(
                            enabled = isRunning,
                            onClick = {
                                stopBackgroundService()
                            }) {
                            Text(text = "Stop Service")
                        }
                        Button(
                            enabled = !isRunning,
                            onClick = {
                                gotoBoundActivity()
                            }) {
                            Text(text = "Goto BoundService")
                        }
                    }

                }
            }
        }
    }
    private fun startBackgroundService() {
        Intent(this, BackgroundService::class.java).also { intent ->
            intent.action = "START_ACTION"
            startService(intent)
        }
        isRunning = true
    }
    private fun stopBackgroundService() {
        Intent(this, BackgroundService::class.java).also { intent ->
            intent.action = "STOP_ACTION"
            startService(intent)
        }
        isRunning = false
    }

    private fun gotoBoundActivity() {
        Intent(this,BoundActivity::class.java).also {
            startActivity(it)
        }
    }

}