package besmart.elderlycare.screen.knowledge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.knowlege.KnowlegeResponce
import besmart.elderlycare.repository.DevmanRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class KnowlegeViewModel(private val repository: DevmanRepository) :BaseViewModel(){

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _knowlegeLiveData = MutableLiveData<List<KnowlegeResponce>>()
    val knowlegeLiveData: LiveData<List<KnowlegeResponce>>
        get() = _knowlegeLiveData

    fun getAllKnowlege(){
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAllKnowLege().subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    _knowlegeLiveData.value = responce.body()
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