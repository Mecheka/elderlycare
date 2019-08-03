package besmart.elderlycare.screen.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class ChatViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _chatLiveData = MutableLiveData<List<ProfileResponce>>()
    val chatLiveData: LiveData<List<ProfileResponce>>
        get() = _chatLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getAllUser() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAllProfile().subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    _chatLiveData.value = responce.body()
                } else {
                    responce.errorBody()?.let {
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