package com.app.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.smartalarm.data.Alarm
import com.app.smartalarm.data.local.AlarmDB
import com.app.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.app.smartalarm.fragment.TimeDialogFragment
import com.app.smartalarm.help.timeFormatter
import kotlinx.android.synthetic.main.activity_repeating_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepeatingAlarmActivity : AppCompatActivity(), TimeDialogFragment.TimeDialogListener {

    private var _binding: ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding

    private val db by lazy { AlarmDB(this) }
    private var alarmReceiver: AlarmReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeating_alarm)

        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        initView()
    }

    private fun initView() {
        binding.apply {
            btnSetTimeRepeating.setOnClickListener {
                val timePickerFragment = TimeDialogFragment()
                timePickerFragment.show(supportFragmentManager, "timePickerDialog")
            }
            btnAdd.setOnClickListener {
                val time = tvOnceTimeRepeating.text.toString()
                val message = edtNote.text.toString()

                if (time == "Time") {
                    Toast.makeText(
                        applicationContext, getString(R.string.txt_toast_add_alarm),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    alarmReceiver?.setRepeatingAlarm(
                        applicationContext,
                        AlarmReceiver.TYPE_REPEATING,
                        time,
                        message
                    )
                    //IO menjalankan background task
                    CoroutineScope(Dispatchers.IO).launch {
                        db.alarmDao().addAlarm(
                            Alarm(
                                0,
                                "Repeating Alarm",
                                time,
                                message
                            )
                        )
                        Log.i("AddAlarm", "alarm set on: $time with message $message")
                        finish()
                    }
                }
            }
        }
        btn_cancel.setOnClickListener {
            finish()
        }
    }

    override fun onTimeSetListener(tag: String?, hour: Int, minute: Int) {
        binding.tvOnceTimeRepeating.text = timeFormatter(hour, minute)
    }
}