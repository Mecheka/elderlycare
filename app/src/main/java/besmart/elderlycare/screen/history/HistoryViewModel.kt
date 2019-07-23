package besmart.elderlycare.screen.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.HistoryRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class HistoryViewModel(private val repository: HistoryRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _historyLiveData = MutableLiveData<List<HistoryResponce>>()
    val historyLiveData: LiveData<List<HistoryResponce>>
        get() = _historyLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getHistoryByCardId(profile: ProfileResponce) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getHistoryByCardId(profile.cardID!!).subscribe({ response ->
                _loadingLiveEvent.sendAction(false)
                if (response.isSuccessful) {
                    _historyLiveData.value = response.body()
                } else {
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