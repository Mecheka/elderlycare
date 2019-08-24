package besmart.elderlycare.screen.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.schedule.Schedule
import besmart.elderlycare.util.BaseViewModel

class CalendarViewModel : BaseViewModel() {

    private val _schuduelLiveData = MutableLiveData<List<Schedule>>()
    val scheduleLiveData: LiveData<List<Schedule>>
        get() = _schuduelLiveData

    fun getSchedule(){

    }
}