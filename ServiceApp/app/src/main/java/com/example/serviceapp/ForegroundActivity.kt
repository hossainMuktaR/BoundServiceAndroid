package com.example.serviceapp

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.serviceapp.ui.theme.ServiceAppTheme

class ForegroundActivity : ComponentActivity() {
    private var runningService by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        runningService = foregroundServiceRunning()

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
                            enabled = !runningService,
                            onClick = {
                                startService()
                            }) {
                            Text(text = "Start Service")
                        }
                        Button(
                            enabled = runningService,
                            onClick = {
                                stopService()
                            }) {
                            Text(text = "Stop Service")
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startService() {
        if(!foregroundServiceRunning()){
            Intent(this, MyForegroundService::class.java).also { intent ->
                intent.action = Action.START.toString()
                startForegroundService(intent)
                runningService = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopService() {
        Intent(this, MyForegroundService::class.java).also { intent ->
            intent.action = Action.STOP.toString()
            startForegroundService(intent)
            runningService = false
        }
    }

    fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (MyForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

}