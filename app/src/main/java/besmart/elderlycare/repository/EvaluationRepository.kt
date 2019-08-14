package besmart.elderlycare.repository

import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.evaluation.QuestionResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
}