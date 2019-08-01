package besmart.elderlycare.screen.elderlyadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class AddElderlyViewModel constructor(
    private val profileRepo: ProfileRepository,
    private val elderlyRepo: ElderlyRepository
) : BaseViewModel() {
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _profileLiveData = MutableLiveData<MutableList<ProfileResponce>>()
    val profileLiveData: LiveData<MutableList<ProfileResponce>>
        get() = _profileLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    fun getAllProfile() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            profileRepo.getAllProfile().subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    val profile =
                        responce.body()?.sortedBy { profile -> profile.cardID }?.toMutableList()
                    _profileLiveData.value = profile
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

    fun getAllProfileWithRefresh() {
        addDisposable(
            profileRepo.getAllProfile().subscribe({ responce ->
                if (responce.isSuccessful) {
                    val profile =
                        responce.body()?.sortedBy { profile -> profile.cardID }?.toMutableList()
                    _profileLiveData.value = profile
                } else {
                    responce.errorBody()?.let {
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                    }
                }
            }, { error ->
                _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
            })
        )
    }

    fun addElderlty(profile: ProfileResponce) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            elderlyRepo.createElderly(profile.cardID).subscribe({ responce ->
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