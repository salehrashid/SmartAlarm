package com.app.smartalarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smartalarm.data.Alarm
import com.app.smartalarm.databinding.ItemRowReminderAlarmBinding

class AlarmAdapter(private val listAlarm: ArrayList<Alarm>) : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemRowReminderAlarmBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemRowReminderAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            itemDateAlarm.text = alarm.date
            itemTimeAlarm.text = alarm.time
            itemNoteAlarm.text = alarm.message
        }
    }

    override fun getItemCount() = listAlarm.size
}