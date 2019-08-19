package besmart.elderlycare.screen.villagehealthvoluntor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel

class VillageHealthVolanteerViewModel :BaseViewModel(){

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
}