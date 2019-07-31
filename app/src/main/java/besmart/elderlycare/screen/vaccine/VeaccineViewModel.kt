package besmart.elderlycare.screen.vaccine

import androidx.lifecycle.LiveData
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel

class VeaccineViewModel :BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

}