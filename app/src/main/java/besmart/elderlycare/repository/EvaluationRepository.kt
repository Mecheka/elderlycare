package besmart.elderlycare.repository

import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.evaluation.QuestionRequest
import besmart.elderlycare.model.evaluation.QuestionResponse
import besmart.elderlycare.model.evaluation.UserEvaluarion
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class EvaluationRepository(private val service: CommonWithAuth) {

    fun getEvaluation(): Single<Response<List<EvaluationResponse>>> {
        return service.getEvaluationService().getEvaluation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getQuestion(evaluationID: String): Single<Response<QuestionResponse>> {
        return service.getEvaluationService().getQuestion(evaluationID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getEvaluationHistory(
        cardID: String,
        evaluationID: String
    ): Single<Response<List<UserEvaluarion>>> {
        return service.getEvaluationService().getUserEvaluation(cardID, evaluationID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addAnswer(body: QuestionRequest):Single<Response<ResponseBody>>{
        return service.getEvaluationService().addAnswer(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}