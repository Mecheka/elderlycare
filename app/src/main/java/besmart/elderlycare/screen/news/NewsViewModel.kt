package besmart.elderlycare.screen.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.devman.DevmanResponce
import besmart.elderlycare.model.news.NewsData
import besmart.elderlycare.repository.DevmanRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class NewsViewModel(private val repository: DevmanRepository) :BaseViewModel(){

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _newsLiveData = MutableLiveData<DevmanResponce<NewsData>>()
    val newsLiveData: LiveData<DevmanResponce<NewsData>>
        get() = _newsLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getAllNews(){
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAllNews().subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    _newsLiveData.value = responce.body()
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