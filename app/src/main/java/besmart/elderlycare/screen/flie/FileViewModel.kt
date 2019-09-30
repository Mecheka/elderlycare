package besmart.elderlycare.screen.flie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.devman.DevmanResponce
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.repository.DevmanRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class FileViewModel constructor(private val repository: DevmanRepository) : BaseViewModel() {
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _fileLiveData = MutableLiveData<DevmanResponce<FileData>>()
    val devmanLiveData: LiveData<DevmanResponce<FileData>>
        get() = _fileLiveData

    fun getAllFile() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAllFile().subscribe({ response ->
                _loadingLiveEvent.sendAction(false)
                if (response.isSuccessful) {
                    _fileLiveData.value = response.body()
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