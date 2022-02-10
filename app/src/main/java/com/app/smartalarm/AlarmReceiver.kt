package com.app.smartalarm

import android.app.*
import android.app.AlarmManager.INTERVAL_DAY
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*
import kotlin.collections.ArrayList

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)//saat alarm berdering, code tersebut membuat hp kita bergetar
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notifictionManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notifictionManager.notify(notificationId, notification)
    }

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
            0
        )//broadcast untuk menerima data dan dijadikan notif
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success set One Time Alarm.", Toast.LENGTH_LONG).show()
        Log.i("SetAlarmNotification", "setOneTimeAlarm: Alarm will rings on ${calendar.time}")
    }

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

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Success Set Up Repeating Alarm", Toast.LENGTH_LONG).show()
    }

    fun converterData(array: Array<String>): List<Int> {
        return array.map {
            it.toInt()
        }
    }

    companion object{
        const val EXTRA_TYPE = "type"
        const val EXTRA_MESSAGE = "message"

        const val TYPE_ONE_TIME = 0
        const val TYPE_REPEATING = 1

        const val ID_ONE_TIME = 101
        const val ID_REPEATING = 102
    }
}