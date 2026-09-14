package com.example.mad_practical4_24012011102

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var cardCreateAlarm: MaterialCardView
    private lateinit var cardCancelAlarm: MaterialCardView
    private lateinit var btnCreateAlarm: com.google.android.material.button.MaterialButton
    private lateinit var btnCancelAlarm: com.google.android.material.button.MaterialButton
    private lateinit var tvSetAlarmTime: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardCreateAlarm = findViewById(R.id.cardCreateAlarm)
        cardCancelAlarm = findViewById(R.id.cardCancelAlarm)
        btnCreateAlarm = findViewById(R.id.btnCreateAlarm)
        btnCancelAlarm = findViewById(R.id.btnCancelAlarm)
        tvSetAlarmTime = findViewById(R.id.tvSetAlarmTime)

        btnCreateAlarm.setOnClickListener {
            showTimerDialog()
        }

        btnCancelAlarm.setOnClickListener {
            setAlarm(0L, "Stop")
            cardCancelAlarm.visibility = android.view.View.GONE
            cardCreateAlarm.visibility = android.view.View.VISIBLE
            Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTimerDialog() {
        val cldr: Calendar = Calendar.getInstance()
        val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cldr.get(Calendar.MINUTE)

        val picker = TimePickerDialog(
            this,
            { _, sHour, sMinute -> sendDialogDataToActivity(sHour, sMinute) },
            hour,
            minutes,
            false
        )
        picker.show()
    }

    private fun sendDialogDataToActivity(hour: Int, minute: Int) {
        val alarmCalendar = Calendar.getInstance()
        val now = Calendar.getInstance()

        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour)
        alarmCalendar.set(Calendar.MINUTE, minute)
        alarmCalendar.set(Calendar.SECOND, 0)
        alarmCalendar.set(Calendar.MILLISECOND, 0)

        if (alarmCalendar.before(now)) {
            alarmCalendar.add(Calendar.DATE, 1)
        }

        tvSetAlarmTime.text = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(alarmCalendar.time)

        setAlarm(alarmCalendar.timeInMillis, "Start")

        cardCancelAlarm.visibility = android.view.View.VISIBLE
        cardCreateAlarm.visibility = android.view.View.GONE

        val diff = alarmCalendar.timeInMillis - now.timeInMillis
        val diffHours = (diff / (1000 * 60 * 60)).toInt()
        val diffMinutes = ((diff / (1000 * 60)) % 60).toInt()

        val toastMessage = if (diffHours > 0) {
            "Alarm in $diffHours Hours $diffMinutes minutes"
        } else {
            "Alarm in $diffMinutes minutes"
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm(millisTime: Long, str: String) {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            234324243,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (str == "Start") {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intentPermission = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intentPermission)
                return
            }
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
        } else if (str == "Stop") {
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
        }
    }
}