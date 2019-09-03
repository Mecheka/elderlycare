package besmart.elderlycare.screen.question.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.evaluation.UserEvaluarion
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class QuestionHistoryViewModel(private val repository: EvaluationRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<List<UserEvaluarion>>()
    val evaluationLiveData: LiveData<List<UserEvaluarion>>
        get() = _evaluationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getEvaluetionHistory(evaluationId: Int, cardID: String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getEvaluationHistory(cardID, evaluationId.toString()).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _evaluationLiveData.value = response.body()
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