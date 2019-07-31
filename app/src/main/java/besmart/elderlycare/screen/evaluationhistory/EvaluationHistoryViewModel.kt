package besmart.elderlycare.screen.evaluationhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class EvaluationHistoryViewModel(private val repository: EvaluationRepository) : BaseViewModel() {
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<List<BloodPressuresResponse>>()
    val evaluationLiveData: LiveData<List<BloodPressuresResponse>>
        get() = _evaluationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getEvaluationHistory(cardID: String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getEvaluationLastIntex(cardID).subscribe(
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
}