package com.example.mad_practical4_24012011102

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val str1 = intent.getStringExtra("Service1")

            if (str1 == "Start" || str1 == "Stop") {
                val intentService = Intent(context, AlarmService::class.java)
                intentService.putExtra("Service1", str1)

                if (str1 == "Start") {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(intentService)
                    } else {
                        context.startService(intentService)
                    }
                } else if (str1 == "Stop") {
                    context.stopService(intentService)
                }
            }
        }
    }
}