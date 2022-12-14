package com.example.mytodolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodolist.databinding.ActivityEditBinding
import com.example.mytodolist.databinding.ActivityScheduleEditBinding
import com.example.mytodolist.model.ScheduleData
import com.example.mytodolist.model.TodoListData
import kotlinx.android.synthetic.main.activity_schedule_edit.*

class ScheduleEditActivity : AppCompatActivity() {

    private lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private lateinit var timepicker : TimePicker
    private var targetTime : String = ""
    private var targetDay : String = ""
    private var id = 0
    private var scheduleContent : ScheduleData? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)

        timepicker = scheduleEditBinding.timePicker

        val type = intent.getStringExtra("type") //schedule
        targetDay = intent.getStringExtra("time").toString()

        scheduleEditBinding.selectTime.text = targetDay

        timepicker.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute ->
            var hour = hourOfDay
            //오전 오후를 표시하기 위해 사용된 코드
            if (hour > 12) {
                hour -= 12
                scheduleEditBinding.tvTime.text = "오후 $hour 시 $minute 분"
                targetTime = "오후 $hour 시 $minute 분"
            } else {
                scheduleEditBinding.tvTime.text = "오전 $hour 시 $minute 분"
                targetTime = "오후 $hour 시 $minute 분"
            }
        })

        scheduleEditBinding.scheduleSaveBtn.setOnClickListener {
            val contentPost = scheduleEditBinding.scheduleText.text.toString() //schedule edit text

            if (type.equals("schedule")) {
                if (contentPost.isNotEmpty()) {
                    //객체생성은 따로 입력받아서 하는거
                    var schedule = ScheduleData(id, contentPost, targetTime)
                    val intent = Intent().apply {
                        putExtra("schedule", schedule)
                        putExtra("flag",3)
                    }
                    id += 1
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}