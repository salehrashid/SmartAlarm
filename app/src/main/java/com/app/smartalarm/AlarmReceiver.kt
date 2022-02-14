package com.app.smartalarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val type = intent?.getIntExtra(EXTRA_TYPE, -1)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        val title = if (type == TYPE_ONE_TIME) "One Time Alarm" else "Repeating Alarm"

        val notificationId = if (type == TYPE_ONE_TIME) ID_ONE_TIME else ID_REPEATING

        if (message != null) showAlarmNotification(context, title, message, notificationId)
    }

    private fun showAlarmNotification(
        context: Context?,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifictionManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager//getSystemService adalah untuk menggunakan ringtone dari hp nya langsung
        val builder =
            NotificationCompat.Builder(context, channelId)//builder adalah untuk membuat notif nya
                .setSmallIcon(R.drawable.ic_one_time)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(alarmSound)

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)//saat alarm berdering, code tersebut membuat hp kita bergetar
        channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        builder.setChannelId(channelId)
        notifictionManager.createNotificationChannel(channel)

        val notification = builder.build()
        notifictionManager.notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setOneTimeAlarm(context: Context, type: Int, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra(EXTRA_MESSAGE, message)//intent put extra untuk menerima data
        intent.putExtra(EXTRA_TYPE, type)
        Log.e("ErrorSetOneTimeAlarm", "setOneTimeAlarm: $date, $time")

        //date diterima -> 2-2-2022
        //split untuk menghilangkan tanda "-" -> 2 2 2022
        val dateArray = date.split("-").toTypedArray()
//        converterData(dateArray)
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        //date
        calendar.set(Calendar.DAY_OF_MONTH, converterData(dateArray)[0])
        calendar.set(Calendar.MONTH, converterData(dateArray)[1] - 1)
        calendar.set(Calendar.YEAR, converterData(dateArray)[2])
        //time
        calendar.set(
            Calendar.HOUR_OF_DAY,
            converterData(timeArray)[0]
        )//HOUR OF DAY = 24 jam. 12 jam/a.m, p.m
        calendar.set(Calendar.MINUTE, converterData(timeArray)[1])
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_ONE_TIME,
            intent,
            PendingIntent.FLAG_MUTABLE
        )//broadcast untuk menerima data dan dijadikan notif
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success set One Time Alarm.", Toast.LENGTH_LONG).show()
        Log.i("SetAlarmNotification", "setOneTimeAlarm: Alarm will rings on ${calendar.time}")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setRepeatingAlarm(context: Context, type: Int, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()//toRegex untuk memisahkan array
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Success Set Up Repeating Alarm", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun cancelAlarm(context: Context, type: Int) {
        //alarm manager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //intent ke alarm manager
        val intent = Intent(context, AlarmReceiver::class.java)

        //ambil req code / ID_ALARM berdasarkan tipe alarmnya
        val requestCode = if (type == TYPE_ONE_TIME) ID_ONE_TIME else ID_REPEATING

        //cancel pending intent
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_MUTABLE)
        pendingIntent.cancel()

        //cancel alarm manager
        alarmManager.cancel(pendingIntent)
        Log.i("CancelAlarm", "cancelAlarm: Success canceled alarm.")
        if (type == TYPE_ONE_TIME) {
            Toast.makeText(context, "Successfully cancel One Time Alarm", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully cancel One Time Alarm", Toast.LENGTH_SHORT).show()
        }
    }

    fun converterData(array: Array<String>): List<Int> {
        return array.map {
            it.toInt()
        }
    }

    companion object {
        const val EXTRA_TYPE = "type"
        const val EXTRA_MESSAGE = "message"

        const val TYPE_ONE_TIME = 0
        const val TYPE_REPEATING = 1

        const val ID_ONE_TIME = 101
        const val ID_REPEATING = 102
    }
}