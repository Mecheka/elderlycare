package besmart.elderlycare.screen.bodymassadd

import androidx.lifecycle.LiveData
import besmart.elderlycare.model.bodymass.BodyMassRequest
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.BodyMassRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import java.text.SimpleDateFormat
import java.util.*

class AddBodyMassViewModel(private val repository: BodyMassRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<String>()
    val successLiveEvent: LiveData<String>
        get() = _successLiveEvent

    fun addBodyMass(
        day: String,
        time: String,
        weight: String,
        height: String,
        profile: ProfileResponce
    ) {
        _loadingLiveEvent.sendAction(true)
        val dateTime = "$day $time"
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale("TH"))
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val input = inputFormat.parse(dateTime)
        val output = outputFormat.format(input)
        val request =
            BodyMassRequest(profile.cardID.toString(), output, height.toFloat(), weight.toFloat())
        addDisposable(
            repository.addBodyMass(request).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _successLiveEvent.sendAction(dateTime)
                    } else {
                        val errorResponse = response.errorBody()
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(errorResponse!!))
                    }
                },
                { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                })
        )
    }
}