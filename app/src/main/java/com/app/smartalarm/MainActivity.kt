package com.app.smartalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smartalarm.adapter.AlarmAdapter
import com.app.smartalarm.data.Alarm
import com.app.smartalarm.data.local.AlarmDB
import com.app.smartalarm.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null //adalah class yang menampung layout
    private val binding get() = _binding as ActivityMainBinding

    private var alarmAdapter: AlarmAdapter? = null
    private val db by lazy { AlarmDB(this) } //lazy adalah mempermudah dalam inisialisasi

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val alarm = db.alarmDao().getAlarm()
            alarmAdapter?.setData(alarm)
            Log.i("GetAlarm", "setupRecyclerView: with this data $alarm")//background thread
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            alarmAdapter = AlarmAdapter()
            rvReminderAlarm.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = alarmAdapter
            }
        }
    }

    private fun initView(){
        binding.apply {
            cvSetOneTimeAlarm.setOnClickListener{
                startActivity(Intent(this@MainActivity, OneTImeAlarmActivity::class.java))
            }

            cvSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
            }
        }
    }
    //main thread tugas nya adalah menangani layout seperti menampilkan recycler view dan memberikan access click



    //dinonaktifkan karena sudah mendapatkan live tanggal nya di xml
//    private fun initDateToday(){
//        val calendar = Calendar.getInstance()
//        val dateFormat = SimpleDateFormat("E, dd MMMM yyyy", Locale.getDefault())
//        val formattedDate = dateFormat.format(calendar.time)
//
//        binding.tvDateToday.text = formattedDate
//    }
//
//    private fun initTimeToday() {
//        //calendar untuk mendapatkan segala hal yang berhubungan dengan waktu di android
//        val calendar = Calendar.getInstance()
//        // menentukan format jam yang akan digunakan, contohnya 13:36 atau 01:36 p.m atau 13:36:60
//        val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
//        val formattedTime = timeFormat.format(calendar.time)
//
//        binding.tvTimeToday.text = formattedTime
//    }
}