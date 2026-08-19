package com.example.mad_practical4_24012011102

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getStringExtra("Service1")
        if(action=="Start"){
            val serviceIntent = Intent(context, MyService::class.java)
            serviceIntent.putExtra("Service", "Play")
            context.startService(serviceIntent)
            Toast.makeText(context, "Alarm started", Toast.LENGTH_SHORT).show()
        }
        else if (action == "Stop") {
            val serviceIntent = Intent(context, MyService::class.java)
            context.stopService(serviceIntent)
            Toast.makeText(context, "Alarm stopped", Toast.LENGTH_SHORT).show()
        }
    }
}