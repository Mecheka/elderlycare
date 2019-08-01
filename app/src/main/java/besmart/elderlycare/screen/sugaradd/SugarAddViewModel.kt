package besmart.elderlycare.screen.sugaradd

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import besmart.elderlycare.model.sugar.SugarReqeust
import besmart.elderlycare.repository.SugarRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import java.text.SimpleDateFormat
import java.util.*

class SugarAddViewModel(private val repository: SugarRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<String>()
    val successLiveEvent: LiveData<String>
        get() = _successLiveEvent

    val date = ObservableField<String>()
    val time = ObservableField<String>()
    val result = ObservableField<String>()
    val blood = ObservableField<String>()

    @SuppressLint("SimpleDateFormat")
    fun addSugarBlood(cardID: String) {
        _loadingLiveEvent.sendAction(true)
        if (validateData()) {
            val dateInput = "${date.get()} ${time.get()}"
            val calendar = Calendar.getInstance()
            val inputDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("TH"))
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            calendar.time = inputDateFormat.parse(dateInput)
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 543)
            val dateBody = outputDateFormat.format(calendar.time)
            val body = SugarReqeust(cardID, dateBody, blood.get()!!.toInt())
            addDisposable(
                repository.addSugarBlood(body).subscribe(
                    { response ->
                        _loadingLiveEvent.sendAction(false)
                        if (response.isSuccessful) {
                            _successLiveEvent.sendAction(dateBody)
                        } else {
                            val errorResponse = response.errorBody()
                            _errorLiveEvent.sendAction(
                                HandingNetworkError.getErrorMessage(
                                    errorResponse!!
                                )
                            )
                        }
                    },
                    { error ->
                        _loadingLiveEvent.sendAction(false)
                        _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                    })
            )
        }
    }

    private fun validateData(): Boolean {
        if (date.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please select day")
            return false
        }
        if (time.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please select time")
            return false
        }
        if ((blood.get().isNullOrEmpty())) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your FBS")
            return false
        }
        return true
    }
}