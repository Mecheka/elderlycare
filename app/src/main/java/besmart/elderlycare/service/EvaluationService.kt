package besmart.elderlycare.service

import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.evaluation.QuestionResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EvaluationService {

    @GET("/api/v1.0/evaluations")
    fun getEvaluation(): Single<Response<List<EvaluationResponse>>>

    @GET("/api/v1.0/evaluations/{evaluationID}/questions")
    fun getQuestion(@Path("evaluationID") evaluationID: String): Single<Response<QuestionResponse>>
}