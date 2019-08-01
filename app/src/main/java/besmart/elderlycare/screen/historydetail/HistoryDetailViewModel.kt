package besmart.elderlycare.screen.historydetail

import androidx.lifecycle.LiveData
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.history.HistoryResponce
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

    private val _bodymassLiveEvent = ActionLiveData<BodyMassResponce>()
    val bodymassLiveEvent: LiveData<BodyMassResponce>
        get() = _bodymassLiveEvent

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
                    repoBody.getHistoryByDate(history.dataID.toString()).subscribe(
                        { response ->
                            _loadingLiveEvent.sendAction(false)
                            if (response.isSuccessful) {

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