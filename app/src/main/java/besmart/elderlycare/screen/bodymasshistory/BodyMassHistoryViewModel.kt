package besmart.elderlycare.screen.bodymasshistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.repository.BodyMassRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class BodyMassHistoryViewModel(private val repository: BodyMassRepository) : BaseViewModel() {
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _bodyMassLiveData = MutableLiveData<List<BodyMassResponce>>()
    val bodyMassLiveData: LiveData<List<BodyMassResponce>>
        get() = _bodyMassLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getBodyMassHistory(cardID: String) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getBodyMassLastIndex(cardID).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _bodyMassLiveData.value = response.body()
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