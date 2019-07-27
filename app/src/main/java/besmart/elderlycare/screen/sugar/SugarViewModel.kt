package besmart.elderlycare.screen.sugar

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.repository.SugarRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import com.github.mikephil.charting.data.Entry

class SugarViewModel(private val repository: SugarRepository) :BaseViewModel(){
    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _chartLiveData = MutableLiveData<List<Entry>>()
    val chartLiveData: LiveData<List<Entry>>
        get() = _chartLiveData

    private val _historyLiveData = MutableLiveData<List<SugarResponse>>()
    val historyLiveData: LiveData<List<SugarResponse>>
        get() = _historyLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val entryList = mutableListOf<Entry>()
    var history= listOf<SugarResponse>()
    val fbs = ObservableField<String>()


    fun getSugarLastIndex(cardID: String?) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getSugarLastIndex(cardID!!).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        val lastIndex = response.body()?.get(0)
                        fbs.set(lastIndex?.fbs.toString())
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

    fun getSugarHistory(cardID: String?, year: String = "2019", month: String = "7") {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getSugarHistory(cardID!!, year, month).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        response.body()?.forEachIndexed { index, sugar ->
                            entryList.add(mapListToEntry(index, sugar))
                        }
                        _chartLiveData.value = entryList
                        history = response.body()!!
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

    private fun mapListToEntry(
        index: Int, sugar: SugarResponse
    ): Entry {
        return Entry(index.toFloat(), sugar.fbs!!.toFloat())
    }
}