package besmart.elderlycare.screen.question.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.evaluation.*
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.screen.question.QuestionViewModel
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class QuestHistorySingleViewModel(private val repository: EvaluationRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<QuestionViewModel.EvaluationItem>()
    val evaluationLiveData: LiveData<QuestionViewModel.EvaluationItem>
        get() = _evaluationLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getQuestion(userEvaluarion: UserEvaluarion) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getQuestion(userEvaluarion.evaluationID.toString()).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        val result =
                            response.body()?.questions?.map { mapQuestionToAdapterItem(it) }
                        _evaluationLiveData.value =
                            QuestionViewModel.EvaluationItem(result, userEvaluarion)
//                        getEvaluationHistory(cardID, evaluationID, result)
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

    private fun mapQuestionToAdapterItem(question: Question): QuestItem {
        return if (!question.questionNo?.contains(".")!! && question.choices.isNullOrEmpty()) {
            QuestItem(QuestType.HEADER, question)
        } else if (question.questionNo.contains(".")) {
            QuestItem(QuestType.CHOINCE, question)
        } else if (!question.questionNo.contains(".") && question.choices!!.isNotEmpty()) {
            QuestItem(QuestType.HEADERWITHCHOINCE, question)
        } else {
            QuestItem(QuestType.HEADER, question)
        }
    }
}