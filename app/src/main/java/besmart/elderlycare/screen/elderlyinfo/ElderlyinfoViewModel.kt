package besmart.elderlycare.screen.elderlyinfo

import androidx.lifecycle.LiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class ElderlyinfoViewModel(
    private val repository: ElderlyRepository,
    private val profileRepo: ProfileRepository
) :
    BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    private val _profileLiveEvent = ActionLiveData<ProfileResponce>()
    val profileLiveEvent: LiveData<ProfileResponce>
        get() = _profileLiveEvent

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

    fun getProfileByCardId(profile: ProfileResponce) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            profileRepo.getProfileByCardId(profile.cardID!!).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _profileLiveEvent.sendAction(response.body()!!)
                    } else {
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                },
                { error ->
                    _loadingLiveEvent.sendAction(false)
                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                })
        )
    }
}