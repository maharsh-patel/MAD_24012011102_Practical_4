package com.example.mad_practical4_24012011102

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MyService : Service() {
    lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!this::mediaPlayer.isInitialized){
            mediaPlayer=MediaPlayer.create(this, R.raw.alarm)
        }
        if(intent!=null){
            val str1:String? = intent.getStringExtra("Service")
            if(!mediaPlayer.isPlaying)
                mediaPlayer.start()
        }
        else
        {
            mediaPlayer.pause()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}