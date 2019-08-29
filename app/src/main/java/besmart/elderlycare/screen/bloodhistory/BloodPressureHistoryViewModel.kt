package besmart.elderlycare.screen.bloodhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.repository.BloodPresureRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class BloodPressureHistoryViewModel(private val repository: BloodPresureRepository) : BaseViewModel() {
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<List<BloodPressuresResponse>>()
    val evaluationLiveData: LiveData<List<BloodPressuresResponse>>
        get() = _evaluationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _removeSuccessLiveEvent = ActionLiveData<Boolean>()
    val removeSuccessLiveEvent: LiveData<Boolean>
        get() = _removeSuccessLiveEvent

    fun getEvaluationHistory(cardID: String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getBloodPresureLastIntex(cardID).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _evaluationLiveData.value = response.body()
                    } else {
                        val errorResponse = response.errorBody()
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(errorResponse!!))
                    }
                }, { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                })
        )
    }

    fun removeBloodPressureHistory(item: BloodPressuresResponse) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.removeBloodPressure(item.id.toString()).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _removeSuccessLiveEvent.value = true
                    } else {
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                }, { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                })

        )
    }
}