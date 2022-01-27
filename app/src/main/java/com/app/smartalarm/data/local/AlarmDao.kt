package com.app.smartalarm.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.app.smartalarm.data.Alarm

@Dao
interface AlarmDao {
    @Insert
    fun addAlarm(alarm: Alarm)
}
