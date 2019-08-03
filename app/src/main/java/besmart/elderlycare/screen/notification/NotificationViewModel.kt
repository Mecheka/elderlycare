package besmart.elderlycare.screen.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.notification.NotificationResponce
import besmart.elderlycare.repository.AlertRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class NotificationViewModel(private val repository: AlertRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _notificationLiveData = MutableLiveData<List<NotificationResponce>>()
    val notificationLiveData: LiveData<List<NotificationResponce>>
        get() = _notificationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getNotification() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAlert().subscribe({ response ->
                _loadingLiveEvent.sendAction(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _notificationLiveData.value = it
                    }
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