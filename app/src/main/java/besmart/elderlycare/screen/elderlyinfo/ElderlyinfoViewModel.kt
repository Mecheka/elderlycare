package besmart.elderlycare.screen.elderlyinfo

import androidx.lifecycle.LiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class ElderlyinfoViewModel(private val repository: ElderlyRepository) :BaseViewModel(){

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    fun removeElderly(profile:ProfileResponce){
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.removeElderly(profile.cardID!!).subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    _successLiveEvent.sendAction(true)
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