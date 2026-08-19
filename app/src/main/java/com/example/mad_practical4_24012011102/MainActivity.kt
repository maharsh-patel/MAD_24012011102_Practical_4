package com.example.mad_practical4_24012011102

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView


class MainActivity : AppCompatActivity() {

    private lateinit var createAlarmButton: Button
    private lateinit var textAlarmTime: TextView
    private lateinit var cancelAlarmButton : Button
    private lateinit var cartView: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        textAlarmTime = findViewById(R.id.textAlarmTime)
        cartView = findViewById(R.id.card_2)
        cartView.visibility = View.GONE

        createAlarmButton = findViewById(R.id.create_alarm_button)
        createAlarmButton.setOnClickListener {
            showTimerDialog()
        }

        cancelAlarmButton = findViewById<Button>(R.id.cancel_alarm_button)
        cancelAlarmButton.setOnClickListener{
            cancelAlarm()
        }

    }

        fun getCurrentTime():String{
            val cal = Calendar.getInstance()
            val df: DateFormat = SimpleDateFormat("MMM,dd yyyy hh:mm:ss a")
            return  df.format(cal.time)
        }


        fun getMillis(hour:Int,min:Int):Long{
            val setCalendar = Calendar.getInstance()
            setCalendar[Calendar.HOUR_OF_DAY]=hour
            setCalendar[Calendar.MINUTE]=min
            setCalendar[Calendar.SECOND]=0
            return setCalendar.timeInMillis
        }


        fun showTimerDialog(){
            val cldr : Calendar = Calendar.getInstance()
            val hour : Int =cldr.get(Calendar.HOUR_OF_DAY)
            val minutes : Int = cldr.get(Calendar.MINUTE)

            val picker = TimePickerDialog(
                this,
                {tp,sHour,sMinuts -> sendDialogDataToActivity(sHour,sMinuts) },
                hour,
                minutes,
                false
            )
            picker.show()
        }
    private fun sendDialogDataToActivity(hour: Int, minute: Int) {
        val alarmCalendar = Calendar.getInstance()
        val year: Int = alarmCalendar.get(Calendar.YEAR)
        val month: Int = alarmCalendar.get(Calendar.MONTH)
        val day: Int = alarmCalendar.get(Calendar.DATE)

        alarmCalendar.set(year, month, day, hour, minute, 0)
        textAlarmTime.text = SimpleDateFormat("hh:mm ss a").format(alarmCalendar.time)

        cartView.visibility = View.VISIBLE

        setAlarm(alarmCalendar.timeInMillis, "Start")
        Toast.makeText(this, "Time: hours:${hour}, minutes:${minute}, millis:${alarmCalendar.timeInMillis}", Toast.LENGTH_SHORT).show()
    }

        fun  setAlarm(millisTimes:Long,str:String)
        {
            val intent = Intent(this,AlarmBroadcastReceiver::class.java)
            intent.putExtra("Service1",str)
            val pendingIntent = PendingIntent.getBroadcast(applicationContext,234324243,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val alarmManager =getSystemService(ALARM_SERVICE) as AlarmManager
            if(str=="Start"){
                if(alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        millisTimes,
                        pendingIntent
                    )

                }
                else{
                    Toast.makeText(this,"cannot Schedul alaram",Toast.LENGTH_LONG).show()
                }
            }
            else if(str=="Stop"){
                alarmManager.cancel(pendingIntent)
                sendBroadcast(intent)
            }
        }
    fun cancelAlarm() {
        setAlarm(0, "Stop")
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
        cartView.visibility = View.GONE
    }
}