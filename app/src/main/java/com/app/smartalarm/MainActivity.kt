package com.app.smartalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.smartalarm.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding// adalah class yang menampung layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
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