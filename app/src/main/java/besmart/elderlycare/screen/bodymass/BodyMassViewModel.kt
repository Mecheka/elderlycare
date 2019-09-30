package besmart.elderlycare.screen.bodymass

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.repository.BodyMassRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import com.github.mikephil.charting.data.Entry

class BodyMassViewModel(private val repository: BodyMassRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _chartLiveData = MutableLiveData<List<BodyMassResponce>>()
    val chartLiveData: LiveData<List<BodyMassResponce>>
        get() = _chartLiveData

    private val _historyLiveData = MutableLiveData<BodyMassResponce>()
    val historyLiveData: LiveData<BodyMassResponce>
        get() = _historyLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    val heavy = ObservableField<String>()
    val height = ObservableField<String>()
    val bmi = ObservableField<String>()
    val weigthResult = ObservableField<String>()
    var history = mutableListOf<BodyMassResponce>()


    fun getBodymassLastIndex(cardID: String?) {
        addDisposable(
            repository.getBodyMassLastIndex(cardID!!).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
                            val lastIndex = response.body()?.get(0)
                            heavy.set(lastIndex?.weight?.toString() ?: "0.0")
                            height.set(lastIndex?.height?.toString() ?: "0.0")
                            bmi.set(lastIndex?.bMI?.toString() ?: "0.0")
                            weigthResult.set(lastIndex?.getWeigthResultByBMI() ?: "น้ำหนักปกติ")
                            _historyLiveData.value = lastIndex
                        }else{
                            heavy.set("-")
                            height.set("-")
                            bmi.set("-")
                            weigthResult.set("-")
                        }
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

    fun getBodymassHistory(cardID: String?, year: String = "2019", month: String = "7") {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getBodyMassHistory(cardID!!, year, month).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        history.clear()
                        if (response.body()?.isNotEmpty()!!) {
                            history.addAll(response.body()!!)
                            _chartLiveData.value = history
                        } else {
                            _chartLiveData.value = history
                        }
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