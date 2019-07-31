package besmart.elderlycare.repository

import besmart.elderlycare.model.blood.BloodPressuresRequest
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class EvaluationRepository(private val service: CommonWithAuth) {

    fun getEvaluationLastIntex(cardID: String): Single<Response<List<BloodPressuresResponse>>> {
        return service.getEvaluationService().getBloodPressuresLastIndex(cardID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getEvaluationHistory(
        cardId: String,
        year: String,
        month: String
    ): Single<Response<List<BloodPressuresResponse>>> {
        return service.getEvaluationService().getBloodPressuresHistory(cardId, year, month)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addEvaluation(body: BloodPressuresRequest): Single<Response<ResponseBody>> {
        return service.getEvaluationService().addBloodPressures(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}