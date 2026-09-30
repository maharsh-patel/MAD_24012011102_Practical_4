package com.example.mad_practical4_24012011102

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private var mp: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "alarm_service_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm Playing Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("MAD Alarm Ringing")
            .setContentText("Your set alarm is active")
            .setSmallIcon(R.drawable.alarm_outlined_alert_clock_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, notification)

        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.alarm)
            mp?.isLooping = true
        }
        mp?.start()

        return START_STICKY
    }

    override fun onDestroy() {
        mp?.stop()
        mp?.release()
        mp = null
        super.onDestroy()
    }
}