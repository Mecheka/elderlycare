package besmart.elderlycare.screen.blood

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class BloodPressureViewModel(private val repository: EvaluationRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _chartLiveData = MutableLiveData<List<BloodPressuresResponse>>()
    val chartLiveData: LiveData<List<BloodPressuresResponse>>
        get() = _chartLiveData

    private val _historyLiveData = MutableLiveData<BloodPressuresResponse>()
    val historyLiveData: LiveData<BloodPressuresResponse>
        get() = _historyLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    val sys = ObservableField<String>()
    val dia = ObservableField<String>()
    val result = ObservableField<String>()
    var history = listOf<BloodPressuresResponse>()

    fun getBloodPressureLastIndex(cardID: String) {
        addDisposable(
            repository.getEvaluationLastIntex(cardID).subscribe(
                { response ->
                    if (response.isSuccessful) {
                        val lastIndex = response.body()?.get(0)
                        sys.set(lastIndex?.sys?.toString())
                        dia.set(lastIndex?.dia?.toString())
                        result.set(lastIndex?.getResult())
                        _historyLiveData.value = lastIndex
                    } else {
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                }, { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                }
            )
        )
    }

    fun getBloodPressureHistory(cardID: String, year: String, month: String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getEvaluationHistory(cardID, year, month).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _chartLiveData.value = response.body()
                        history = response.body()!!
                    } else {
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                }, { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                }
            )
        )
    }
}