package besmart.elderlycare.screen.historydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.repository.BloodPresureRepository
import besmart.elderlycare.repository.BodyMassRepository
import besmart.elderlycare.repository.SugarRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import retrofit2.Response

class HistoryDetailViewModel(
    private val repoBody: BodyMassRepository,
    private val repoBloodPresure: BloodPresureRepository,
    private val repoSugar: SugarRepository
) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _bodymassLiveEvent = ActionLiveData<List<BodyMassResponce>>()
    val bodymassLiveEvent: LiveData<List<BodyMassResponce>>
        get() = _bodymassLiveEvent

    private val _bloodPressureLiveEvent = ActionLiveData<List<BloodPressuresResponse>>()
    val bloodPressure: LiveData<List<BloodPressuresResponse>>
        get() = _bloodPressureLiveEvent

    private val _sugarLiveEvent = ActionLiveData<List<SugarResponse>>()
    val sugarLiveEvent: LiveData<List<SugarResponse>>
        get() = _sugarLiveEvent

    private val _removeSuccessLiveEvent = MutableLiveData<Boolean>()
    val removeSuccessLiveEvent: LiveData<Boolean>
        get() = _removeSuccessLiveEvent

    fun getHistoryDetail(history: HistoryResponce) {
        _loadingLiveEvent.sendAction(true)
        when (history.typeID) {
            1 -> {
                addDisposable(
                    repoBody.getHistoryByDate(history.dataID.toString()).subscribe(
                        { response ->
                            handleResponse(response, _bodymassLiveEvent)
                        }, { error ->
                            handleError(error)
                        })
                )
            }
            2 -> {
                addDisposable(
                    repoBloodPresure.getHistoryByDataID(history.dataID.toString()).subscribe(
                        { response ->
                            handleResponse(response, _bloodPressureLiveEvent)
                        }, { error ->
                            handleError(error)
                        })
                )
            }
            else -> {
                addDisposable(
                    repoSugar.getHistoryByDataID(history.dataID.toString()).subscribe(
                        { response ->
                            handleResponse(response, _sugarLiveEvent)
                        }, { error ->
                            handleError(error)
                        })
                )
            }
        }
    }

    fun removeHistoryByItemType(item: Any?, typeID: Int?) {
        _loadingLiveEvent.sendAction(true)
        when (typeID) {
            // Body Mass
            1 -> {
                val bodymass = item as BodyMassResponce
                addDisposable(
                    repoBody.removeBodyMass(bodymass.id.toString()).subscribe({ response ->
                        handleRemove(response)
                    }, { error ->
                        handleError(error)
                    })
                )
            }
            // Blood pressure
            2 -> {
                val bloodPressure = item as BloodPressuresResponse
                addDisposable(
                    repoBloodPresure.removeBloodPressure(bloodPressure.id.toString()).subscribe({ response ->
                        handleRemove(response)
                    }, { error ->
                        handleError(error)
                    })
                )
            }
            else -> {
                // Sugar
                val sugar = item as SugarResponse
                addDisposable(
                    repoSugar.removeSugarBloodHistory(sugar.id.toString()).subscribe({ response ->
                        handleRemove(response)
                    }, { error ->
                        handleError(error)
                    })
                )
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>, yourLiveData: MutableLiveData<T>) {
        _loadingLiveEvent.sendAction(false)
        if (response.isSuccessful) {
            yourLiveData.value = response.body()
        } else {
            val errorResponse = response.errorBody()
            _errorLiveEvent.sendAction(
                HandingNetworkError.getErrorMessage(
                    errorResponse!!
                )
            )
        }
    }

    private fun <T> handleRemove(response: Response<T>) {
        _loadingLiveEvent.sendAction(false)
        if (response.isSuccessful) {
            _removeSuccessLiveEvent.value = true
        } else {
            val errorResponse = response.errorBody()
            _errorLiveEvent.sendAction(
                HandingNetworkError.getErrorMessage(
                    errorResponse!!
                )
            )
        }
    }

    private fun handleError(error: Throwable) {
        _loadingLiveEvent.sendAction(false)
        _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
    }
}