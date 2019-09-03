package besmart.elderlycare.screen.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.evaluation.*
import besmart.elderlycare.repository.EvaluationRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class QuestionViewModel(private val repository: EvaluationRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _evaluationLiveData = MutableLiveData<EvaluationItem>()
    val evaluationLiveData: LiveData<EvaluationItem>
        get() = _evaluationLiveData

    private val _scroreLiveEvent = ActionLiveData<Answer>()
    val scroreLiveEvent: LiveData<Answer>
        get() = _scroreLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getQuestion(id: Int, cardID: String?, evaluationID: Int?) {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getQuestion(id.toString()).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        val result =
                            response.body()?.questions?.map { mapQuestionToAdapterItem(it) }
                        _evaluationLiveData.value = EvaluationItem(result)
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

//    private fun getEvaluationHistory(
//        cardID: String?,
//        evaluationID: Int?,
//        result: List<QuestItem>?
//    ) {
//        addDisposable(
//            repository.getEvaluationHistory(cardID!!, evaluationID.toString()).subscribe(
//                { response ->
//                    _loadingLiveEvent.sendAction(false)
//                    if (response.isSuccessful) {
//                        _evaluationLiveData.value = EvaluationItem(result, response.body())
//                    } else {
//                        _evaluationLiveData.value = EvaluationItem(result)
//                    }
//                },
//                { error ->
//                    _loadingLiveEvent.sendAction(false)
//                    _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
//                })
//        )
//    }

    fun addAnswer(
        answer: Answer,
        cardID: String?,
        evaluationID: Int?
    ) {
        _loadingLiveEvent.sendAction(true)
        val body = QuestionRequest(cardID!!, evaluationID!!, answer.result, answer.answer)
        addDisposable(
            repository.addAnswer(body).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful) {
                        _scroreLiveEvent.value = answer
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

    data class EvaluationItem(
        val item: List<QuestItem>?,
        val userEvaluarion: UserEvaluarion? = null
    )
}