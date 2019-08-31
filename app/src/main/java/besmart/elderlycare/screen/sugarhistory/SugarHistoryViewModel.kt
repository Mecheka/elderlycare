package besmart.elderlycare.screen.sugarhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.repository.SugarRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class SugarHistoryViewModel(private val repository: SugarRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _sugarLiveData = MutableLiveData<List<SugarResponse>>()
    val sugarLiveData: LiveData<List<SugarResponse>>
        get() = _sugarLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _removeSuccessLiveEvent = MutableLiveData<Boolean>()
    val removeSuccessLiveEvent: LiveData<Boolean>
        get() = _removeSuccessLiveEvent

    fun getSugarHistory(cardID:String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getSugarLastIndex(cardID).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _sugarLiveData.value = response.body()
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

    fun removeBodyMassHistory(item: SugarResponse) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.removeSugarBloodHistory(item.id.toString()).subscribe(
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