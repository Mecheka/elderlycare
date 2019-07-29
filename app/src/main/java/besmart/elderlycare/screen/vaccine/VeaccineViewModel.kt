package besmart.elderlycare.screen.vaccine

import androidx.lifecycle.LiveData
import besmart.elderlycare.repository.VeaccineRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel

class VeaccineViewModel(private val repository: VeaccineRepository):BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getVeaccine(){

    }
}