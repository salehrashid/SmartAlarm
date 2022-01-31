package com.app.smartalarm.adapter

import androidx.recyclerview.widget.DiffUtil
import com.app.smartalarm.data.Alarm

class AlarmDiffUtil(private val oldList: List<Alarm>, private val newList: List<Alarm>):
    DiffUtil.Callback(){
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]//dengan perbandingan ini kita akan mendapatkan true atau false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id == newData.id
                && oldData.date == newData.date
                && oldData.time == newData.time
                && oldData.message == newData.message
    }
}