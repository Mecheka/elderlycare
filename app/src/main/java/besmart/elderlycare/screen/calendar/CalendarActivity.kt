package besmart.elderlycare.screen.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.schedule.Schedule
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import kotlin.random.Random

class CalendarActivity : AppCompatActivity(), OnDateSelectedListener {

    private lateinit var calendarAdapter : CalendarAdapter
    private lateinit var calendarList: MutableList<List<Schedule>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()

    }

    private fun initInstance() {
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        calendar.selectedDate = CalendarDay.today()
        calendar.setOnDateChangedListener(this)
        calendarList = randomSchedule()
        val day= Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        calendarAdapter = CalendarAdapter(calendarList[day].toMutableList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CalendarActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@CalendarActivity, DividerItemDecoration.VERTICAL))
            adapter = calendarAdapter
        }
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        calendarAdapter.setScheduleData(calendarList[date.day])
    }

    private fun randomSchedule(): MutableList<List<Schedule>> {
        val randomListSize = Random.nextInt(1, 10)
        return MutableList<List<Schedule>>(32){
            List<Schedule>(randomListSize){
                Schedule.randomValue()
            }
        }
    }
}
