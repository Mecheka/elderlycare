package besmart.elderlycare.screen.villagehealthvoluntor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.villageitem.VillageItem
import besmart.elderlycare.model.villageitem.VillageType
import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.VillageHealthVolunteerRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class VillageHealthVolunteerViewModel(private val repository: VillageHealthVolunteerRepository, private val elderlyRepo: ElderlyRepository) :
    BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _profileLiveData = MutableLiveData<MutableList<VillageItem>>()
    val profileLiveData: LiveData<MutableList<VillageItem>>
        get() = _profileLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    fun getMyOrsomor(profile: ProfileResponce, typeId: String = "1") {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getProfileByCardIdAndTypeId(profile.cardID!!, typeId).subscribe(
                { response ->
                    if (response.isSuccessful) {
                        getElderlyByStaffId(response.body()?.id.toString(), profile)
                    } else {
                        _loadingLiveEvent.sendAction(false)
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

    fun removeElderly(profile: ProfileResponce) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            elderlyRepo.removeElderly(profile.cardID!!).subscribe({ responce ->
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

    private fun getElderlyByStaffId(
        typeId: String,
        profile: ProfileResponce
    ) {
        addDisposable(
            repository.getElderlyByStaffId(typeId).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        val list = mutableListOf<VillageItem>()
                        list.add(VillageItem(VillageType.TILTE, profile))
                        list.add(VillageItem(VillageType.HEAD, "ผู้สูงอายุที่ดูแล"))
                        response.body()?.forEach {
                            list.add(VillageItem(VillageType.LIST, it))
                        }

                        _profileLiveData.value = list
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