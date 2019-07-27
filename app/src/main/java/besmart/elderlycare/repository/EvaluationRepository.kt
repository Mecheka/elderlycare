package besmart.elderlycare.repository

import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
}