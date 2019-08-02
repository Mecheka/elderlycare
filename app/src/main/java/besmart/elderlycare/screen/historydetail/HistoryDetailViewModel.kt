package besmart.elderlycare.screen.historydetail

import androidx.lifecycle.LiveData
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.repository.BodyMassRepository
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.repository.SugarRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class HistoryDetailViewModel(
    private val repoBody: BodyMassRepository,
    private val repoEvaluation: EvaluationRepository,
    private val repoBlood: SugarRepository
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

    private val _evaluationLiveEvent = ActionLiveData<List<BloodPressuresResponse>>()
    val evaluationLiveEvent: LiveData<List<BloodPressuresResponse>>
        get() = _evaluationLiveEvent

    private val _sugarLiveEvent = ActionLiveData<List<SugarResponse>>()
    val sugarLiveEvent: LiveData<List<SugarResponse>>
        get() = _sugarLiveEvent

    fun getHistoryDetail(history: HistoryResponce) {
        _loadingLiveEvent.sendAction(true)
        when (history.typeID) {
            1 -> {
                addDisposable(
                    repoBody.getHistoryByDate(history.dataID.toString()).subscribe(
                        { response ->
                            _loadingLiveEvent.sendAction(false)
                            if (response.isSuccessful) {
                                _bodymassLiveEvent.value = response.body()
                            } else {
                                val errorResponse = response.errorBody()
                                _errorLiveEvent.sendAction(
                                    HandingNetworkError.getErrorMessage(
                                        errorResponse!!
                                    )
                                )
                            }
                        }, { error ->
                            _loadingLiveEvent.sendAction(false)
                            _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                        })
                )
            }
            2 -> {
                addDisposable(
                    repoEvaluation.getHistoryByDataID(history.dataID.toString()).subscribe(
                        { response ->
                            _loadingLiveEvent.sendAction(false)
                            if (response.isSuccessful) {
                                _evaluationLiveEvent.value = response.body()
                            } else {
                                val errorResponse = response.errorBody()
                                _errorLiveEvent.sendAction(
                                    HandingNetworkError.getErrorMessage(
                                        errorResponse!!
                                    )
                                )
                            }
                        }, { error ->
                            _loadingLiveEvent.sendAction(false)
                            _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                        })
                )
            }
            else -> {
                addDisposable(
                    repoBlood.getHistoryByDataID(history.dataID.toString()).subscribe(
                        { response ->
                            _loadingLiveEvent.sendAction(false)
                            if (response.isSuccessful) {
                                _sugarLiveEvent.value = response.body()
                            } else {
                                val errorResponse = response.errorBody()
                                _errorLiveEvent.sendAction(
                                    HandingNetworkError.getErrorMessage(
                                        errorResponse!!
                                    )
                                )
                            }
                        }, { error ->
                            _loadingLiveEvent.sendAction(false)
                            _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                        })
                )
            }
        }
    }

}