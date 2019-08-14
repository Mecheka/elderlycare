package besmart.elderlycare.screen.evaluation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class EvaluationViewModel(private val repository: EvaluationRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<List<EvaluationResponse>>()
    val evaluationLiveData: LiveData<List<EvaluationResponse>>
        get() = _evaluationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getEvaluation() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getEvaluation().subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful){
                        _evaluationLiveData.value = response.body()
                    }else{
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                }, { error ->
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                })
        )
    }
}