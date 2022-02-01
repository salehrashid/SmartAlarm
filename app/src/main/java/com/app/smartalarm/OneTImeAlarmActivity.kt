package com.app.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.smartalarm.data.Alarm
import com.app.smartalarm.data.local.AlarmDB
import com.app.smartalarm.databinding.ActivityOneTimeAlarmBinding
import com.app.smartalarm.fragment.DateDialogFragment
import com.app.smartalarm.fragment.TimeDialogFragment
import com.app.smartalarm.help.timeFormatter
import kotlinx.android.synthetic.main.activity_one_time_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTImeAlarmActivity : AppCompatActivity(), DateDialogFragment.DialogDateListener, TimeDialogFragment.TimeDialogListener {

    private var _binding: ActivityOneTimeAlarmBinding? = null
    private val binding get() = _binding as ActivityOneTimeAlarmBinding

    private val db by lazy { AlarmDB(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_time_alarm)

        _binding = ActivityOneTimeAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.apply {
            btnSetDateOneTime.setOnClickListener {
                val datePickerFragment = DateDialogFragment()
                datePickerFragment.show(supportFragmentManager, "DatePickerDialog")
            }

            btnSetTimeOneTime.setOnClickListener {
                val timePickerFragment =  TimeDialogFragment()
                timePickerFragment.show(supportFragmentManager, "timePickerDialog")
            }

            btnAdd.setOnClickListener {
                val date = tvOnceDate.text.toString()
                val time = tvOnceTime.text.toString()
                val message = edtNoteOneTime.text.toString()

                if (date == "Date" && time == "Time"){
                    Toast.makeText(applicationContext, getString(R.string.txt_toast_add_alarm),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    db.alarmDao().addAlarm(Alarm(
                        0,
                        date,
                        time,
                        message
                    ))
                    Log.i("AddAlarm", "alarm set on: $date $time with message $message")
                    finish()
                }
            }
        }
        btn_cancel.setOnClickListener {
            finish()
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()

        //untuk mengubah tanggal calendar sekarang menjadi tanggal yang telah dipilih di DatePicker
        calendar.set(year, month, dayOfMonth)
        val dateFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.tvOnceDate.text = dateFormatted.format(calendar.time)
    }

    //yang di tambahkan sendiri
    override fun onTimeSetListener(tag: String?, hour: Int, minute: Int) {
//        val time = Calendar.getInstance()
//
//        time.set(hour, minute)
//        val timeFormatted = SimpleDateFormat("HH,mm", Locale.getDefault())

        binding.tvOnceTime.text = timeFormatter(hour, minute)
    }
}