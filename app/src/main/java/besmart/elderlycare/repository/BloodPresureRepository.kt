package besmart.elderlycare.repository

import besmart.elderlycare.model.blood.BloodPressuresRequest
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class BloodPresureRepository(private val service: CommonWithAuth) {

    fun getBloodPresureLastIntex(cardID: String): Single<Response<List<BloodPressuresResponse>>> {
        return service.getBloodPresureService().getBloodPressuresLastIndex(cardID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getBloodPresureHistory(
        cardId: String,
        year: String,
        month: String
    ): Single<Response<List<BloodPressuresResponse>>> {
        return service.getBloodPresureService().getBloodPressuresHistory(cardId, year, month)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addBloodPresure(body: BloodPressuresRequest): Single<Response<ResponseBody>> {
        return service.getBloodPresureService().addBloodPressures(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getHistoryByDataID(dataID: String): Single<Response<List<BloodPressuresResponse>>> {
        return service.getBloodPresureService().getHistoryByDataID(dataID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}